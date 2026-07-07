import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { I18nPipe } from 'src/app/core/pipes/I18nPipe';

@Component({
  selector: 'app-internal-server-error',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule, I18nPipe],
  templateUrl: './internal-server-error.component.html',
  styleUrl: './internal-server-error.component.scss'
})
export class InternalServerErrorComponent {}
