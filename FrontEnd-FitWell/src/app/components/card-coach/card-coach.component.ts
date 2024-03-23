import { Component, DoCheck, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CardWorkout } from 'src/app/models/card-workout';
import { User } from 'src/app/models/user';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { UserService } from 'src/app/services/user.service';
import { WorkoutService } from 'src/app/services/workout.service';

@Component({
  selector: 'app-card-coach',
  templateUrl: './card-coach.component.html',
  styleUrls: ['./card-coach.component.scss'],
})
export class CardCoachComponent implements OnInit, DoCheck {
  cardWorkout!: CardWorkout[];
  panels: number[] = [];
  workoutsId: string[] = [];

  NewCardForm!: FormGroup;
  userFound: User | undefined;

  constructor(
    private cardSrv: CardWorkoutService,
    private router: Router,
    private userSrv: UserService,
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
    this.getCardWorkout();
    this.NewCardForm = this.fb.group({
      name: [null, Validators.required],
      workoutsId: [null],
      restTimer: [null],
      user_id: [null],
      inputUser: [null],
      coach_id: [null],
    });
    console.log(this.workoutSrv.workoutId);
    
  }

  getCardWorkout() {
    this.cardSrv.getCardWorkout().subscribe((el: CardWorkout[]) => {
      this.cardWorkout = el;
      console.log(this.cardWorkout);
    });
  }

  changePage(id: string) {
    this.router.navigate(['/details/', id]);
  }

  addPanel() {
    this.panels.push(this.panels.length + 1);
  }

  savenewCard() {
    const data = {
      name: this.NewCardForm.controls['name'].value,
      workouts_id: this.workoutsId,
      restTimer: this.NewCardForm.controls['restTimer'].value,
      user_id: this.NewCardForm.controls['user_id'].value,
    };

    console.log(data);
    try{
      this.cardSrv.saveNewCard(data).subscribe(el =>{
        console.log(el);
      });
    }catch(err){
      console.log(err);
    }
    
  }

  searchById() {
    const id = this.NewCardForm.controls['inputUser'].value;
    this.userSrv.getUser(id).subscribe((el: User) => {
      this.userFound = el;
    });
  }
}
