import { Component, DoCheck, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'chart.js';
import { CardWorkout } from 'src/app/models/card-workout';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-details-card',
  templateUrl: './details-card.component.html',
  styleUrls: ['./details-card.component.scss'],
})
export class DetailsCardComponent implements OnInit, DoCheck {
  ModCard!: FormGroup;
  id!: string;
  card!: CardWorkout;
  exercisesName: string[] = [];
  workoutsId: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private cardSrv: CardWorkoutService,
    private router: Router,
    private fb: FormBuilder,
    private workoutSrv: WorkoutService
  ) {}


  ngDoCheck(): void {
    this.workoutSrv.workoutId.forEach(el =>{
      if (!this.workoutsId.includes(el)) {
        this.workoutsId.push(el);
      }
    })
  }

  ngOnInit(): void {
    this.takeId();
    console.log(this.id);
    this.takeCard();
    this.ModCard = this.fb.group({
      name: [null, Validators.required],
      workoutsId: [null],
      restTimer: [null],
      user_id: [null],
      coach_id: [null],
    });
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  takeCard() {
    this.cardSrv.getSingleCard(this.id).subscribe((el: CardWorkout) => {
      this.card = el;
      el.workouts.forEach(element =>{
        this.exercisesName.push(element.exercise.name);
      })
      this.setValueForm();
    });
  }

  exerciseDetails(id: string) {
    this.router.navigate(['/exDetails/', id]);
  }

  setValueForm(){
    this.ModCard.patchValue({
      name: this.card.name,
      restTimer: this.card.restTimer
    })
  }

  addWorkout(){
    this.card.workouts.push({id: '0', exercise: this.card.workouts[0].exercise, sets: []})
  }

  async modWorkout(){
    this.card.workouts.forEach(el =>{
      if(el.id !== '0'){
        this.workoutsId.push(el.id);
      }
    })
    const data = {
      name: this.ModCard.controls['name'].value,
      workouts_id: this.workoutsId,
      restTimer: this.ModCard.controls['restTimer'].value,
    };
    
    /* if (this.user?.role.includes('COACH')) {
      data['setId'] = this.setId;
    } */

    console.log(data);
    try{
      this.cardSrv.modCard(this.id, data).subscribe(() =>{
        this.workoutSrv.workoutId = [];
      this.workoutsId = [];
      });
    }catch(err){
      console.log(err);
    }
  }
}
