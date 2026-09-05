import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class I18nService {
  private translations: Record<string, string> = {};
  private currentLocale: string = 'en';
  private currentLang: string = 'en';

  constructor(private http: HttpClient) {}

  public async init(): Promise<void> {
    const legacyLang = localStorage.getItem('lang');
    if (legacyLang) {
      if (!localStorage.getItem('locale')) {
        localStorage.setItem('locale', legacyLang);
      }
      localStorage.removeItem('lang');
    }

    const savedLocale = localStorage.getItem('locale');
    if (savedLocale) {
      this.currentLocale = savedLocale.replace('-', '_');
    } else if (navigator.language) {
      this.currentLocale = navigator.language.replace('-', '_');
    } else {
      this.currentLocale = 'en';
    }

    this.currentLang = this.currentLocale.split('_')[0];

    try {
      const loadJson = (filename: string): Promise<Record<string, string>> =>
        firstValueFrom(this.http.get<Record<string, string>>(`assets/i18n/${filename}`)).catch(() => ({}));

      const promises: Promise<Record<string, string>>[] = [
        loadJson('model.json'),
        loadJson('main.json')
      ];

      if (this.currentLang) {
        promises.push(loadJson(`model_${this.currentLang}.json`));
        promises.push(loadJson(`main_${this.currentLang}.json`));
      }

      if (this.currentLocale && this.currentLocale !== this.currentLang) {
        promises.push(loadJson(`model_${this.currentLocale}.json`));
        promises.push(loadJson(`main_${this.currentLocale}.json`));
      }

      const results = await Promise.all(promises);
      this.translations = Object.assign({}, ...results);
    } catch (error) {
      console.error('Failed to load translations', error);
    }
  }

  public translate(key: string, params?: Record<string, any>): string {
    let value = this.translations[key] !== undefined ? this.translations[key] : key;
    if (params && typeof params === 'object') {
      Object.keys(params).forEach(paramKey => {
        const paramVal = params[paramKey] !== null && params[paramKey] !== undefined ? String(params[paramKey]) : '';
        value = value.replace(new RegExp(`\\{\\{\\s*${paramKey}\\s*\\}\\}|\\{${paramKey}\\}`, 'g'), paramVal);
      });
    }
    return value;
  }

  public getLocale(): string {
    return this.currentLocale;
  }

  public setLocale(locale: string): void {
    localStorage.setItem('locale', locale);
    window.location.reload();
  }

  public getLang(): string {
    return this.currentLang;
  }

  public setLang(lang: string): void {
    this.setLocale(lang);
  }
}
