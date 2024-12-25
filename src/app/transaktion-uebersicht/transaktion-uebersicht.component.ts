import {Component, DestroyRef, OnInit} from '@angular/core';
import {
  initialTransaktionUebersicht,
  TransaktionAusgabe, TransaktionEinnahme,
  TransaktionUebersicht, TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {getKategorieLabel, getMonatLabel, getMonatValue, TRANSAKTION_JAHR} from "../common/select-options";
import {BehaviorSubject} from "rxjs";
import {GeldbetragNumerisch} from "../models/geldbetrag.model";

@Component({
  selector: 'app-transaktion-uebersicht',
  templateUrl: './transaktion-uebersicht.component.html',
  styleUrl: './transaktion-uebersicht.component.scss'
})
export class TransaktionUebersichtComponent implements OnInit {
  monate: string[] = ['gesamt', 'januar', 'februar', 'märz', 'april', 'mai', 'juni',
    'juli', 'august', 'september', 'oktober', 'november', 'dezember'];
  transaktionen: TransaktionUebersicht = initialTransaktionUebersicht;
  dataSource: TransaktionUebersichtTransformiert[] = [];
  jahrAuswahl$ = new BehaviorSubject<string | null>(null);
  kategorienSet: string[] = [];
  isDataProcessing: boolean = false;

  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef,
              private router: Router) {}

  ngOnInit(): void {
    this.loadAllTransaktionen();
    this.handleJahrSelektion();
  }

  get displayedColumns() {
    return ['monat', 'einnahmen', ...this.kategorienSet, 'gesamtausgaben', 'saldo'];
  }

  onJahrChange(selectedJahr: string) {
    this.jahrAuswahl$.next(selectedJahr);
  }

  onNeuErfassen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }

  private loadAllTransaktionen(): void {
    this.transaktionService.getAllTransaktionen()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(alleTransaktionen => {
        this.transaktionen = alleTransaktionen;
      });
  }

  private handleJahrSelektion(): void {
    this.jahrAuswahl$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(jahr => {
        if(jahr !== null) {
          this.isDataProcessing = true;
          this.transformDataSource(jahr);
          this.setAusgabeKategorien();
          this.isDataProcessing = false;
        }
      })
  }

  private transformDataSource(jahr: string): void {
    const einnahmen = this.transaktionen.einnahmen;
    const ausgabenMapped = this.mapTransaktionAusgabe();
    const transaktionenUntransformiert = [...einnahmen, ...ausgabenMapped];

    const transaktionenGefiltert = transaktionenUntransformiert.filter((transaktion: TransaktionEinnahme | TransaktionAusgabe) => {
      return transaktion.jahrTransaktion === jahr;
    });
    transaktionenGefiltert.forEach((transaktion: TransaktionEinnahme | TransaktionAusgabe) => {
      if(transaktion.monatTransaktion !== undefined) {
        console.log('monatTransaktion', transaktion.monatTransaktion)
        transaktion.monatTransaktion = getMonatLabel(transaktion.monatTransaktion.toLowerCase());
      }
    });
    console.log('transaktionenGefiltert', transaktionenGefiltert)

    const datenGruppiert = this.gruppiereNachMonat(transaktionenGefiltert);
    console.log('datenGruppiert', datenGruppiert)
    this.dataSource = this.sortiereDaten(datenGruppiert);
    console.log('dataSource', this.dataSource);
  }

  private mapTransaktionAusgabe(): TransaktionAusgabe[] {
    return this.transaktionen.ausgaben.map(transaktion => {
      const datumString = transaktion.datumTransaktion;
      const [tagTransaktion, monatNummer, jahrTransaktion] = datumString.split('.');
      const monatTransaktion = this.monate[parseInt(monatNummer, 10)];

      return {...transaktion, tagTransaktion, monatTransaktion, jahrTransaktion};
    });
  }

  private gruppiereNachMonat(transaktionen: (TransaktionEinnahme | TransaktionAusgabe)[]): TransaktionUebersichtTransformiert[] {
    const gruppiert: { [monat: string]: TransaktionUebersichtTransformiert  } = {};

    transaktionen.forEach((transaktion) => {
      let monat: string = transaktion.monatTransaktion ?? '';

      if (!gruppiert[monat]) {
        gruppiert[monat] = {
          monatTransaktion: monat,
          einnahmen: {hoehe: 0, waehrung: '€'},
          ausgaben: {},
          gesamtausgaben: {hoehe: 0, waehrung: '€'},
          saldo: {hoehe: 0, waehrung: '€'},
        };
      }

      if (!gruppiert['gesamt']) {
        gruppiert['gesamt'] = {
          monatTransaktion: getMonatLabel('gesamt'),
          einnahmen: {hoehe: 0, waehrung: '€'},
          ausgaben: {},
          gesamtausgaben: {hoehe: 0, waehrung: '€'},
          saldo: {hoehe: 0, waehrung: '€'},
        };
      }

      if (this.isEinnahme(transaktion)) {
        gruppiert[monat].einnahmen.hoehe += Number(transaktion.betragEinnahme?.hoehe || 0);
        gruppiert[monat].saldo.hoehe += Number(transaktion.betragEinnahme?.hoehe || 0);
      } else {
        let kategorieFormatted = '';
        if(transaktion.benutzerdefinierteKategorie === '') {
           kategorieFormatted = getKategorieLabel(transaktion.kategorie);
        } else {
          kategorieFormatted = transaktion.kategorie.charAt(0).toUpperCase() + transaktion.kategorie.slice(1).toLowerCase();
        }

        if (!gruppiert[monat].ausgaben[kategorieFormatted]) {
          gruppiert[monat].ausgaben[kategorieFormatted] = {hoehe: 0, waehrung: '€'};
        }
        gruppiert[monat].ausgaben[kategorieFormatted].hoehe += Number(transaktion.betragAusgabe?.hoehe || 0);
        gruppiert[monat].gesamtausgaben.hoehe += Number(transaktion.betragAusgabe?.hoehe || 0);
        gruppiert[monat].saldo.hoehe -= Number(transaktion.betragAusgabe?.hoehe || 0);
      }

      gruppiert['gesamt'].einnahmen.hoehe += gruppiert[monat].einnahmen.hoehe;
      gruppiert['gesamt'].gesamtausgaben.hoehe += gruppiert[monat].gesamtausgaben.hoehe;
      gruppiert['gesamt'].saldo.hoehe += gruppiert[monat].saldo.hoehe;

    });
    console.log('gruppiert ', gruppiert)
    return Object.values(gruppiert);
  }

  private sortiereDaten(unsortierteDaten: TransaktionUebersichtTransformiert[]): TransaktionUebersichtTransformiert[] {
    return unsortierteDaten.sort((a, b) => {
      const indexA = this.monate.indexOf(getMonatValue(a.monatTransaktion));
      const indexB = this.monate.indexOf(getMonatValue(b.monatTransaktion));

      return indexB - indexA;
    })
  }

  private setAusgabeKategorien(): void {
    const kategorienSet = new Set<string>();
    this.dataSource.forEach(transaktion => {
      Object.keys(transaktion.ausgaben).forEach(kategorie => {
        kategorienSet.add(kategorie);
      });
    });

    this.kategorienSet = Array.from(kategorienSet);
  }

  isSpinnerActive(): boolean {
    return this.isDataProcessing;
  }

  private isEinnahme(transaktion: TransaktionEinnahme | TransaktionAusgabe): transaktion is TransaktionEinnahme {
    return transaktion.tranksaktionsArt === 'einnahme';
  }


  protected readonly jahrOptions = TRANSAKTION_JAHR;
}
