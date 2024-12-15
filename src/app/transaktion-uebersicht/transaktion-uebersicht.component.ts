import {Component, DestroyRef, OnInit} from '@angular/core';
import {initialTransaktionUebersicht, Transaktion, TransaktionUebersicht} from "../models/transaktion.model";
import {TransaktionService} from "../service/transaktion.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {Router} from "@angular/router";
import {TRANSAKTION_JAHR, TRANSAKTION_MONAT} from "../common/select-options";
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
  jahr$ = new BehaviorSubject<string | null>(null);

  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef,
              private router: Router) {}

  ngOnInit(): void {
    this.transaktionService.getAllTransaktionen()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(alleTransaktionen => {
        this.transaktionen = alleTransaktionen;
        this.dataSource = [...this.transaktionen.einnahmen, ...this.transaktionen.ausgaben];
        console.log('dataSource ', this.dataSource);
      });

    this.jahr$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(jahr => {

    })
  }

  onJahrChange(selectedJahr: string) {
    this.jahr$.next(selectedJahr);
  }

  onNeuErfassen() {
    this.router.navigate(['/neu'], {
      queryParams: {}
    });
  }


  protected readonly jahrOptions = TRANSAKTION_JAHR;
}
