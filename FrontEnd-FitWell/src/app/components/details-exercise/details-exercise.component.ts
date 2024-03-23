import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Chart } from 'chart.js';
import { Workout } from 'src/app/models/workout';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-details-exercise',
  templateUrl: './details-exercise.component.html',
  styleUrls: ['./details-exercise.component.scss']
})
export class DetailsExerciseComponent implements OnInit {
  workout!: Workout;
  id!: string;

  constructor(private workoutSrv: WorkoutService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.takeId();
    this.takeWorkout();
    this.getStatsW()
  }


  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  takeWorkout(){
    this.workoutSrv.getWorkout(this.id).subscribe((el: Workout) =>{
      this.workout = el;
    })
  }


  getStatsW() {
    new Chart('statsWorkout', {
      type: 'line',
      data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8'],
        datasets: [
          {
            label: 'Rep',
            data: [0],
            borderWidth: 1,
            borderColor: "rgb(0, 255, 64)",
            backgroundColor: "rgb(0, 255, 64)"
          },
          {
            label: 'Weight',
            data: [0],
            borderWidth: 1,
            borderColor: "#f1f8fa",
            backgroundColor: "#f1f8fa"
          }
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
