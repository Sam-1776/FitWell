import {
  Component,
  OnInit,
  ViewChildren,
  QueryList,
  ElementRef,
} from '@angular/core';
import { Router } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { forkJoin } from 'rxjs';
import { CardWorkout } from 'src/app/models/card-workout';
import { Diet } from 'src/app/models/diet';
import { StatsW } from 'src/app/models/stats-w';
import { User } from 'src/app/models/user';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { DietService } from 'src/app/services/diet.service';
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
  days!: {
    name: string;
    isToday: boolean;
    borderClass: string;
    workout?: CardWorkout;
    diet: Diet;
  }[];
  today!: string;
  rep: number[] = [];
  weight: number[] = [];
  date: Date[] | string[] = [];
  cardWorkout!: CardWorkout[];
  @ViewChildren('canvas') canvasElements: QueryList<
    ElementRef<HTMLCanvasElement>
  >;
  MyDiet!: Diet[];

  constructor(
    private userSrv: UserService,
    private cardSrv: CardWorkoutService,
    private statsSrv: StatsWService,
    private dietSrv: DietService,
    private router: Router
  ) {
    this.canvasElements = new QueryList<ElementRef<HTMLCanvasElement>>();
  }

  ngOnInit(): void {
    this.getUser();
    this.fillCalendar();
  }

  getUser() {
    this.userSrv.getProfile().subscribe((utente: User) => {
      this.user = utente;
      console.log(this.user);
      this.getStatsD();
    });
  }

  fillCalendar() {
    this.getCardWorkoutAndDiet();
  }

  getCardWorkoutAndDiet() {
    forkJoin([
      this.cardSrv.getCardWorkout(),
      this.dietSrv.getAllDiet(),
    ]).subscribe(([cardWorkoutData, dietData]) => {
      this.cardWorkout = cardWorkoutData;
      console.log(this.cardWorkout);
      this.renderStatsCarousel();

      this.MyDiet = dietData;
      console.log(this.MyDiet[1].rmr);

      // Dopo aver ottenuto i dati sia del cardWorkout che della dieta, ora puoi chiamare la funzione per generare i giorni della settimana
      this.days = this.generateWeekDays();
      this.today = new Date().toLocaleDateString('en-US', { weekday: 'long' });
    });
  }

  generateWeekDays(): {
    name: string;
    isToday: boolean;
    borderClass: string;
    workout?: CardWorkout;
    diet: Diet;
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
    const days: {
        name: string;
        isToday: boolean;
        borderClass: string;
        workout?: CardWorkout;
        diet: Diet;
    }[] = [];

    for (let i = 0; i < 7; i++) {
        const index = i % 7;
        const dayName = weekdays[index];
        const isToday = dayName === today;
        const borderClass = isToday ? 'today-border' : '';
        let assignedWorkout = undefined;

        switch (this.cardWorkout.length) {
            case 3:
                if ([0, 2, 4].includes(index)) {
                    const workoutIndex = Math.floor(index / 2);
                    assignedWorkout = this.cardWorkout[workoutIndex];
                }
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                if ([0, 1, 3, 4].includes(index)) {
                    const workoutIndex = [0, 1, 3, 4].indexOf(index);
                    assignedWorkout = this.cardWorkout[workoutIndex];
                }
                break;
            // In caso di altre lunghezze di this.cardWorkout, non facciamo nulla
        }

        // Aggiungiamo il giorno alla lista
        days.push({
            name: dayName,
            isToday: isToday,
            borderClass: borderClass,
            workout: assignedWorkout,
            diet: this.MyDiet[i],
        });
    }

    return days;
}

  getStatsD() {
    new Chart('statsDiet', {
      type: 'line',
      data: {
        labels: this.user.noteBookD.stats.map((el) => el.date),
        datasets: [
          {
            label: 'Diet',
            data: this.user.noteBookD.stats.map((el) => el.weight),
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

  async renderStatsCarousel() {
    for (const workout of this.cardWorkout) {
      this.statsSrv.getStatsW(workout.id).subscribe(
        (stats: StatsW[]) => {
          if (!stats || stats.length === 0) {
            const today = new Date();
            stats = [{ id: '0', cardWorkout: workout, date: today }]; // Statistiche con valori predefiniti a 0
          }
          this.createChart(workout, stats);
        },
        (error) => {
          console.error(
            "Errore durante il recupero delle statistiche per l'allenamento con ID",
            workout.id,
            ':',
            error
          );
        }
      );
    }
  }

  createChart(workout: CardWorkout, stats: StatsW[]): void {
    const index = this.cardWorkout.indexOf(workout);
    const canvasElementsArray = this.canvasElements.toArray();
    if (canvasElementsArray && canvasElementsArray.length > index) {
      const canvasElement = canvasElementsArray[index].nativeElement;
      const ctx = canvasElement.getContext('2d');

      let totalReps = 0;
      let totalWeight = 0;
      let totalSets = 0;

      for (const workoutStat of workout.workouts) {
        for (const set of workoutStat.sets) {
          totalReps += set.rep;
          totalWeight += set.weight;
          totalSets++;
        }
      }

      const averageRep = totalReps / totalSets;
      const averageWeight = totalWeight / totalSets;

      new Chart('statsWorkout' + index, {
        type: 'line',
        data: {
          labels: stats.map((stat) => {
            const date =
              stat.date instanceof Date ? stat.date : new Date(stat.date);
            const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1)
              .toString()
              .padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
            return formattedDate;
          }),
          datasets: [
            {
              label: 'Rep',
              data: stats.map((stat) => averageRep),
              borderWidth: 1,
              borderColor: 'rgb(0, 255, 64)',
              backgroundColor: 'rgb(0, 255, 64)',
            },
            {
              label: 'Weight',
              data: stats.map((stat) => averageWeight),
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

  changePageDiet(id: string) {
    this.router.navigate(['/dietDetails/', id]);
  }
  changePageWorkout(id?: string) {
    this.router.navigate(['/details/', id]);
  }
}
