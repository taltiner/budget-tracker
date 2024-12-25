import {Component, DestroyRef, OnInit} from '@angular/core';
import {
  initialTransaktionUebersicht,
  TransaktionAusgabe, TransaktionEinnahme,
  TransaktionUebersicht, TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {getMonatLabel, TRANSAKTION_JAHR} from "../common/select-options";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-transaktion-uebersicht',
  templateUrl: './transaktion-uebersicht.component.html',
  styleUrl: './transaktion-uebersicht.component.scss'
})
export class TransaktionUebersichtComponent implements OnInit {
  monate: string[] = ['januar', 'februar', 'märz', 'april', 'mai', 'juni',
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
    return ['monat', 'einnahmen', ...this.kategorienSet];
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
        transaktion.monatTransaktion = getMonatLabel(transaktion.monatTransaktion);
      }
    });

    const datenGruppiert = this.gruppiereNachMonat(transaktionenGefiltert);
    this.dataSource = this.sortiereDaten(datenGruppiert);
    console.log('dataSource', this.dataSource);
  }

  private mapTransaktionAusgabe(): TransaktionAusgabe[] {
    return this.transaktionen.ausgaben.map(transaktion => {
      const datumString = transaktion.datumTransaktion;
      const [tagTransaktion, monatNummer, jahrTransaktion] = datumString.split('.');
      const monatTransaktion = this.monate[parseInt(monatNummer, 10) - 1];

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
          ausgaben: {}
        };
      }

      if (this.isEinnahme(transaktion)) {
        gruppiert[monat].einnahmen.hoehe += Number(transaktion.betragEinnahme?.hoehe || 0);
      } else {
        const kategorie = transaktion.kategorie;

        if (!gruppiert[monat].ausgaben[kategorie]) {
          gruppiert[monat].ausgaben[kategorie] = {hoehe: 0, waehrung: '€'};
        }
        gruppiert[monat].ausgaben[kategorie].hoehe += Number(transaktion.betragAusgabe?.hoehe || 0);
      }
    });

    return Object.values(gruppiert);
  }

  private sortiereDaten(unsortierteDaten: TransaktionUebersichtTransformiert[]): TransaktionUebersichtTransformiert[] {
    return unsortierteDaten.sort((a, b) => {
      const indexA = this.monate.indexOf(a.monatTransaktion);
      const indexB = this.monate.indexOf(b.monatTransaktion);

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
