import { Component, DestroyRef, OnInit, ViewChild} from '@angular/core';
import {ChartData, ChartOptions} from "chart.js";
import {TransaktionUebersichtTransformiert} from "../../models/transaktion.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {TransaktionService} from "../../service/transaktion.service";
import {BaseChartDirective} from "ng2-charts";
import {getKategorieLabel} from "../../common/select-options";
import {combineLatest} from "rxjs";
import {diagrammFarbenModel} from "../../models/diagrammFarben.model";

@Component({
  selector: 'app-uebersicht-diagramm',
  templateUrl: './uebersicht-diagramm.component.html',
  styleUrl: './uebersicht-diagramm.component.scss',
  standalone: false
})
export class UebersichtDiagrammComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  constructor(
    private transaktionService: TransaktionService,
    private destroyRef: DestroyRef
  ) {
  }

  public barChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      x: {
        beginAtZero: true,
      },
    },
  };

  public barChartLabels: string[] = [];
  public barChartData: ChartData<'line'> = {
    labels: this.barChartLabels,
    datasets: [],
  };

  ngOnInit() {
    combineLatest([this.transaktionService.kategorie$, this.transaktionService.dataSource$])
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(([selectedKategorien, dataSource]) => {

        if(selectedKategorien && selectedKategorien.length && dataSource) {
          this.clearChart();
          selectedKategorien.forEach((kategorie, index) => {
            const kategorieLabel = getKategorieLabel(kategorie);
            const anzahlEintraege = this.barChartData.datasets.length;
            const newDataSet = createDataSet(index);

            this.barChartData.datasets.push(newDataSet);
            dataSource.forEach((data:  TransaktionUebersichtTransformiert) => {

              this.setChart(data, kategorieLabel, anzahlEintraege);
            });

            this.chart?.update();
          });

        } else {
          this.clearChart();
        }
      })
  }

  private setChart(data: TransaktionUebersichtTransformiert, kategorieLabel: string, anzahlEintraege: number) {
    const ausgabeKategorie = data.ausgaben[kategorieLabel];

    if(ausgabeKategorie !== undefined && ausgabeKategorie.hoehe !== undefined) {
      const monat: string = data.monatTransaktion;

      if(kategorieLabel !== undefined ) {
        if( !this.barChartLabels.includes(monat)) {
          this.barChartLabels.push(monat);
          this.barChartData.labels?.push(monat);
        }
        this.barChartData.datasets[anzahlEintraege].data.push(ausgabeKategorie.hoehe);
        this.barChartData.datasets[anzahlEintraege].label = kategorieLabel;
      }
    }
  }

  private clearChart() {
    this.barChartLabels = [];
    this.barChartData.datasets = [];
    this.barChartData.labels = [];
    this.chart?.update();
  }
}

export function createDataSet(index: number)  {
  return {
    data: [],
    label: '',
    backgroundColor: diagrammFarbenModel[index % diagrammFarbenModel.length],
    borderColor: diagrammFarbenModel[index % diagrammFarbenModel.length],
    borderWidth: 1,
  }
}
