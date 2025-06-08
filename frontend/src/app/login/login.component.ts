import { Component } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {TransaktionService} from "../service/transaktion.service";
import {Login} from "../models/login.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  standalone: false
})
export class LoginComponent {

  constructor(private transaktionService: TransaktionService,
              private router: Router) {}

  loginForm = new FormGroup({
    'email': new FormControl('', [Validators.required, this.emailValidator()]),
    'password': new FormControl('', Validators.required)
  })

  onAnmelden() {
    console.log('form', this.loginForm)
    this.markFormFieldsAsTouched();

    if(this.loginForm.invalid){
      console.log('Form invalid', this.loginForm);
      return;
    }
    const loginPayload = this.createLoginPayload();
    this.transaktionService.login(loginPayload);
  }

  onKontoErstellen() {
    this.router.navigateByUrl('/register');
  }

  private emailValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const email = control.value;
      const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

      if(!email) {
        return null;
      }

      return regex.test(email) ? null : {invalidEmail: true};
    }
  }

  private markFormFieldsAsTouched(): void {
    this.loginForm.get('email')?.markAsTouched();
    this.loginForm.get('password')?.markAsTouched();
  }

  private createLoginPayload() {
    return {
      email: this.loginForm.get('email')?.value ?? '',
      password: this.loginForm.get('password')?.value ?? ''
    }
  }
}
