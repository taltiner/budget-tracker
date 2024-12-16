import {Component, DestroyRef, OnInit} from '@angular/core';
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
  displayedColumns: string[] = ['monat', 'einnahmen', 'miete', 'strom', 'lebensmittel'];
  transaktionen: TransaktionUebersicht = initialTransaktionUebersicht;
  dataSource:any = [];
  jahrAuswahl$ = new BehaviorSubject<string | null>(null);

  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef,
              private router: Router) {}

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
          const einnahmen = this.transaktionen.einnahmen;
          const ausgabenMapped = this.mapTransaktionAusgabe();

          this.dataSource = [...einnahmen, ...ausgabenMapped];
          this.transformDataSource(jahr);
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
    })
  }


  onJahrChange(selectedJahr: string) {
    this.jahrAuswahl$.next(selectedJahr);
  }

  onNeuErfassen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }


  protected readonly jahrOptions = TRANSAKTION_JAHR;
}
