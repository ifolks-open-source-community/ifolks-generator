import { Component } from '@angular/core';
import { I18nService, SupportedLocale } from 'src/app/core/services/I18nService';

@Component({
  selector: 'app-public-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(public i18nService: I18nService) {}

  get supportedLocales(): SupportedLocale[] {
    return this.i18nService.getSupportedLocales();
  }

  get currentLocale(): string {
    return this.i18nService.getLocale();
  }

  changeLocale(locale: string): void {
    this.i18nService.setLocale(locale);
  }
}
