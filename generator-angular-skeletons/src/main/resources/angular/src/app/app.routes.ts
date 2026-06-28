import { AuthGuard } from './core/services/AuthGuard';
import { Routes } from '@angular/router';
import { NotFoundComponent } from './features/components/public/not-found/not-found.component';
import { ForbiddenComponent } from './features/components/public/forbidden/forbidden.component';

export const routes: Routes = [
/* Specific Code Start */
,{path: '', redirectTo: '/index', pathMatch:'full'}
,{path:'', loadChildren:()=>import('src/app/features/components/index/index.routes').then(m=>m.routes), canActivate: [AuthGuard]}
,{path:'', loadChildren:()=>import('src/app/features/components/auth/auth.routes').then(m=>m.routes)}
,{path: '403', component: ForbiddenComponent}
,{path: '404', component: NotFoundComponent}
,{path:'**', redirectTo: '404'}
/* Specific Code End */
];
