import {Component, Injectable} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions, TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
import {TransaktionAusgabe, TransaktionEinnahme} from "../models/transaktion.model";
import {Store} from "@ngrx/store";
import {createTransaktionAusgabe, createTransaktionEinnahme} from "../state/transaktion.actions";
import {TransaktionState} from "../state/transaktion.state";
import {TransaktionService} from "../service/transaktion.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-transaktion',
  templateUrl: './transaktion.component.html',
  styleUrl: './transaktion.component.scss'
})

export class TransaktionComponent {

  constructor(private store: Store<TransaktionState>,
              private transaktionService: TransaktionService,
              private router: Router) {
  }

  kategorieOptions: SelectOptions[] = KATEGORIE_AUSGABE;
  jahrOptions: SelectOptions[] = TRANSAKTION_JAHR;
  monatOptions: SelectOptions[] = TRANSAKTION_MONAT;

  transaktionForm = new FormGroup({
    'datumTransaktion': new FormControl('', Validators.required),
    'jahr': new FormControl('', Validators.required),
    'monat': new FormControl('', Validators.required),
    'tranksaktionsArt': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),
    'kategorie': new FormControl('', Validators.required),
    'betragAusgabe': new FormControl('', Validators.required),
  })

  get datumTransaktion(): string {
    return this.transaktionForm.controls.datumTransaktion.value ?? '';
  }

  get jahrTransaktion(): string {
    return this.transaktionForm.controls.jahr.value ?? '';
  }

  get monatTransaktion(): string {
    return this.transaktionForm.controls.monat.value ?? '';
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

    if(this.transaktionsArt === 'einnahme') {
      const transaktionEinnahme = this.createPayloadEinnahme();

      this.transaktionService.createTransaktion(transaktionEinnahme);
    } else {
      const transaktionAusgabe = this.createPayloadAusgabe();
      this.store.dispatch(createTransaktionAusgabe({transaktionAusgabe}));
    }
  }

  onAbbrechen() {
    this.router.navigate(['/'], {
      queryParams: {} });
  }

  private handleValidators(artValue: string) {
    let felderToReset: string[] = [];
    let felderToSet: string[] = [];

    if(artValue === 'einnahme') {
      felderToReset = ['kategorie', 'betragAusgabe', 'datumTransaktion'];
      felderToSet = ['jahr', 'monat'];
    } else {
      felderToReset = ['betragEinnahme', 'jahr', 'monat'];
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
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
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
