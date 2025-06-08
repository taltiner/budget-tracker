import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {TransaktionService} from "../service/transaktion.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  standalone: false
})
export class RegisterComponent {

  constructor(private transaktionService: TransaktionService) {}

  registerForm = new FormGroup({
    'vorname': new FormControl('', Validators.required),
    'nachname': new FormControl('', Validators.required),
    'email': new FormControl('', Validators.required),
    'password': new FormControl('', Validators.required)
  });

  onRegistrieren() {
    if(this.registerForm.invalid) {
      console.log('registrierung invalid', this.registerForm.value);
    }

    const registrierung = this.createRegisterPayload();

    this.transaktionService.register(registrierung);
  }

  private createRegisterPayload() {
    return {
      vorname: this.registerForm.get('vorname')?.value ?? '',
      nachname: this.registerForm.get('nachname')?.value ?? '',
      email: this.registerForm.get('email')?.value ?? '',
      password: this.registerForm.get('password')?.value ?? ''
    }
  }
}
