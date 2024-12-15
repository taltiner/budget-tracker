import {Component, DestroyRef, OnInit} from '@angular/core';
import {initialTransaktionUebersicht, TransaktionUebersicht} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {TRANSAKTION_JAHR} from "../common/select-options";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-transaktion-uebersicht',
  templateUrl: './transaktion-uebersicht.component.html',
  styleUrl: './transaktion-uebersicht.component.scss'
})
export class TransaktionUebersichtComponent implements OnInit {
  displayedColumns: string[] = ['monat', 'einnahmen', 'ausgaben'];
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
          const einnahmenFiltered = this.transaktionen.einnahmen.filter(transaktion => {

            return transaktion.jahrTransaktion === jahr;
          });
          const ausgabenFiltered = this.transaktionen.ausgaben.filter(transaktion => {
            const datumString = transaktion.datumTransaktion;
            const [tagAusgabe, monatAusgabe, jahrAusgabe] = datumString.split('.');

            return jahrAusgabe === jahr;
          });
          this.dataSource = [...einnahmenFiltered, ...ausgabenFiltered];
        }

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
