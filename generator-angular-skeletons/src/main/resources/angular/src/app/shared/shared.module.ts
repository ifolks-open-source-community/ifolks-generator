import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { RouterModule } from '@angular/router';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { IsoLocalDateNativeDateAdapter, ISO_LOCAL_DATE_FORMAT } from '../core/adapters/IsoLocalDateAdapter';


@NgModule({
  imports: [CommonModule, MaterialModule, RouterModule, ReactiveFormsModule, FormsModule],
  exports: [CommonModule, MaterialModule, RouterModule, ReactiveFormsModule, FormsModule],
  declarations: [],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'en-US' },
    { provide: DateAdapter, useClass: IsoLocalDateNativeDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: ISO_LOCAL_DATE_FORMAT }
  ]
})
export class SharedModule {
}
