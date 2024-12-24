import {ChangeDetectorRef, Component, DestroyRef, OnInit} from '@angular/core';
import {
  initialTransaktionUebersicht,
  TransaktionAusgabe,
  TransaktionEinnahme,
  TransaktionUebersicht
} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {getMonatLabel, TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-transaktion-uebersicht',
  templateUrl: './transaktion-uebersicht.component.html',
  styleUrl: './transaktion-uebersicht.component.scss'
})
export class TransaktionUebersichtComponent implements OnInit {
  monate = ['januar', 'februar', 'märz', 'april', 'mai', 'juni',
    'juli', 'august', 'september', 'oktober', 'november', 'dezember'];
  transaktionen: TransaktionUebersicht = initialTransaktionUebersicht;
  dataSource:any = [];
  jahrAuswahl$ = new BehaviorSubject<string | null>(null);
  kategorienSet: string[] = [];
  //displayedColumns: string[] = ['monat', 'einnahmen', ...this.kategorienSet];
  get displayedColumns() {
    return ['monat', 'einnahmen', ...this.kategorienSet];
  }
  isLoading: boolean = false;

  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef,
              private router: Router,
              private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.transaktionService.getAllTransaktionen()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(alleTransaktionen => {
        this.transaktionen = alleTransaktionen;
        console.log('transaktionen', this.transaktionen);
      });

    this.jahrAuswahl$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(jahr => {
        if(jahr !== null) {
          this.isLoading = true;
          const einnahmen = this.transaktionen.einnahmen;
          const ausgabenMapped = this.mapTransaktionAusgabe();

          this.dataSource = [...einnahmen, ...ausgabenMapped];
          this.transformDataSource(jahr);
          this.getAusgabeKategorien();
          console.log('kategorienSet', this.kategorienSet)
          this.cdr.detectChanges();
          this.isLoading = false;
        }
    })
  }

  mapTransaktionAusgabe(): TransaktionAusgabe[] {
    return this.transaktionen.ausgaben.map(transaktion => {
      const datumString = transaktion.datumTransaktion;
      const [tagTransaktion, monatNummer, jahrTransaktion] = datumString.split('.');
      const monatTransaktion = this.monate[parseInt(monatNummer, 10) - 1];

      return {...transaktion, tagTransaktion, monatTransaktion, jahrTransaktion};
    });
  }

  transformDataSource(jahr: string) {
    this.dataSource = this.dataSource.filter((transaktion: any) => {
      return transaktion.jahrTransaktion === jahr
    });
    this.dataSource.forEach((data: any) => {
      data.monatTransaktion = getMonatLabel(data.monatTransaktion);
    });
    console.log('dataSource MonatLabel ', this.dataSource);
    this.dataSource = this.gruppiereNachMonat(this.dataSource);
    console.log('dataSource gruppiert', this.dataSource);
    // @ts-ignore
    this.dataSource = this.sortiereDaten(this.dataSource);
    console.log('dataSource sortiert ', this.dataSource);
/*    this.getAusgabeKategorien();
    console.log('kategorienSet', this.kategorienSet);*/
  }


  onJahrChange(selectedJahr: string) {
    this.jahrAuswahl$.next(selectedJahr);
  }

  onNeuErfassen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }

  gruppiereNachMonat(daten: any[]) {
    const gruppiert: { [monat: string]: any } = {};

    daten.forEach((transaktion) => {
      const monat = transaktion.monatTransaktion;

      if (!gruppiert[monat]) {
        gruppiert[monat] = {
          monatTransaktion: monat,
          einnahmen: {hoehe: 0, waehrung: '€'},
          ausgaben: {}
        };
      }

      if (transaktion.tranksaktionsArt === 'einnahme') {
        gruppiert[monat].einnahmen.hoehe += Number(transaktion.betragEinnahme?.hoehe || 0);
      }

      if (transaktion.tranksaktionsArt === 'ausgabe') {
        const kategorie = transaktion.kategorie;

        if (!gruppiert[monat].ausgaben[kategorie]) {
          gruppiert[monat].ausgaben[kategorie] = {hoehe: 0, waehrung: '€'};
        }
        gruppiert[monat].ausgaben[kategorie].hoehe += Number(transaktion.betragAusgabe?.hoehe || 0);
      }
    });

    return Object.values(gruppiert);
  }

  sortiereDaten(unsortierteDaten: any) {
    // @ts-ignore
    return unsortierteDaten.sort((a, b) => {
      const indexA = this.monate.indexOf(a.monatTransaktion);
      const indexB = this.monate.indexOf(b.monatTransaktion);
      return indexB - indexA;
    })
  }

  getAusgabeKategorien() {
    const kategorienSet = new Set<string>();
    // @ts-ignore
    this.dataSource.forEach(transaktion => {
      Object.keys(transaktion.ausgaben).forEach(kategorie => {
        kategorienSet.add(kategorie);
      });
    });

    this.kategorienSet = Array.from(kategorienSet);
  }


  protected readonly jahrOptions = TRANSAKTION_JAHR;
}
