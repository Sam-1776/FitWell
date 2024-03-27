import {
  Component,
  OnInit,
  ViewChildren,
  QueryList,
  ElementRef,
} from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { CardWorkout } from 'src/app/models/card-workout';
import { StatsW } from 'src/app/models/stats-w';
import { User } from 'src/app/models/user';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { StatsWService } from 'src/app/services/stats-w.service';
import { UserService } from 'src/app/services/user.service';

Chart.register(...registerables);

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  user!: User;
  days!: { name: string; isToday: boolean; borderClass: string }[];
  today!: string;
  rep: number[] = [];
  weight: number[] = [];
  date: Date[] = [];
  cardWorkout!: CardWorkout[];
  @ViewChildren('canvas') canvasElements:
    | QueryList<ElementRef<HTMLCanvasElement>>
;

  constructor(
    private userSrv: UserService,
    private cardSrv: CardWorkoutService,
    private statsSrv: StatsWService
  ) {
    this.canvasElements = new QueryList<ElementRef<HTMLCanvasElement>>();
  }

  ngOnInit(): void {
    this.getUser();
    this.getStatsD();
    this.days = this.generateWeekDays();
    this.today = new Date().toLocaleDateString('en-US', { weekday: 'long' });
  }

  getUser() {
    this.userSrv.getProfile().subscribe((utente: User) => {
      this.user = utente;
      console.log(this.user);
      this.getCardWorkout();
    });
  }

  getStatsD() {
    new Chart('statsDiet', {
      type: 'line',
      data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8'],
        datasets: [
          {
            label: 'Diet',
            data: [0],
            borderWidth: 1,
            borderColor: 'rgb(0, 255, 64)',
            backgroundColor: 'rgb(0, 255, 64)',
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }

  generateWeekDays(): {
    name: string;
    isToday: boolean;
    borderClass: string;
  }[] {
    const weekdays = [
      'Monday',
      'Tuesday',
      'Wednesday',
      'Thursday',
      'Friday',
      'Saturday',
      'Sunday',
    ];
    const today = new Date().toLocaleDateString('en-US', { weekday: 'long' });
    const days: { name: string; isToday: boolean; borderClass: string }[] = [];

    for (let i = 0; i < 7; i++) {
      const index = i % 7;
      const dayName = weekdays[index];
      const isToday = dayName === today;
      const borderClass = isToday ? 'today-border' : ''; // Aggiunta della classe per i bordi del giorno odierno

      days.push({ name: dayName, isToday: isToday, borderClass: borderClass });
    }

    return days;
  }

  async renderStatsCarousel() {
    for (const workout of this.cardWorkout) {
      this.statsSrv.getStatsW(workout.id).subscribe(
        (stats: StatsW[]) => {
          if (!stats || stats.length === 0) {
            const today = new Date().toLocaleDateString();
            stats = [{ id: '0', cardWorkout: workout, date: new Date(today) }]; // Statistiche con valori predefiniti a 0
        }
          this.createChart(workout, stats);
        },
        (error) => {
          console.error("Errore durante il recupero delle statistiche per l'allenamento con ID", workout.id, ':', error);
        }
      );
    }
  }

  createChart(workout: CardWorkout, stats: StatsW[]): void {
    // Ottieni l'indice del workout nell'array cardWorkout
    const index = this.cardWorkout.indexOf(workout);
    const canvasElementsArray = this.canvasElements.toArray();
    if (canvasElementsArray && canvasElementsArray.length > index) {
      // Ottieni il canvas corrispondente
      const canvasElement = canvasElementsArray[index].nativeElement;
      const ctx = canvasElement.getContext('2d');
  
      let totalReps = 0;
      let totalWeight = 0;
      let totalSets = 0;
  
      // Calcola la media delle ripetizioni e del peso dal workout corrente
      for (const workoutStat of workout.workouts) {
        for (const set of workoutStat.sets) {
          totalReps += set.rep;
          totalWeight += set.weight;
          totalSets++;
        }
      }
  
      const averageRep = totalReps / totalSets;
      const averageWeight = totalWeight / totalSets;
  
      // Inizializza un nuovo grafico per il canvas corrente
      new Chart('statsWorkout' + index, {
        type: 'line',
        data: {
          labels: stats.map(stat => stat.date),
          datasets: [
            {
              label: 'Rep',
              data: stats.map(stat => averageRep),
              borderWidth: 1,
              borderColor: 'rgb(0, 255, 64)',
              backgroundColor: 'rgb(0, 255, 64)',
            },
            {
              label: 'Weight',
              data: stats.map(stat => averageWeight),
              borderWidth: 1,
              borderColor: '#f1f8fa',
              backgroundColor: '#f1f8fa',
            },
          ],
        },
        options: {
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        },
      });
    } else {
      console.error("Elemento canvas non trovato per l'indice:", index);
    }
  }

  getCardWorkout() {
    this.cardSrv.getCardWorkout().subscribe((el: CardWorkout[]) => {
      this.cardWorkout = el;
      console.log(this.cardWorkout);
      this.renderStatsCarousel();
    });
  }
}
