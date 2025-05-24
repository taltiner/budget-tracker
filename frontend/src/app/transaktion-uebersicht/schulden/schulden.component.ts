import {Component, DestroyRef, Input} from '@angular/core';
import {ChartData, ChartOptions, ChartType} from "chart.js";
import {TransaktionService} from "../../service/transaktion.service";
import {TransaktionAusgabe, TransaktionUebersicht,} from "../../models/transaktion.model";
import {Schulden} from "../../models/schulden.model";
import {take} from "rxjs";

@Component({
  selector: 'app-schulden',
  templateUrl: './schulden.component.html',
  styleUrl: './schulden.component.scss',
  standalone: false
})
export class SchuldenComponent {
  private _data: TransaktionAusgabe[] = [];
  private schuldenMap = new Map<string, number>;
  private schulden: Schulden[] = [];
  private isSchuldenGeladen: boolean = false;
  private zinssatz: number = 6.01;

  @Input()
  set transaktionen(value: TransaktionUebersicht){
    this._data = value.ausgaben.filter(ausgabe => ausgabe.istSchulden);
    console.log('data', this._data);
    this.getSchulden();

    if(this.isSchuldenGeladen) {
      this.erzeugeSchuldenMap();
      this.erzeugeSchuldenGraphikData();
    }
  }


  constructor(private transaktionService: TransaktionService,
              private destroyRef: DestroyRef) {}

  public barChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    indexAxis: 'y',
    scales: {
      x:{
        title: {
          display: true,
          text: '€'
        },
        beginAtZero: true,
      },
      y: {
        beginAtZero: true,
      },
    },
    plugins: {
      legend: {
        display: true,
      }
    }
  };

  public barChartType: ChartType = 'bar';
  public barChartLabels: string[] = [];
  public barChartData: ChartData<'bar'> = {
    labels: this.barChartLabels,
    datasets: [
    ],
  };

  private getSchulden() {
    this.transaktionService.getSchulden()
      .pipe(take(1))
      .subscribe(
      schulden => {
        console.log('schulden', schulden);
        this.schulden = schulden;
        this.isSchuldenGeladen = true;
      }
    )
  }

  private erzeugeSchuldenMap() {
    this._data.forEach(data => {
      let kategorie = data.kategorie;
      if(this.schuldenMap.has(kategorie)) {
        let betrag: number = Number(data.betragAusgabe.hoehe) + this.schuldenMap.get(kategorie)!.valueOf();
        if(kategorie === 'kfw') {
          betrag = betrag * (100 - this.zinssatz) / 100;
          this.schuldenMap.set(kategorie, Math.round(betrag * 100)/100);
        } else {
          this.schuldenMap.set(kategorie, Math.round(betrag * 100)/100);
        }
      } else {
        let betrag = Math.round(Number(data.betragAusgabe.hoehe) * 100) / 100;
        this.schuldenMap.set(kategorie, betrag);
      }
    });
    console.log('schuldenMap', this.schuldenMap);
  }

  private erzeugeSchuldenGraphikData() {
    const labels: string[] = [];
    const urspruenglichBetrag: number[] = [];
    const bezahlterBetrag: number[] = [];

    this.schuldenMap.forEach((value, key) => {
      const betrag = this.getUrsrpuenglichenBetrag(key);
      const label = key.charAt(0).toUpperCase() + key.slice(1);

      labels.push(label);
      urspruenglichBetrag.push(betrag);
      bezahlterBetrag.push(value);
    });

    this.barChartLabels = labels;
    this.barChartData = {
      labels: labels,
      datasets: [
        {
          data: urspruenglichBetrag,
          label: 'Ursprünglicher Betrag',
          backgroundColor: 'rgba(255, 99, 132, 0.6)'
        },
        {
          data: bezahlterBetrag,
          label: 'Bezahlter Betrag',
          backgroundColor: 'rgba(54, 162, 235, 0.6)'
        }
      ]
    };

    if(this.schulden.length > this.schuldenMap.size) {
      const nichtGetilgteSchulden = this.schulden.filter(schuld =>
       !this.schuldenMap.has(schuld.schuldenBezeichnung.toLowerCase())
      );

      nichtGetilgteSchulden.forEach(schuld => {
        const betrag = Math.round(Number(schuld.schuldenHoehe.hoehe) * 100) / 100;
        this.barChartLabels.push(schuld.schuldenBezeichnung);
        this.barChartData.datasets[0].data.push(betrag);
      })
    }

  }

  private getUrsrpuenglichenBetrag(bezeichnung: string): number {
    let schuldenHoehe: number = 0;
    this.schulden.forEach((schuld) => {

      if (schuld.schuldenBezeichnung.toLowerCase() === bezeichnung.toLowerCase()) {

        schuldenHoehe =  Math.round(Number(schuld.schuldenHoehe.hoehe) * 100) / 100;
      }
    });

    return schuldenHoehe;
  }

}
