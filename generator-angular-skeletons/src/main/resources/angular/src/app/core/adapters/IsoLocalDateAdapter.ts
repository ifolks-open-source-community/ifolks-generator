import { Injectable } from '@angular/core';
import { NativeDateAdapter } from '@angular/material/core';

export const ISO_LOCAL_DATE_FORMAT = {
  parse: {
    dateInput: { month: 'short', year: 'numeric', day: 'numeric' },
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: { year: 'numeric', month: 'numeric' },
    dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric', month: 'long' },
  },
};

@Injectable({
  providedIn: 'root'
})
export class IsoLocalDateNativeDateAdapter extends NativeDateAdapter {

  override getYear(date: Date): number {
    return date.getUTCFullYear();
  }

  override getMonth(date: Date): number {
    return date.getUTCMonth();
  }

  override getDate(date: Date): number {
    return date.getUTCDate();
  }

  override getDayOfWeek(date: Date): number {
    return date.getUTCDay();
  }

  override createDate(year: number, month: number, date: number): Date {
    if (month < 0 || month > 11) {
      throw Error(`Invalid month index "${month}". Month index has to be between 0 and 11.`);
    }
    if (date < 1 || date > 31) {
      throw Error(`Invalid date "${date}". Date has to be between 1 and 31.`);
    }
    const result = new Date(Date.UTC(year, month, date));
    if (!this.isValid(result)) {
      throw Error(`Invalid date "${date}" for month with index "${month}".`);
    }
    return result;
  }

  override parse(value: any): Date | null {
    if (typeof value === 'string' && value.trim().length > 0) {
      const parts = value.trim().split('-');
      if (parts.length === 3) {
        const year = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1;
        const day = parseInt(parts[2], 10);
        if (!isNaN(year) && !isNaN(month) && !isNaN(day)) {
          return new Date(Date.UTC(year, month, day));
        }
      }
    }
    return value ? new Date(Date.parse(value)) : null;
  }

  override format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'YYYY-MM-DD') {
      const year = date.getUTCFullYear();
      const month = String(date.getUTCMonth() + 1).padStart(2, '0');
      const day = String(date.getUTCDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
    return super.format(date, displayFormat);
  }
}
