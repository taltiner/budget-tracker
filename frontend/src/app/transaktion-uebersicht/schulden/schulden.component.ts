import {Component, OnInit} from '@angular/core';
import {ChartData, ChartOptions, ChartType} from "chart.js";

@Component({
  selector: 'app-schulden',
  templateUrl: './schulden.component.html',
  styleUrl: './schulden.component.scss',
  standalone: false
})
export class SchuldenComponent implements OnInit{

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
  public barChartLabels: string[] = ['Januar', 'Februar', 'März', 'April'];
  public barChartData: ChartData<'bar'> = {
    labels: this.barChartLabels,
    datasets: [
      {data: [65, 75, 80, 90], label: 'Test'},
      {data: [50, 85, 60, 40], label: 'Test2'},
    ],
  };

  ngOnInit(): void {

  }

}
