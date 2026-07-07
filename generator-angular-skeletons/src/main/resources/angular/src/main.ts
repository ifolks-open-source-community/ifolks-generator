import { enableProdMode, provideZoneChangeDetection, APP_INITIALIZER } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { environment } from './environments/environment';
import { RestRequestInterceptor } from './app/core/services/RestRequestInterceptor';
import { I18nService } from './app/core/services/I18nService';

if (environment.production) {
  enableProdMode();
}

export function initI18n(i18nService: I18nService) {
  return () => i18nService.init();
}

bootstrapApplication(AppComponent, {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: RestRequestInterceptor, multi: true },
    { provide: APP_INITIALIZER, useFactory: initI18n, deps: [I18nService], multi: true },
    provideRouter(routes)
  ]
}).catch(err => console.error(err));
