import { Component, OnInit } from '@angular/core';
import { PublicTemplatesModule } from 'src/app/templates/public/templates.module';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [PublicTemplatesModule],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss'
})
export class LogoutComponent implements OnInit {

    ngOnInit(): void {
       
    }
}
