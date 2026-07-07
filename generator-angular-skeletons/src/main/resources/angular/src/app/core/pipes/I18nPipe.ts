import { Pipe, PipeTransform } from '@angular/core';
import { I18nService } from '../services/I18nService';

@Pipe({
  name: 'i18n',
  pure: true,
  standalone: true
})
export class I18nPipe implements PipeTransform {
  constructor(private i18nService: I18nService) {}

  transform(key: string): string {
    return this.i18nService.translate(key);
  }
}
