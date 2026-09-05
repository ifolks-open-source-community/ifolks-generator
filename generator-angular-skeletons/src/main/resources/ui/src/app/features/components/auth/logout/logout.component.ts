import { Component, OnInit } from '@angular/core';
import { PublicTemplatesModule } from 'src/app/templates/public/templates.module';
import { I18nPipe } from 'src/app/core/pipes/I18nPipe';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [PublicTemplatesModule, I18nPipe],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss'
})
export class LogoutComponent implements OnInit {

    ngOnInit(): void {
       
    }
}
