import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { I18nService } from "./I18nService";

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private snackBar: MatSnackBar, private i18n: I18nService) {}

  public info(message: string, params?: Record<string, any>) {
    const translatedMessage = this.i18n.translate(message, params);
    this.snackBar.open(translatedMessage, 'X', {
      duration: 5000,
      verticalPosition: "top",
      horizontalPosition: "center",
      panelClass: ["notification-info-style"]
    });
  }

  public error(message: string, params?: Record<string, any>) {
    const translatedMessage = this.i18n.translate(message, params);
    this.snackBar.open(translatedMessage, 'X', {
      duration: 5000,
      verticalPosition: "top",
      horizontalPosition: "center",
      panelClass: ["notification-error-style"]
    });
  }
}
