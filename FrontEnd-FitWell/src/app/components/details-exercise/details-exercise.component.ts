import { Component, DoCheck, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart, LineElement } from 'chart.js';
import { CardWorkout } from 'src/app/models/card-workout';
import { Workout } from 'src/app/models/workout';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
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
  card!: CardWorkout;
  timerValue: number = 0; 

  constructor(
    private workoutSrv: WorkoutService,
    private route: ActivatedRoute,
    private statsSrv: StatsWService,
    private cardSrv: CardWorkoutService,
  ) {}

  ngOnInit(): void {
    this.takeId();
  }

  takeCard() {
    this.cardSrv.getSingleCard(this.cardID).subscribe((el: CardWorkout) => {
      this.card = el;
      this.timerValue = this.card.restTimer;
    });
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
      this.cardID = parm['cardId'];
      console.log(this.cardID);
      this.takeWorkout();
      this.takeCard();
    });
  }

  takeWorkout() {
    this.workoutSrv.getWorkout(this.id).subscribe((el: Workout) => {
      this.workout = el;
      console.log(el);
      this.takeStats();
    });
  }

  async takeStats() {
    this.statsSrv.getStatsW(this.cardID).subscribe((el) => {
      console.log(el);
      let avgRep = 0;
      let avgWei = 0;

      for (let i = 0; i < el.length; i++) {
        this.date.push(el[i].date);
        console.log(this.date);
        let totalRep = 0;
        let totalWeigth = 0;
        let totalSets = 0;
        for (let j = 0; j < this.workout.sets!.length; j++) {
          totalRep += this.workout.sets[j].rep;
          totalWeigth += this.workout.sets[j].weight;
          totalSets++;
        }
        avgRep = totalRep / totalSets;
        console.log(totalRep);
        console.log(totalSets);

        avgWei = totalWeigth / totalSets;
        this.rep.push(avgRep);
        this.weigth.push(avgWei);
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

  Timer(initialValue: number) {
    this.timerValue = initialValue; 
  
    const timerInterval = setInterval(() => {
      console.log(this.timerValue); 
  
      if (this.timerValue === 0) {
        clearInterval(timerInterval); 
        this.timerValue = this.card.restTimer;
      } else {
        this.timerValue--; 
      }
    }, 1000); 
  }
}
