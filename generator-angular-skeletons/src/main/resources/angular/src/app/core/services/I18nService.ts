import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class I18nService {
  private translations: any = {};
  private currentLang: string = 'en';

  constructor(private http: HttpClient) {}

  public async init(): Promise<void> {
    const savedLang = localStorage.getItem('lang');
    if (savedLang) {
      this.currentLang = savedLang;
    } else {
      const browserLang = navigator.language ? navigator.language.split('-')[0] : 'en';
      this.currentLang = browserLang === 'en' || browserLang === 'fr' ? browserLang : 'en';
    }

    try {
      const modelPromise = firstValueFrom(this.http.get(`assets/i18n/model.json`));
      const langPromise = firstValueFrom(this.http.get(`assets/i18n/${this.currentLang}.json`));

      const [modelData, langData] = await Promise.all([
        modelPromise.catch(() => ({})),
        langPromise.catch(() => ({}))
      ]);

      this.translations = { ...modelData, ...langData };
    } catch (error) {
      console.error('Failed to load translations', error);
    }
  }

  public translate(key: string): string {
    return this.translations[key] !== undefined ? this.translations[key] : key;
  }

  public getLang(): string {
    return this.currentLang;
  }

  public setLang(lang: string): void {
    localStorage.setItem('lang', lang);
    window.location.reload();
  }
}
