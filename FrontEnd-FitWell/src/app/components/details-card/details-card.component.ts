import { Component, DoCheck, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'chart.js';
import { CardWorkout } from 'src/app/models/card-workout';
import { User } from 'src/app/models/user';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { NotebookService } from 'src/app/services/notebook.service';
import { UserService } from 'src/app/services/user.service';
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
  user!: User;
  timerValue: number = 0; 


  constructor(
    private route: ActivatedRoute,
    private cardSrv: CardWorkoutService,
    private router: Router,
    private fb: FormBuilder,
    private workoutSrv: WorkoutService,
    private userSrv: UserService,
    private noteBookSrv: NotebookService
  ) {}


  ngDoCheck(): void {
    this.workoutSrv.workoutId.forEach(el =>{
      if (!this.workoutsId.includes(el)) {
        this.workoutsId.push(el);
      }
    })
  }

  ngOnInit(): void {
    this.getUser();
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

  getUser() {
    this.userSrv.getProfile().subscribe((utente: User) => {
      this.user = utente;
      console.log(this.user);
    });
  }


  takeCard() {
    this.cardSrv.getSingleCard(this.id).subscribe((el: CardWorkout) => {
      this.card = el;
      this.timerValue = el.restTimer;
      el.workouts.forEach(element =>{
        this.exercisesName.push(element.exercise.name);
      })
      this.setValueForm();
    });
  }

  exerciseDetails(cardId: string, id: string) {
    console.log(cardId);
    this.router.navigate(['/exDetails/',cardId, id]);
    this.workoutSrv.cardWorkoutId = this.card.id;
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

  saveWorkout(){
    const data = {
      cardWorkoutId: this.id
    }
    console.log(data);
    console.log(this.user);
    try{
      this.noteBookSrv.saveStatsOnNotebookW(this.user.noteBookW.id, data).subscribe(el => {
        console.log(el);
      })
    }catch(err){
      console.log(err);
    }
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
