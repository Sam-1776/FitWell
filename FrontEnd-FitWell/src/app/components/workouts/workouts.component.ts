import { Component, OnInit } from '@angular/core';
import { CardWorkout } from 'src/app/models/card-workout';
import { Exercise } from 'src/app/models/exercise';
import { PageExercise } from 'src/app/models/page-exercise';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { ExerciseService } from 'src/app/services/exercise.service';

@Component({
  selector: 'app-workouts',
  templateUrl: './workouts.component.html',
  styleUrls: ['./workouts.component.scss'],
})
export class WorkoutsComponent implements OnInit {
  pageExercise!: PageExercise;
  exercises!: Exercise[];
  cardWorkout!: CardWorkout[];

  constructor(private exerciseSrv: ExerciseService, private cardSrv: CardWorkoutService) {}

  ngOnInit(): void {
    this.getExercise();
    this.getCardWorkout();
  }

  getExercise() {
    this.exerciseSrv.getAllExercise().subscribe((page: PageExercise) => {
      this.exercises = page.content;
      console.log(this.exercises);
    });
  }

  getCardWorkout() {
    this.cardSrv.getCardWorkout().subscribe((el: CardWorkout[]) =>{
      this.cardWorkout = el;
      console.log(this.cardWorkout);
      
    })
  }
}
