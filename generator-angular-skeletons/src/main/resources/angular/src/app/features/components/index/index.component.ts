import { Component, OnInit } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { PrivateTemplatesModule } from 'src/app/templates/private/templates.module';


@Component({
  selector: 'app-index',
  standalone: true,
  imports: [SharedModule, PrivateTemplatesModule],
  templateUrl: './index.component.html',
  styleUrl: './index.component.scss'
})
export class IndexComponent implements OnInit {

    constructor() {
    }
    
    ngOnInit(): void {
        
    }
}
