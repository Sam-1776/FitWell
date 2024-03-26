import { Component, DoCheck, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, LineElement } from 'chart.js';
import { Workout } from 'src/app/models/workout';
import { StatsWService } from 'src/app/services/stats-w.service';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-details-exercise',
  templateUrl: './details-exercise.component.html',
  styleUrls: ['./details-exercise.component.scss'],
})
export class DetailsExerciseComponent implements OnInit {
  workout!: Workout;
  id!: string;
  cardID!: string;
  rep: number[] = [];
  weigth: number[] = [];
  date: Date[] = [];  

  constructor(
    private workoutSrv: WorkoutService,
    private route: ActivatedRoute,
    private statsSrv: StatsWService
  ) {}

  ngOnInit(): void {
    this.cardID = this.workoutSrv.cardWorkoutId;
    this.takeId();
    this.takeWorkout();
    this.takeStats();
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  takeWorkout() {
    this.workoutSrv.getWorkout(this.id).subscribe((el: Workout) => {
      this.workout = el;
    });
  }

  async takeStats() {
    this.statsSrv.getStatsW(this.cardID).subscribe((el) => {
      console.log(el);

      for (let i = 0; i < el.length; i++) {
        this.date.push(el[i].date)
        console.log(this.date);
        for (let j = 0; j < this.workout.sets.length; j++) {
          this.rep.push(this.workout.sets[j].rep);
          this.weigth.push(this.workout.sets[j].weight);
        }
        console.log(this.rep);
        console.log(this.weigth);
      }

      this.getStatsW(this.date, this.rep, this.weigth);
    });
  }

  getStatsW(date: Date[], rep: number[], weight: number[]) {
    new Chart('statsWorkout', {
      type: 'line',
      data: {
        labels: date,
        datasets: [
          {
            label: 'Rep',
            data: rep,
            borderWidth: 1,
            borderColor: 'rgb(0, 255, 64)',
            backgroundColor: 'rgb(0, 255, 64)',
          },
          {
            label: 'Weight',
            data: weight,
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
  }
}
