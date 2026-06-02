import { AuthGuard } from './core/services/AuthGuard';
import { Routes } from '@angular/router';

export const routes: Routes = [
/* Specific Code Start */
,{path: '', redirectTo: '/index', pathMatch:'full'}
,{path:'', loadChildren:()=>import('src/app/features/components/index/index.routes').then(m=>m.routes), canActivate: [AuthGuard]}
,{path:'', loadChildren:()=>import('src/app/features/components/auth/auth.routes').then(m=>m.routes)}
,{path:'**', redirectTo: '/index'}
/* Specific Code End */
];
