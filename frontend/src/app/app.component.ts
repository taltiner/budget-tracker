import { Component } from '@angular/core';
import { TransaktionService } from './service/transaktion.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss',
    standalone: false
})
export class AppComponent {
  title = 'budget-tracker';

  constructor(private transaktionService: TransaktionService) {}

}
