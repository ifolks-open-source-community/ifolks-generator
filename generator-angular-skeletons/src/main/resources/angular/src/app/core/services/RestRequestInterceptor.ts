import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NotificationService } from './NotificationService';

@Injectable()
export class RestRequestInterceptor implements HttpInterceptor {

  constructor(private router: Router, private notifications: NotificationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (request.url.includes('/assets/') || request.url.startsWith('assets/')) {
      return next.handle(request);
    }

    const token = localStorage.getItem('access_token');

    if (token) {
      if (this.isTokenExpired(token)) {
        localStorage.removeItem('access_token');
        window.location.reload();
        return throwError(() => new Error('Token expired'));
      }

      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    const isReadRequest = request.method === 'GET' || request.url.includes('/scroll') || request.url.includes('/search');

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          localStorage.removeItem('access_token');
          window.location.reload();
        } else if (error.status === 404) {
          if (isReadRequest) {
            this.router.navigate(['/404']);
          }
        } else if (error.status === 403) {
          if (isReadRequest) {
            this.router.navigate(['/403']);
          } else {
            this.notifications.error(error.error?.detail || "Action non autorisée");
          }
        } else if (error.status === 409 || (error.status === 400 && !isReadRequest)) {
          this.notifications.error(error.error?.detail || "Une erreur est survenue");
        } else if (error.status === 0 || error.status >= 500) {
          if (isReadRequest) {
            this.router.navigate(['/500']);
          } else {
            this.notifications.error("Une erreur technique est survenue sur le serveur.");
          }
        }
        return throwError(() => error);
      })
    );
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.exp) {
        const expirationDate = new Date(payload.exp * 1000);
        return expirationDate < new Date();
      }
    } catch (e) {
      return true;
    }
    return false;
  }
}
