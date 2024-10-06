import {  Component } from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-transaktion',
  templateUrl: './transaktion.component.html',
  styleUrl: './transaktion.component.scss'
})

export class TransaktionComponent {
  transaktionForm = new FormGroup({
    'einnahme': new FormControl('', Validators.required),
    'datumEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),
    'tranksaktionsArt': new FormControl('', Validators.required)
  })

  einnahmeChange() {
    console.log('einnahme entered')
  }

  notizChange() {
    console.log('notiz entered');
  }

}
