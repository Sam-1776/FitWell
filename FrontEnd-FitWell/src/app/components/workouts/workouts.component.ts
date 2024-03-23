import { Component, DoCheck, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthData } from 'src/app/auth/auth-data';
import { AuthService } from 'src/app/auth/auth.service';
import { CardWorkout } from 'src/app/models/card-workout';
import { Exercise } from 'src/app/models/exercise';
import { PageExercise } from 'src/app/models/page-exercise';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { ExerciseService } from 'src/app/services/exercise.service';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-workouts',
  templateUrl: './workouts.component.html',
  styleUrls: ['./workouts.component.scss'],
})
export class WorkoutsComponent implements OnInit, DoCheck {
  
  cardWorkout!: CardWorkout[];
  generateCard!: FormGroup;
  AddCardForm!: FormGroup;
  muscle!: string;
  user!: AuthData | null;

  workoutsId: string[] = [];

  panels: number[] = [];

  constructor(
    private cardSrv: CardWorkoutService,
    private fb: FormBuilder,
    private router: Router,
    private workoutSrv: WorkoutService,
    private authSrv: AuthService
  ) {}
  ngDoCheck(): void {
    this.muscle = this.generateCard.controls['partMuscle'].value;
    this.workoutSrv.workoutId.forEach(el =>{
      if (!this.workoutsId.includes(el)) {
        this.workoutsId.push(el);
      }
    })
  }

  ngOnInit(): void {
    this.generateCard = this.fb.group({
      name: [null, Validators.required],
      partMuscle: [null],
      exp: [null],
      type: [null],
      weight: [null],
    });
    this.AddCardForm = this.fb.group({
      name: [null, Validators.required],
      workoutsId: [null],
      restTimer:[null],
      user_id: [null],
      coach_id: [null]
    })

    this.getCardWorkout();
    this.getUser();
  }

  addPanel() {
    this.panels.push(this.panels.length + 1);
  }

  getCardWorkout() {
    this.cardSrv.getCardWorkout().subscribe((el: CardWorkout[]) => {
      this.cardWorkout = el;
      console.log(this.cardWorkout);
    });
  }

  generate() {
    const data = {
      name: this.generateCard.controls['name'].value,
      partMuscle: this.generateCard.controls['partMuscle'].value,
      exp: this.generateCard.controls['exp'].value,
      type: this.generateCard.controls['type'].value,
      weight: this.generateCard.controls['weight'].value,
    };
    try {
      this.cardSrv.generateAutoCard(data).subscribe();
    } catch (err) {
      console.log(err);
    }
  }

  getUser(){
    this.authSrv.user$.subscribe((user) => {
      this.user = user;
    });
  }


  savenewCard(){
    const data = {
      name: this.AddCardForm.controls['name'].value,
      workouts_id: this.workoutsId,
      restTimer: this.AddCardForm.controls['restTimer'].value,
    };
    
    /* if (this.user?.role.includes('COACH')) {
      data['setId'] = this.setId;
    } */

    console.log(data);
    try{
      this.cardSrv.saveNewCard(data).subscribe();
    }catch(err){
      console.log(err);
    }finally{
      this.workoutSrv.workoutId = [];
      this.workoutsId = [];
    }
    
  }

  changePage(id: string){
    this.router.navigate(['/details/',id]);
  }


}
