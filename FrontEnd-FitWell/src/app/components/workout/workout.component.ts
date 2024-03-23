import { Component, DoCheck, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CardWorkout } from 'src/app/models/card-workout';
import { Exercise } from 'src/app/models/exercise';
import { PageExercise } from 'src/app/models/page-exercise';
import { Set } from 'src/app/models/set';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
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
  setList: Set[] = [];
  id!: string;
  WorkoutId!: string;
  exerciseValue!: string;
  newExercises!: Exercise;
  

  @Input() exercisesName: string[] = [];
  @Input() index!: number;
  @Input() cardByParent!: CardWorkout;

  constructor(
    private fb: FormBuilder,
    private exerciseSrv: ExerciseService,
    private setSrv: SetService,
    private workoutSrv: WorkoutService,
    private route: ActivatedRoute,
    private cardSrv: CardWorkoutService
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
    this.takeId();
    if (this.id) {
      this.exerciseValue = this.exercisesName[this.index];
      this.setValueForExercise(this.exerciseValue);
      this.WorkoutId = this.cardByParent.workouts[this.index].id;
      this.cardByParent.workouts[this.index].sets.forEach(el =>{
        this.setList.push(el);
      })
    }
  }

  getExercise() {
    this.exerciseSrv.getAllExercise().subscribe((page: PageExercise) => {
      this.exercises = page.content;
    });
  }

  saveNew() {
    const data = {
      exerciseId: this.newWorkoutForm.controls['idExercise'].value,
      setId: this.setId,
    };
    console.log(data);
    try {
      this.workoutSrv.saveWorkout(data).subscribe();
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

  addSet() {
    this.setList.push({ id: '0' , rep: 0, weight: 0 });
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  setValueForExercise(newExerciseValue: string) {
    this.newWorkoutForm.patchValue({ exercise: newExerciseValue });
  }

  async modWorkout(){
    try {
      const el: Exercise[] | undefined = await this.exerciseSrv.getByName(this.exerciseValue).toPromise();
      if(el){
        this.newExercises = el[0];
      }
      console.log(this.newExercises);
      console.log(this.setList);
  
      this.setList.forEach(element =>{
        if (element.id !== '0') {
          this.setId.push(element.id);
        }
      })
      const data = {
        exerciseId: this.newExercises.id,
        setId: this.setId,
      };
      console.log(data);
      this.workoutSrv.modWorkout(this.WorkoutId, data).subscribe();
    } catch (err) {
      console.log(err);
    }finally{
      this.setSrv.idString = [];
      this.setId = [];
    }
  }
}