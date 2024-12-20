import {Component} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import { MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions, TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
import {TransaktionAusgabe, TransaktionEinnahme} from "../models/transaktion.model";
import {Store} from "@ngrx/store";
import {TransaktionState} from "../state/transaktion.state";
import {TransaktionService} from "../service/transaktion.service";
import {Router} from "@angular/router";
import {DatePipe} from "@angular/common";

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
    'tranksaktionsArt': new FormControl('', Validators.required),
    'jahr': new FormControl('', Validators.required),
    'monat': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),

    'ausgabeAbschnitte': new FormArray([this.createAusgabeFormGroup()]),
  })

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
/*  get datumTransaktion(): string {
    return this.transaktionForm.controls.datumTransaktion.value ?? '';
  }
  get kategorie(): string {
    return this.transaktionForm.controls.kategorie.value ?? '';
  }
  get betragAusgabe(): string {
    return this.transaktionForm.controls.betragAusgabe.value ?? '';
  }*/
  get ausgabeAbschnitte() {
    return this.transaktionForm.get('ausgabeAbschnitte') as FormArray ?? [];
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
      //const transaktionAusgabe = this.createPayloadAusgabe();
      //this.transaktionService.createTransaktion(transaktionAusgabe);

    }
  }

  onAbbrechen() {
    this.router.navigate(['/'], {
      queryParams: {} });
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
      betragEinnahme: {hoehe: this.betragEinnahme, waehrung: '€'},
      notiz: this.notiz
    }

    return payload;
  }

/*  private createPayloadAusgabe(): TransaktionAusgabe {
    const datePipe = new DatePipe('en-US');
    const formattedDate = datePipe.transform(this.datumTransaktion, 'dd.MM.yyyy');
    const payload: TransaktionAusgabe = {
      datumTransaktion: formattedDate || '',
      tranksaktionsArt: this.transaktionsArt,
      kategorie: this.kategorie,
      betragAusgabe: {hoehe: this.betragAusgabe, waehrung: '€'}
    }

    return payload;
  }*/

  private createAusgabeFormGroup():FormGroup {
    return new FormGroup({
      'kategorie': new FormControl('', Validators.required),
      'datumTransaktion': new FormControl('', Validators.required),
      'betragAusgabe': new FormControl('', Validators.required),
    });
  }
}
