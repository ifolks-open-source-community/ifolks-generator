import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { I18nPipe } from 'src/app/core/pipes/I18nPipe';

@Component({
  selector: 'app-bad-request',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule, I18nPipe],
  templateUrl: './bad-request.component.html',
  styleUrl: './bad-request.component.scss'
})
export class BadRequestComponent {}
