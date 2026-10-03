import { Routes } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { InternalServerErrorComponent } from './internal-server-error/internal-server-error.component';
import { BadRequestComponent } from './bad-request/bad-request.component';

export const routes: Routes = [

  { path: '400', component: BadRequestComponent },
  { path: '403', component: ForbiddenComponent },
  { path: '404', component: NotFoundComponent },
  { path: '500', component: InternalServerErrorComponent },
];

