import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions} from "../common/select-options";
import {TransaktionAusgabe, TransaktionEinnahme} from "../models/transaktion.model";
import {Store} from "@ngrx/store";
import {createTransaktionAusgabe, createTransaktionEinnahme} from "../state/transaktion.actions";

@Component({
  selector: 'app-transaktion',
  templateUrl: './transaktion.component.html',
  styleUrl: './transaktion.component.scss'
})

export class TransaktionComponent {

  constructor(private store: Store<any>) {
  }

  kategorieOptions: SelectOptions[] = KATEGORIE_AUSGABE;

  transaktionForm = new FormGroup({
    'datumTransaktion': new FormControl('', Validators.required),
    'tranksaktionsArt': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),
    'kategorie': new FormControl('', Validators.required),
    'betragAusgabe': new FormControl('', Validators.required),
  })

  get datumTransaktion(): string {
    return this.transaktionForm.controls.datumTransaktion.value ?? '';
  }

  get transaktionsArt(): string {
    return this.transaktionForm.controls.tranksaktionsArt?.value ?? '';
  }

  get betragEinnahme(): string {
    return this.transaktionForm.controls.betragEinnahme.value ?? '';
  }

  get notiz(): string {
    return this.transaktionForm.controls.notiz.value ?? '';
  }

  get kategorie(): string {
    return this.transaktionForm.controls.kategorie.value ?? '';
  }

  get betragAusgabe(): string {
    return this.transaktionForm.controls.betragAusgabe.value ?? '';
  }

  onTransaktonArtChange(art: MatRadioChange) {
    const artValue = art.value;
    this.transaktionForm.get('tranksaktionsArt')?.setValue(artValue);
    this.handleValidators(artValue);
  }

  onSpeichern() {
    if(this.transaktionForm.invalid) {
      console.log('invalid ', this.transaktionForm);
      return;
    }

    console.log('valid ', this.transaktionForm);
    if(this.transaktionsArt === 'einnahme') {
      const transaktionEinnahme = this.createPayloadEinnahme();
      this.store.dispatch(createTransaktionEinnahme({transaktionEinnahme}));
    } else {
      const transaktionAusgabe = this.createPayloadAusgabe();
      this.store.dispatch(createTransaktionAusgabe({transaktionAusgabe}));
    }

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


  private createPayloadEinnahme(): TransaktionEinnahme {
    const payload: TransaktionEinnahme = {
      datumTransaktion: this.datumTransaktion,
      tranksaktionsArt: this.transaktionsArt,
      betragEinnahme: this.betragEinnahme,
      notiz: this.notiz
    }

    return payload;
  }

  private createPayloadAusgabe(): TransaktionAusgabe {
    const payload: TransaktionAusgabe = {
      datumTransaktion: this.datumTransaktion,
      tranksaktionsArt: this.transaktionsArt,
      kategorie: this.kategorie,
      betragAusgabe: this.betragAusgabe
    }

    return payload;
  }
}
