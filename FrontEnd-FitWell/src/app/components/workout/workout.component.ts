import { Component, DoCheck, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Exercise } from 'src/app/models/exercise';
import { PageExercise } from 'src/app/models/page-exercise';
import { ExerciseService } from 'src/app/services/exercise.service';
import { SetService } from 'src/app/services/set.service';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss'],
})
export class WorkoutComponent implements OnInit, DoCheck {
  newWorkoutForm!: FormGroup;
  pageExercise!: PageExercise;
  exercises!: Exercise[];
  panels: number[] = [];
  setId: string[] = [];

  constructor(
    private fb: FormBuilder,
    private exerciseSrv: ExerciseService,
    private setSrv: SetService,
    private workoutSrv: WorkoutService
  ) {}
  ngDoCheck(): void {
    this.setSrv.idString.forEach((el) => {
      if (!this.setId.includes(el)) {
        // Verifica se l'elemento non è già presente in setId
        this.setId.push(el);
      }
    });
  }

  ngOnInit(): void {
    this.newWorkoutForm = this.fb.group({
      exercise: [null],
      idExercise: [null],
    });
    this.getExercise();
  }

  getExercise() {
    this.exerciseSrv.getAllExercise().subscribe((page: PageExercise) => {
      this.exercises = page.content;
      console.log(this.exercises);
    });
  }

  saveNew() {
    const data = {
      exerciseId: this.newWorkoutForm.controls['idExercise'].value,
      setId: this.setId,
    };
    console.log(data);
    try {
      this.workoutSrv.saveWorkout(data).subscribe()
    } catch (err) {
      console.log(err);
    } finally {
      this.setSrv.idString = [];
      this.setId = [];
    }
  }

  searchByName() {
    const exercise = this.newWorkoutForm.controls['exercise'].value;
    this.exerciseSrv.getByName(exercise).subscribe((el: Exercise[]) => {
      this.exercises = el;
    });
  }

  addPanel() {
    this.panels.push(this.panels.length + 1);
  }
}
