import {Component, DestroyRef, OnInit} from '@angular/core';
import {
  initialTransaktionUebersicht,
  TransaktionAusgabe,
  TransaktionEinnahme,
  TransaktionUebersicht,
  TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {
  getKategorieLabel,
  getMonatLabel,
  getMonatValue,
  KATEGORIE_AUSGABE,
  TRANSAKTION_JAHR
} from "../common/select-options";
import {Darstellung} from "../models/darstellung.model";
import _default from "chart.js/dist/core/core.interaction";
import dataset = _default.modes.dataset;

@Component({
    selector: 'app-transaktion-uebersicht',
    templateUrl: './transaktion-uebersicht.component.html',
    styleUrl: './transaktion-uebersicht.component.scss',
    standalone: false
})
export class TransaktionUebersichtComponent implements OnInit {
  monate: string[] = ['gesamt', 'januar', 'februar', 'märz', 'april', 'mai', 'juni',
    'juli', 'august', 'september', 'oktober', 'november', 'dezember'];
  transaktionen: TransaktionUebersicht = initialTransaktionUebersicht;
  dataSource: TransaktionUebersichtTransformiert[] = [];
  kategorienSet: string[] = [];
  isDataProcessing: boolean = false;
  darstellung: Darstellung = Darstellung.Tabellarisch;
  kategorie: string = '';

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
    this.transaktionService.jahrAuswahl$.next(selectedJahr);
  }

  onDarstellungChange(darstellung: Darstellung) {
    this.darstellung = darstellung;
  }

  onKategorieChange(kategorie: string) {
    console.log('kategorie changed', kategorie)
    this.kategorie = kategorie;
    this.transaktionService.kategorie$.next(kategorie);
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
    this.transaktionService.jahrAuswahl$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(jahr => {
        if(jahr !== null) {
          this.isDataProcessing = true;
          this.transformDataSource(jahr);
          this.setAusgabeKategorien();
          this.transaktionService.dataSource$.next(this.dataSource);
          console.log('datasource changed')
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
        transaktion.monatTransaktion = getMonatLabel(transaktion.monatTransaktion.toLowerCase());
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
      const monatTransaktion = this.monate[parseInt(monatNummer, 10)];

      return {...transaktion, tagTransaktion, monatTransaktion, jahrTransaktion};
    });
  }

  private gruppiereNachMonat(transaktionen: (TransaktionEinnahme | TransaktionAusgabe)[]): TransaktionUebersichtTransformiert[] {
    const gruppiert: { [monat: string]: TransaktionUebersichtTransformiert } = {};

    const gesamtKey = 'gesamt';
    gruppiert[gesamtKey] = {
      monatTransaktion: getMonatLabel(gesamtKey),
      einnahmen: { hoehe: 0, waehrung: '€' },
      ausgaben: {},
      gesamtausgaben: { hoehe: 0, waehrung: '€' },
      saldo: { hoehe: 0, waehrung: '€' },
    };

    transaktionen.forEach((transaktion) => {
      const monat = transaktion.monatTransaktion ?? '';
      const isEinnahme = this.isEinnahme(transaktion);
      const betragHoehe = Number(isEinnahme ? transaktion.betragEinnahme?.hoehe : transaktion.betragAusgabe?.hoehe || 0);

      if (!gruppiert[monat]) {
        gruppiert[monat] = {
          monatTransaktion: monat,
          einnahmen: { hoehe: 0, waehrung: '€' },
          ausgaben: {},
          gesamtausgaben: { hoehe: 0, waehrung: '€' },
          saldo: { hoehe: 0, waehrung: '€' },
        };
      }

      const aktuellerMonat = gruppiert[monat];
      const gesamtEintrag = gruppiert[gesamtKey];

      if (isEinnahme) {
        aktuellerMonat.einnahmen.hoehe += betragHoehe;
        aktuellerMonat.saldo.hoehe += betragHoehe;
        gesamtEintrag.einnahmen.hoehe += betragHoehe;
        gesamtEintrag.saldo.hoehe += betragHoehe;
      } else {
        const kategorie = transaktion.benutzerdefinierteKategorie
          ? transaktion.kategorie.charAt(0).toUpperCase() + transaktion.kategorie.slice(1).toLowerCase()
          : getKategorieLabel(transaktion.kategorie);

        if (!aktuellerMonat.ausgaben[kategorie]) {
          aktuellerMonat.ausgaben[kategorie] = { hoehe: 0, waehrung: '€' };
        }

        aktuellerMonat.ausgaben[kategorie].hoehe += betragHoehe;
        aktuellerMonat.gesamtausgaben.hoehe += betragHoehe;
        aktuellerMonat.saldo.hoehe -= betragHoehe;

        gesamtEintrag.gesamtausgaben.hoehe += betragHoehe;
        gesamtEintrag.saldo.hoehe -= betragHoehe;
      }
    });

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
  protected readonly Darstellung = Darstellung;
  protected readonly kategorieOptions = KATEGORIE_AUSGABE;
}
