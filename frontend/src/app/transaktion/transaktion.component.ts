import {Component, DestroyRef, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatRadioChange} from "@angular/material/radio";
import {KATEGORIE_AUSGABE, SelectOptions, TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
import {
  EingabeArt,
  initialTransaktionUebersicht,
  TransaktionAusgabe,
  TransaktionEinnahme,
  TransaktionNotiz,
  TransaktionUebersicht
} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {ActivatedRoute, Router} from "@angular/router";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Component({
    selector: 'app-transaktion',
    templateUrl: './transaktion.component.html',
    styleUrl: './transaktion.component.scss',
    standalone: false
})

export class TransaktionComponent implements OnInit {

  constructor(private transaktionService: TransaktionService,
              private router: Router,
              private route: ActivatedRoute,
              private destroyRef: DestroyRef) {
  }

  kategorieOptions: SelectOptions[] = KATEGORIE_AUSGABE;
  jahrOptions: SelectOptions[] = TRANSAKTION_JAHR;
  monatOptions: SelectOptions[] = TRANSAKTION_MONAT;
  jahr: string = '';
  monat: string = '';
  transaktion: TransaktionUebersicht = initialTransaktionUebersicht;
  isBearbeitenAktiv: boolean = false;

  transaktionForm = new FormGroup({
    'transaktionsArt': new FormControl(EingabeArt.Einnahme, Validators.required),
    'jahr': new FormControl('', Validators.required),
    'monat': new FormControl('', Validators.required),
    'betragEinnahme': new FormControl('', Validators.required),
    'notiz': new FormControl(''),

    'ausgabeAbschnitte': new FormArray([this.createAusgabeFormGroup()]),
  });

  ngOnInit() {
    if(this.route.snapshot.routeConfig?.path === 'bearbeiten') {
      this.isBearbeitenAktiv = true;
      this.route.queryParams.subscribe(params => {
        let monat: string = params['monat'];
        const jahr: string  = params['jahr'];

        this.transaktionService.getTransaktion(monat, jahr)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe(transaktion => {
            this.transaktion = transaktion;
            this.initForm();
          })
      });
    }
    if(this.transaktionsArt) {
      this.handleValidators(this.transaktionsArt?.toString());
    }

  }

  private initForm() {
    switch(this.transaktionsArt) {

      case EingabeArt.Einnahme:
        this.initEingabeForm();
        break;

       case EingabeArt.Ausgabe:
         this.initAusgabeForm();
         break;

       case EingabeArt.Notiz:
        this.initNotizForm();
        break;

      default:
        return;
    }

  }

  private initEingabeForm() {
    let einnahmeGesamt: number = 0;
    this.transaktion.einnahmen.forEach(einnahme => {
      einnahmeGesamt += Number(einnahme.betragEinnahme.hoehe);
    });
    this.transaktionForm.controls.jahr.patchValue(this.transaktion.einnahmen[0].jahrTransaktion);
    this.transaktionForm.controls.monat.patchValue(this.transaktion.einnahmen[0].monatTransaktion);
    this.transaktionForm.controls.betragEinnahme.patchValue(einnahmeGesamt.toString());
  }

  private initAusgabeForm() {
    this.transaktionForm.controls.jahr.patchValue(this.transaktion.ausgaben[0].jahrTransaktion);
    this.transaktionForm.controls.monat.patchValue(this.transaktion.ausgaben[0].monatTransaktion);
    this.transaktion.ausgaben.forEach((ausgabe, index) => {
      const ausgabeAbschnitt = this.createAusgabeFormGroup();
      ausgabeAbschnitt.get('jahr')?.setValue(ausgabe.jahrTransaktion);
      ausgabeAbschnitt.get('monat')?.setValue(ausgabe.monatTransaktion);
      ausgabeAbschnitt.get('betragAusgabe')?.setValue(ausgabe.betragAusgabe.hoehe);
      if(ausgabe.benutzerdefinierteKategorie !== '' && ausgabe.benutzerdefinierteKategorie !== null) {
        ausgabeAbschnitt.get('kategorie')?.setValue('benutzerdefiniert');
        ausgabeAbschnitt.get('benutzerdefinierteKategorie')?.setValue(ausgabe.benutzerdefinierteKategorie);
        ausgabeAbschnitt.get('istSchulden')?.setValue(ausgabe.istSchulden);
      } else {
        ausgabeAbschnitt.get('kategorie')?.setValue(ausgabe.kategorie);
        ausgabeAbschnitt.get('benutzerdefinierteKategorie')?.setValue(ausgabe.benutzerdefinierteKategorie);
      }
      this.transaktionForm.controls.ausgabeAbschnitte.setControl(index, ausgabeAbschnitt);
    });
  }

  private initNotizForm() {
    this.transaktionForm.controls.jahr.patchValue(this.transaktion.notizen[0].jahrTransaktion);
    this.transaktionForm.controls.monat.patchValue(this.transaktion.notizen[0].monatTransaktion);
    this.transaktionForm.controls.notiz.patchValue(this.transaktion.notizen[0].notiz);
  }

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
    if(this.isBearbeitenAktiv) {
      this.initForm();
    }
    const artValue = art.value;
    this.transaktionForm.get('transaktionsArt')?.setValue(artValue);
    this.handleValidators(artValue);
  }

  onKategorieChange(index: number) {
    const ausgabeGroup = this.ausgabeAbschnitte.controls[index];
    console.log('ausgabeGroup', ausgabeGroup);
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
      this.isBearbeitenAktiv ? this.transaktionService.updateEinnahmeTransaktion(payloadEinnahme)
        : this.transaktionService.createEinnahmeTransaktion(payloadEinnahme);

    } else if(this.transaktionsArt && this.transaktionsArt === 'ausgabe') {
      const payloadAusgaben: TransaktionAusgabe[] = this.prepareForPayloadAusgabe();
      this.isBearbeitenAktiv ? this.transaktionService.updateAusgabenTransaktion(payloadAusgaben)
        : this.transaktionService.createAusgabenTransaktion(payloadAusgaben);

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

  onSchuldCheckboxChanged(checked: boolean) {

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
      abschnitt.get('monat')?.clearValidators();
      abschnitt.get('jahr')?.clearValidators();
      abschnitt.get('kategorie')?.updateValueAndValidity();
      abschnitt.get('betragAusgabe')?.updateValueAndValidity();
      abschnitt.get('monat')?.updateValueAndValidity();
      abschnitt.get('jahr')?.updateValueAndValidity();
    });
  }

  private setValidatorsRequired(feld: string) {
    this.transaktionForm.get(feld)?.setValidators([Validators.required]);
    this.transaktionForm.get(feld)?.updateValueAndValidity();
  }

  private createPayloadEinnahme(): TransaktionEinnahme {

    return {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      betragEinnahme: {hoehe: this.betragEinnahme, waehrung: '€'},
    }
  }

  private createPayloadAusgabe(abschnitt: FormGroup): TransaktionAusgabe {
    const kategorie = abschnitt.get('kategorie')?.value;
    const benutzerdefinierteKategorie = abschnitt.get('benutzerdefinierteKategorie')?.value;
    const betragAusgabe = abschnitt.get('betragAusgabe')?.value.replace(',', '.');
    const istSchulden = abschnitt.get('istSchulden')?.value;

    return {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      kategorie: kategorie,
      benutzerdefinierteKategorie: benutzerdefinierteKategorie,
      istSchulden: istSchulden,
      betragAusgabe: {hoehe: betragAusgabe, waehrung: '€'},
    }
  }

  private createPayloadNotiz(): TransaktionNotiz {

    return {
      jahrTransaktion: this.jahrTransaktion,
      monatTransaktion: this.monatTransaktion,
      transaktionsArt: this.transaktionsArt,
      notiz: this.notiz,
    }
  }

  private createAusgabeFormGroup():FormGroup {
    return new FormGroup({
      'kategorie': new FormControl('', Validators.required),
      'benutzerdefinierteKategorie': new FormControl(''),
      'jahr': new FormControl(this.jahr, Validators.required),
      'monat': new FormControl(this.monat, Validators.required),
      'betragAusgabe': new FormControl('', Validators.required),
      'istSchulden': new FormControl(false)
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
