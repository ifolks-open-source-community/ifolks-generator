import { AuthGuard } from './core/services/AuthGuard';
import { Routes } from '@angular/router';
import { IndexComponent } from './features/components/index/index.component';

export const routes: Routes = [
/* Specific Code Start */
,{path: '', component: IndexComponent, pathMatch: 'full', canActivate: [AuthGuard]}
,{path:'', loadChildren:()=>import('src/app/features/components/errors/errors.routes').then(m=>m.routes)}
,{path:'', loadChildren:()=>import('src/app/features/components/auth/auth.routes').then(m=>m.routes)}
,{path:'**', redirectTo: '404'}
/* Specific Code End */
];
