import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

export interface I18nConfig {
  supportedLangs?: string[];
  supportedLocales?: string[];
  defaultLang?: string;
  defaultLocale?: string;
}

@Injectable({
  providedIn: 'root'
})
export class I18nService {
  private translations: Record<string, string> = {};
  private defaultLang: string = 'en';
  private defaultLocale: string = 'en_US';
  private supportedLangs: string[] = ['en'];
  private supportedLocales: string[] = ['en_US'];
  private currentLocale: string = 'en_US';
  private currentLang: string = 'en';

  constructor(private http: HttpClient) {}

  public setSupportedLangs(langs: string[]): void {
    this.supportedLangs = langs;
  }

  public getSupportedLangs(): string[] {
    return this.supportedLangs;
  }

  public setSupportedLocales(locales: string[]): void {
    this.supportedLocales = locales;
  }

  public getSupportedLocales(): string[] {
    return this.supportedLocales;
  }

  public getDefaultLang(): string {
    return this.defaultLang;
  }

  public getDefaultLocale(): string {
    return this.defaultLocale;
  }

  public async init(config?: I18nConfig | string[]): Promise<void> {
    if (Array.isArray(config)) {
      if (config.length > 0) {
        this.supportedLangs = config;
      }
    } else if (config) {
      if (config.supportedLangs && config.supportedLangs.length > 0) {
        this.supportedLangs = config.supportedLangs;
      }
      if (config.supportedLocales && config.supportedLocales.length > 0) {
        this.supportedLocales = config.supportedLocales;
      }
      if (config.defaultLang) {
        this.defaultLang = config.defaultLang;
      }
      if (config.defaultLocale) {
        this.defaultLocale = config.defaultLocale;
      }
    }

    // Migration of legacy 'lang' key if present in localStorage
    const legacyLang = localStorage.getItem('lang');
    if (legacyLang) {
      if (!localStorage.getItem('locale')) {
        localStorage.setItem('locale', legacyLang);
      }
      localStorage.removeItem('lang');
    }

    // Determine candidate locale from storage or browser navigator
    const savedLocale = localStorage.getItem('locale');
    let candidate = this.defaultLocale;

    if (savedLocale) {
      candidate = savedLocale.trim();
    } else if (typeof navigator !== 'undefined' && navigator.language) {
      candidate = navigator.language.trim();
    }

    // Normalize candidate separator to underscore (e.g. fr-FR -> fr_FR)
    const normalizedCandidate = candidate.replace('-', '_');
    const parts = normalizedCandidate.split('_');
    const candidateLang = parts[0].toLowerCase();
    const candidateLocale = parts.length > 1
      ? `${parts[0].toLowerCase()}_${parts[1].toUpperCase()}`
      : candidateLang;

    // RFC 4647 Lookup Fallback Resolution:
    // Tier 1: Exact Locale Match in supportedLocales
    const matchedLocale = this.supportedLocales.find(
      l => l.toLowerCase() === candidateLocale.toLowerCase() ||
           l.replace('-', '_').toLowerCase() === normalizedCandidate.toLowerCase()
    );

    if (matchedLocale) {
      this.currentLocale = matchedLocale;
      this.currentLang = matchedLocale.split(/[-_]/)[0].toLowerCase();
    } else {
      // Tier 2: Base Language Match in supportedLangs
      const matchedLang = this.supportedLangs.find(
        l => l.toLowerCase() === candidateLang
      );

      if (matchedLang) {
        this.currentLang = matchedLang;
        // Check if there is an associated regional locale supported for this language
        const matchingLocale = this.supportedLocales.find(
          l => l.toLowerCase().startsWith(candidateLang + '_') || l.toLowerCase() === candidateLang
        );
        this.currentLocale = matchingLocale || matchedLang;
      } else {
        // Tier 3: Default Fallback
        this.currentLang = this.defaultLang;
        this.currentLocale = this.defaultLocale;
      }
    }

    try {
      const loadJson = (filename: string): Promise<Record<string, string>> =>
        firstValueFrom(this.http.get<Record<string, string>>(`assets/i18n/${filename}`)).catch(() => ({}));

      // Base translation files (default language)
      const promises: Promise<Record<string, string>>[] = [
        loadJson('model.json'),
        loadJson('main.json')
      ];

      // If active language is different from the default base, load the language overlay
      if (this.currentLang !== this.defaultLang && this.supportedLangs.includes(this.currentLang)) {
        promises.push(loadJson(`model_${this.currentLang}.json`));
        promises.push(loadJson(`main_${this.currentLang}.json`));
      }

      // If active locale is different from current language and default locale, load regional overlay
      if (
        this.currentLocale !== this.currentLang &&
        this.currentLocale !== this.defaultLocale &&
        this.supportedLocales.includes(this.currentLocale)
      ) {
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
