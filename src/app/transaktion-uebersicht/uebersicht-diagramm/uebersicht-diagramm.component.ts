import {AfterViewChecked, ChangeDetectorRef, Component, DestroyRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChartData, ChartOptions} from "chart.js";
import {TransaktionUebersichtTransformiert} from "../../models/transaktion.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {TransaktionService} from "../../service/transaktion.service";
import {GeldbetragNumerisch} from "../../models/geldbetrag.model";
import {BaseChartDirective} from "ng2-charts";
import {getKategorieLabel} from "../../common/select-options";
import {combineLatest, combineLatestAll} from "rxjs";

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
    private destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef,
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
    datasets: [
      {
        data: [],
        label: '',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1,
      },
    ],
  };

  ngOnInit() {

    combineLatest([this.transaktionService.kategorie$, this.transaktionService.dataSource$])
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(([kategorie, dataSource]) => {
        if(kategorie && dataSource) {
          this.clearChart();
          const kategorieLabel = getKategorieLabel(kategorie);

          dataSource.forEach((data:  TransaktionUebersichtTransformiert) => {

            this.setChart(data, kategorieLabel);
          });

          this.chart?.update();
        } else {

          this.clearChart();
        }
      })
  }

  private setChart(data:  TransaktionUebersichtTransformiert, kategorieLabel: string) {
    const ausgabeKategorie = data.ausgaben[kategorieLabel];

    if(ausgabeKategorie !== undefined && ausgabeKategorie.hoehe !== undefined) {
      const monat: string = data.monatTransaktion;

      if(kategorieLabel !== undefined && !this.barChartLabels.includes(monat)) {
        this.barChartLabels.push(monat);
        this.barChartData.datasets[0].data.push(ausgabeKategorie.hoehe);
        this.barChartData.labels?.push(monat);
        this.barChartData.datasets[0].label = kategorieLabel;
        console.log('barChartData', this.barChartData);
      }
    }
  }

  private clearChart() {
    this.barChartLabels = [];
    this.barChartData.datasets.forEach(dataset => dataset.data = []);
    this.chart?.update();
  }
}
