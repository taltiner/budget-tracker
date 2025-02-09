import {Component, DestroyRef, OnInit} from '@angular/core';
import {
  EingabeArt,
  initialTransaktionUebersicht,
  TransaktionAusgabe,
  TransaktionEinnahme,
  TransaktionNotiz,
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
import {MatCheckboxChange} from "@angular/material/checkbox";
import {ToggleType} from "../models/toggle.model";

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
  selectedKategorie: string[] = [];
  selectedJahr: string = '';
  isBackendRunning: boolean = false;
  toggles: ToggleType[] = [];
  selectedEintraege: string[] = [];

  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef,
              private router: Router) {}

  ngOnInit(): void {
    this.checkBackendStatus();
    this.loadAllTransaktionen();
    this.getToggles();
  }

  get displayedColumns() {
    let columns: string[] = ['monat', 'einnahmen', ...this.kategorienSet, 'gesamtausgaben', 'saldo', 'notiz'];
    if(this.toggles.includes(ToggleType.CRUD_TRANSAKTION)) {
      columns.unshift('auswahl')
    }
    return columns;
  }

  onJahrChange(selectedJahr: string) {
    this.selectedJahr = selectedJahr;
    this.transaktionService.jahrAuswahl$.next(selectedJahr);
    this.selectedEintraege = [];
  }

  onDarstellungChange(darstellung: Darstellung) {
    this.darstellung = darstellung;
  }

  onKategorieChange(event: MatCheckboxChange, kategorie: string) {
    if(event.checked) {
      if (!this.selectedKategorie.includes(kategorie)) {
        this.selectedKategorie.push(kategorie);
      }
    } else {
      this.selectedKategorie = this.selectedKategorie.filter(item => item !== kategorie);
    }
    this.transaktionService.kategorieSubject.next([...this.selectedKategorie]);
  }

  onNeuErfassen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }

  onBearbeiten() {
    this.router.navigate(['/bearbeiten'], {
      queryParams: {monat: this.selectedEintraege[0].toLowerCase(), jahr: this.selectedJahr}
    });
  }

  onLoeschen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }

  onFilter() {
    if(this.isBackendRunning) {
      this.transaktionService.filterTransaktion(this.selectedJahr)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(filteredTransaktionen => {
          this.dataSource = filteredTransaktionen;
          this.setAusgabeKategorien();
          this.transaktionService.dataSourceSubject.next([...this.dataSource]);
        });
    } else {
      this.handleJahrSelektion();
    }

  }

  onEintragChecked(isChecked: boolean, transaktion: TransaktionUebersichtTransformiert) {
    const monat = transaktion.monatTransaktion;
    if(!this.selectedEintraege.includes(monat)) {
      this.selectedEintraege.push(monat);
    } else {
      this.selectedEintraege = this.selectedEintraege.filter(monatEintrag => monatEintrag !== monat);
    }
  }

  private checkBackendStatus() {
    this.transaktionService.backendRunning$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(isBackendRunning => this.isBackendRunning = isBackendRunning);
  }

  private loadAllTransaktionen(): void {
    this.transaktionService.getAllTransaktionen()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(alleTransaktionen => {
        this.transaktionen = alleTransaktionen;
      });
  }

  private getToggles() {
    this.transaktionService.getToggles()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(toggles => this.toggles = toggles);
  }

  private handleJahrSelektion(): void {
    this.transaktionService.jahrAuswahl$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(jahr => {
        if(jahr !== null) {
          this.isDataProcessing = true;
          this.transformDataSource(jahr);
          this.setAusgabeKategorien();
          this.transaktionService.dataSourceSubject.next([...this.dataSource]);
          this.isDataProcessing = false;
        }
      })
  }

  private transformDataSource(jahr: string): void {
    const einnahmen = this.transaktionen.einnahmen;
    const ausgaben = this.transaktionen.ausgaben;
    const notizen = this.transaktionen.notizen;
    const transaktionenUntransformiert = [...einnahmen, ...ausgaben, ...notizen];
    const transaktionenGefiltert = transaktionenUntransformiert.filter((transaktion: TransaktionEinnahme | TransaktionAusgabe | TransaktionNotiz) => {
      return transaktion.jahrTransaktion === jahr;
    });

    transaktionenGefiltert.forEach((transaktion: TransaktionEinnahme | TransaktionAusgabe | TransaktionNotiz) => {
      if(transaktion.monatTransaktion !== undefined) {
        transaktion.monatTransaktion = getMonatLabel(transaktion.monatTransaktion.toLowerCase());
      }
    });
    const datenGruppiert = this.gruppiereUndTransformiereNachMonat(transaktionenGefiltert);
    this.dataSource = this.sortiereDaten(datenGruppiert);
  }

  private gruppiereUndTransformiereNachMonat(transaktionen: (TransaktionEinnahme | TransaktionAusgabe | TransaktionNotiz)[]): TransaktionUebersichtTransformiert[] {
    const gruppiert: { [monat: string]: TransaktionUebersichtTransformiert } = {};
    const gesamtKey = 'gesamt';

    gruppiert[gesamtKey] = {
      monatTransaktion: getMonatLabel(gesamtKey),
      einnahmen: { hoehe: 0, waehrung: '€' },
      ausgaben: {},
      gesamtausgaben: { hoehe: 0, waehrung: '€' },
      saldo: { hoehe: 0, waehrung: '€' }
    };

    transaktionen.forEach((transaktion) => {
      const monat = transaktion.monatTransaktion ?? '';
      const isEinnahme = this.isEinnahme(transaktion);
      const isNotiz = this.isNotiz(transaktion);
      let betragHoehe = 0;

      if (!gruppiert[monat]) {
        gruppiert[monat] = {
          monatTransaktion: monat,
          einnahmen: { hoehe: 0, waehrung: '€' },
          ausgaben: {},
          gesamtausgaben: { hoehe: 0, waehrung: '€' },
          saldo: { hoehe: 0, waehrung: '€' },
          notiz: ''
        };
      }

      const aktuellerMonat = gruppiert[monat];
      const gesamtEintrag = gruppiert[gesamtKey];

      if (isEinnahme) {
        betragHoehe = Number(transaktion.betragEinnahme?.hoehe);
        aktuellerMonat.einnahmen.hoehe = this.rundeNachZweiKommastellen(aktuellerMonat.einnahmen.hoehe + betragHoehe);
        aktuellerMonat.saldo.hoehe = this.rundeNachZweiKommastellen(aktuellerMonat.saldo.hoehe + betragHoehe);
        gesamtEintrag.einnahmen.hoehe = this.rundeNachZweiKommastellen(gesamtEintrag.einnahmen.hoehe + betragHoehe);
        gesamtEintrag.saldo.hoehe = this.rundeNachZweiKommastellen(gesamtEintrag.saldo.hoehe + betragHoehe);
      } else if (!isEinnahme && !isNotiz) {
        betragHoehe = Number(transaktion.betragAusgabe?.hoehe);

        const kategorie = transaktion.benutzerdefinierteKategorie
          ? transaktion.kategorie.charAt(0).toUpperCase() + transaktion.kategorie.slice(1).toLowerCase()
          : getKategorieLabel(transaktion.kategorie);

        if (!aktuellerMonat.ausgaben[kategorie]) {
          aktuellerMonat.ausgaben[kategorie] = { hoehe: 0, waehrung: '€' };
        }
        if(!gesamtEintrag.ausgaben[kategorie]) {
          gesamtEintrag.ausgaben[kategorie] = { hoehe: 0, waehrung: '€' };
        }

        aktuellerMonat.ausgaben[kategorie].hoehe = this.rundeNachZweiKommastellen(aktuellerMonat.ausgaben[kategorie].hoehe + betragHoehe);
        aktuellerMonat.gesamtausgaben.hoehe = this.rundeNachZweiKommastellen(aktuellerMonat.gesamtausgaben.hoehe + betragHoehe);
        aktuellerMonat.saldo.hoehe = this.rundeNachZweiKommastellen(aktuellerMonat.saldo.hoehe - betragHoehe);

        gesamtEintrag.gesamtausgaben.hoehe = this.rundeNachZweiKommastellen(gesamtEintrag.gesamtausgaben.hoehe + betragHoehe);
        gesamtEintrag.ausgaben[kategorie].hoehe = this.rundeNachZweiKommastellen(gesamtEintrag.ausgaben[kategorie].hoehe + betragHoehe);
      } else if(isNotiz) {

        aktuellerMonat.notiz = transaktion.notiz;
      }
    });

    gruppiert[gesamtKey].saldo.hoehe = this.rundeNachZweiKommastellen(gruppiert[gesamtKey].einnahmen.hoehe - gruppiert[gesamtKey].gesamtausgaben.hoehe);

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

  private isEinnahme(transaktion: TransaktionEinnahme | TransaktionAusgabe | TransaktionNotiz): transaktion is TransaktionEinnahme {
    return transaktion.transaktionsArt === EingabeArt.Einnahme;
  }

  private isNotiz(transaktion: TransaktionEinnahme | TransaktionAusgabe | TransaktionNotiz): transaktion is TransaktionNotiz {
    return transaktion.transaktionsArt === EingabeArt.Notiz;
  }

  private rundeNachZweiKommastellen(betrag: number): number {
    return Math.ceil(betrag * 100) / 100;
  }

  protected readonly jahrOptions = TRANSAKTION_JAHR;
  protected readonly Darstellung = Darstellung;
  protected readonly kategorieOptions = KATEGORIE_AUSGABE;
  protected readonly ToggleType = ToggleType;
}
