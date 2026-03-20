import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PaymentComponent } from './payment-component/payment-component';

@Component({
  selector: 'app-root',
  imports: [PaymentComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('payment-frontend');
}
