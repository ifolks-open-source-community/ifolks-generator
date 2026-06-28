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

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          localStorage.removeItem('access_token');
          window.location.reload();
        } else if (error.status === 404) {
          if (request.method === 'GET') {
            this.router.navigate(['/404']);
          }
        } else if (error.status === 403) {
          if (request.method === 'GET') {
            this.router.navigate(['/403']);
          } else {
            this.notifications.error(error.error?.message || "Action non autorisée");
          }
        } else if (error.status === 409 || (error.status === 400 && request.method !== 'GET')) {
          this.notifications.error(error.error?.message || "Une erreur est survenue");
        } else if (error.status === 500) {
          this.notifications.error("Une erreur technique est survenue sur le serveur.");
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
