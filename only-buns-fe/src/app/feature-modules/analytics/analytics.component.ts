import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { AnalyticsService } from './analytics.service';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css'
})
export class AnalyticsComponent implements OnInit {
  // Statistika
  weeklyPosts = 0;
  weeklyComments = 0;
  monthlyPosts = 0;
  monthlyComments = 0;
  yearlyPosts = 0;
  yearlyComments = 0;

  // Podaci za radijalni grafikon
  userActivityData = [0, 0, 0];

  private radialChart: Chart | null = null;

  constructor(private analyticsService: AnalyticsService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    // Preuzimanje statistike objava i komentara
    this.analyticsService.getPostsAndCommentsStats().subscribe((data) => {
      this.weeklyPosts = data.weeklyPosts;
      this.weeklyComments = data.weeklyComments;
      this.monthlyPosts = data.monthlyPosts;
      this.monthlyComments = data.monthlyComments;
      this.yearlyPosts = data.yearlyPosts;
      this.yearlyComments = data.yearlyComments;
    });

    // Preuzimanje statistike korisničke aktivnosti
    this.analyticsService.getUserActivityStats().subscribe((data) => {
      this.userActivityData = [
        data.madePosts,
        data.madeOnlyComments,
        data.noActivity,
      ];
      this.renderRadialChart();
    });
  }

  renderRadialChart(): void {
    if (this.userActivityData.every((value) => value === 0)) {
      console.warn('No data to display for the radial chart.');
      return;
    }

    // Logika za prikaz grafikona
    const radialData = {
      labels: ['Made Posts', 'Made Only Comments', 'No Activity'],
      datasets: [
        {
          label: 'User Activity',
          data: this.userActivityData,
          backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726'],
        },
      ],
    };

    const ctx = document.getElementById('radialChart') as HTMLCanvasElement;
    if (this.radialChart) {
      this.radialChart.destroy();
    }
    this.radialChart = new Chart(ctx, {
      type: 'doughnut',
      data: radialData,
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          tooltip: {
            callbacks: {
              label: function (tooltipItem) {
                const label = tooltipItem.label || '';
                const value = tooltipItem.raw; // Raw vrednost podataka
                return `${label}: ${value}%`; // Prikaz u procentima
              },
            },
          },
        },
      },
    });

  }

}
