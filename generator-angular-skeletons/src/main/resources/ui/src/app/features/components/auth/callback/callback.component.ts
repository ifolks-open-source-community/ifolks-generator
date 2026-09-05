import { Component, OnInit } from '@angular/core';
import { PublicTemplatesModule } from 'src/app/templates/public/templates.module';
import { I18nPipe } from 'src/app/core/pipes/I18nPipe';

@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [PublicTemplatesModule, I18nPipe],
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.scss'
})
export class CallbackComponent implements OnInit {

    ngOnInit(): void {

    }
}
