import {  Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions} from "../common/select-options";
import {MatSelectChange} from "@angular/material/select";

@Component({
  selector: 'app-transaktion',
  templateUrl: './transaktion.component.html',
  styleUrl: './transaktion.component.scss'
})

export class TransaktionComponent {
  kategorieOptions: SelectOptions[] = KATEGORIE_AUSGABE;

  transaktionForm = new FormGroup({
    'datumTransaktion': new FormControl('', Validators.required),
    'tranksaktionsArt': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),
    'kategorie': new FormControl('', Validators.required),
    'betragAusgabe': new FormControl('', Validators.required),
  })

  get transaktionsArt(): string {
    return this.transaktionForm.controls.tranksaktionsArt?.value ?? '';
  }

  onDatumTransaktionChange() {

  }

  onTransaktonArtChange(art: MatRadioChange) {
    const artValue = art.value;
    this.transaktionForm.get('tranksaktionsArt')?.setValue(artValue);
    this.handleValidators(artValue);
  }

  onBetragEinnahmeChange(einnahmeEvent: Event): void {
    const einnahmeValue = (einnahmeEvent.target as HTMLInputElement).value;
    this.transaktionForm.get('betragEinnahme')?.setValue(einnahmeValue);
  }

  onNotizChange(notizEvent: Event): void {
   const notizValue = (notizEvent.target as HTMLInputElement).value;
   this.transaktionForm.get('notiz')?.setValue(notizValue);
  }

  onKategorieChange(kategorieEvent: MatSelectChange): void {
    const kategorieValue = kategorieEvent.value;
    this.transaktionForm.get('kategorie')?.setValue(kategorieValue);
  }

  onBetragAusgabeChange(ausgabeEvent: Event) {
    const ausgabeValue = (ausgabeEvent.target as HTMLInputElement).value;
    this.transaktionForm.get('betragAusgabe')?.setValue(ausgabeValue);
  }

  onSpeichern() {
    if(this.transaktionForm.invalid) {
      console.log('invalid ', this.transaktionForm);
      return;
    }

    console.log('valid ', this.transaktionForm);

  }

  private handleValidators(artValue: string) {
    let felderToReset: string[] = [];
    let felderToSet: string[] = [];

    if(artValue === 'einnahme') {
      felderToReset = ['kategorie', 'betragAusgabe'];
      felderToSet = ['datumTransaktion', 'betragEinnahme'];
    } else {
      felderToReset = ['betragEinnahme'];
      felderToSet = ['datumTransaktion', 'kategorie', 'betragAusgabe'];
    }

    felderToReset.forEach(feld => { this.clearValidators(feld) });
    felderToSet.forEach(feld => { this.setValidatorsRequired(feld) });
  }


  private clearValidators(feld: string) {
    this.transaktionForm.get(feld)?.clearValidators();
    this.transaktionForm.get(feld)?.updateValueAndValidity();
  }

  private setValidatorsRequired(feld: string) {
    this.transaktionForm.get(feld)?.setValidators([Validators.required]);
    this.transaktionForm.get(feld)?.updateValueAndValidity();
  }
}
