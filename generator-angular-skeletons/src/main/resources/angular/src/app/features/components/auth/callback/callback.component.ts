import { Component, OnInit } from '@angular/core';
import { PublicTemplatesModule } from 'src/app/templates/public/templates.module';


@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [PublicTemplatesModule],
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.scss'
})
export class CallbackComponent implements OnInit {

    ngOnInit(): void {

    }
}
