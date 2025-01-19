import {Component} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import { MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions, TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
import {EingabeArt, TransaktionAusgabe, TransaktionEinnahme, TransaktionNotiz} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {Router} from "@angular/router";
import {DatePipe} from "@angular/common";

@Component({
    selector: 'app-transaktion',
    templateUrl: './transaktion.component.html',
    styleUrl: './transaktion.component.scss',
    standalone: false
})

export class TransaktionComponent {

  constructor(private transaktionService: TransaktionService,
              private router: Router) {
  }

  kategorieOptions: SelectOptions[] = KATEGORIE_AUSGABE;
  jahrOptions: SelectOptions[] = TRANSAKTION_JAHR;
  monatOptions: SelectOptions[] = TRANSAKTION_MONAT;
  jahr: string = '';
  monat: string = '';

  transaktionForm = new FormGroup({
    'transaktionsArt': new FormControl(undefined, Validators.required),
    'jahr': new FormControl('', Validators.required),
    'monat': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),

    'ausgabeAbschnitte': new FormArray([this.createAusgabeFormGroup()]),
  });

  get jahrTransaktion(): string {
    return this.transaktionForm.controls.jahr.value ?? '';
  }
  get monatTransaktion(): string {
    return this.transaktionForm.controls.monat.value ?? '';
  }
  get transaktionsArt(): EingabeArt | undefined {
    return this.transaktionForm.controls.transaktionsArt?.value || undefined;
  }
  get betragEinnahme(): string {
    const einnahme = this.transaktionForm.controls.betragEinnahme.value?.replace(',', '.');
    return einnahme ?? '';
  }
  get notiz(): string {
    return this.transaktionForm.controls.notiz.value ?? '';
  }
  get ausgabeAbschnitte() {
    return this.transaktionForm.get('ausgabeAbschnitte') as FormArray ?? [];
  }

  onTransaktonArtChange(art: MatRadioChange) {
    const artValue = art.value;
    this.transaktionForm.get('transaktionsArt')?.setValue(artValue);
    this.handleValidators(artValue);
  }

  onKategorieChange(index: number) {
    const ausgabeGroup = this.ausgabeAbschnitte.controls[index];
    if(ausgabeGroup.get('kategorie')?.value !== 'benutzerdefiniert') {
      ausgabeGroup.get('benutzerdefiniert')?.reset();
    }
  }

  onJahrChange(jahr: string) {
    this.jahr = jahr;
    if(this.transaktionsArt === 'ausgabe') {
      this.transaktionForm.controls.ausgabeAbschnitte.controls.forEach(abschnitt => {
        abschnitt.get('jahr')?.setValue(jahr);
      });
    }
  }

  onMonatChange(monat: string) {
    this.monat = monat;
    if(this.transaktionsArt === 'ausgabe') {
      this.transaktionForm.controls.ausgabeAbschnitte.controls.forEach(abschnitt => {
        abschnitt.get('monat')?.setValue(monat);
      });
    }

  }

  onSpeichern() {
    if(this.transaktionForm.invalid) {
      console.log('invalid ', this.transaktionForm);
      return;
    }

    if(this.transaktionsArt && this.transaktionsArt === 'einnahme') {
      const payloadEinnahme = this.createPayloadEinnahme();
      this.transaktionService.createEinnahmeTransaktion(payloadEinnahme);

    } else if(this.transaktionsArt && this.transaktionsArt === 'ausgabe') {
      const payloadAusgaben: TransaktionAusgabe[] = this.prepareForPayloadAusgabe();
      this.transaktionService.createAusgabenTransaktion(payloadAusgaben);

    } else {
      const payloadNotiz = this.createPayloadNotiz();
      this.transaktionService.createNotizTransaktion(payloadNotiz);
    }

    this.router.navigate(['/'], { queryParams: {} });
  }

  onAbbrechen() {
    this.router.navigate(['/'], {queryParams: {} });
  }

  onAusgabeHinzufuegen() {
    const ausgabeAbschnitte = this.transaktionForm.get('ausgabeAbschnitte') as FormArray;
    ausgabeAbschnitte.push(this.createAusgabeFormGroup());
  }

  onAusgabeLoeschen(index: number) {
    this.ausgabeAbschnitte.removeAt(index);
  }

  private handleValidators(artValue: string) {
    let felderToReset: string[] = [];
    let felderToSet: string[] = [];

    if(artValue === 'einnahme') {
      this.clearAusgabeValidators();
      felderToSet = ['jahr', 'monat'];

    } else if(artValue === 'ausgabe') {
      felderToReset = ['betragEinnahme'];
      felderToSet = ['jahr', 'monat', 'kategorie', 'betragAusgabe'];

    } else {
      felderToReset = ['betragEinnahme', 'jahr', 'monat'];
      this.clearAusgabeValidators();
      felderToSet = ['notiz', 'jahr', 'monat'];
    }

    felderToReset.forEach(feld => { this.clearValidators(feld) });
    felderToSet.forEach(feld => { this.setValidatorsRequired(feld) });
  }

  private clearValidators(feld: string) {
    this.transaktionForm.get(feld)?.clearValidators();
    this.transaktionForm.get(feld)?.updateValueAndValidity();
  }

  private clearAusgabeValidators() {
    this.transaktionForm.controls.ausgabeAbschnitte.controls.forEach(abschnitt => {
      abschnitt.get('kategorie')?.clearValidators();
      abschnitt.get('betragAusgabe')?.clearValidators();
      abschnitt.get('kategorie')?.updateValueAndValidity();
      abschnitt.get('betragAusgabe')?.updateValueAndValidity();
    });
  }

  private setValidatorsRequired(feld: string) {
    this.transaktionForm.get(feld)?.setValidators([Validators.required]);
    this.transaktionForm.get(feld)?.updateValueAndValidity();
  }

  private createPayloadEinnahme(): TransaktionEinnahme {
    const payload: TransaktionEinnahme = {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      betragEinnahme: {hoehe: this.betragEinnahme, waehrung: '€'},
    }

    return payload;
  }

  private createPayloadAusgabe(abschnitt: FormGroup): TransaktionAusgabe {
    const kategorie = abschnitt.get('kategorie')?.value;
    const benutzerdefinierteKategorie = abschnitt.get('benutzerdefinierteKategorie')?.value;
    const betragAusgabe = abschnitt.get('betragAusgabe')?.value.replace(',', '.');

    const payload: TransaktionAusgabe = {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      kategorie: kategorie,
      benutzerdefinierteKategorie: benutzerdefinierteKategorie,
      betragAusgabe: {hoehe: betragAusgabe, waehrung: '€'},
    }

    return payload;
  }

  private createPayloadNotiz(): TransaktionNotiz {
    const payload: TransaktionNotiz = {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      notiz: this.notiz,
    }

    return payload;
  }

  private createAusgabeFormGroup():FormGroup {
    return new FormGroup({
      'kategorie': new FormControl('', Validators.required),
      'benutzerdefinierteKategorie': new FormControl(''),
      'jahr': new FormControl(this.jahr, Validators.required),
      'monat': new FormControl(this.monat, Validators.required),
      'betragAusgabe': new FormControl('', Validators.required),
    });
  }

  private prepareForPayloadAusgabe(): TransaktionAusgabe[] {
    const payloadAusgaben: TransaktionAusgabe[] = [];

    this.ausgabeAbschnitte.controls.forEach(abschnitt => {
      if(abschnitt.get('kategorie')?.value === 'benutzerdefiniert' && abschnitt.get('benutzerdefinierteKategorie')?.value) {
        const neueKategorie = abschnitt.get('benutzerdefinierteKategorie')?.value.toLowerCase();
        abschnitt.get('kategorie')?.setValue(neueKategorie);
      }
      const transaktionAusgabe = {...this.createPayloadAusgabe(abschnitt as FormGroup)};
      payloadAusgaben.push(transaktionAusgabe);
    });

    return payloadAusgaben;
  }

  protected readonly EingabeArt = EingabeArt;
}
