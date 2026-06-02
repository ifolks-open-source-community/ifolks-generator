import { Routes } from '@angular/router';
import { CallbackComponent } from './callback/callback.component';
import { LogoutComponent } from './logout/logout.component';

export const routes: Routes = [
  {path: 'callback', component: CallbackComponent},
  {path: 'logout', component: LogoutComponent}
];
