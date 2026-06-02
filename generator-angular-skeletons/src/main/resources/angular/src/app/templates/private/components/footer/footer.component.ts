import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-private-footer',
  standalone: false,
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FooterComponent {
}
