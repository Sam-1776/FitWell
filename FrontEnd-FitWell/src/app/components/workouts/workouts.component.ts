import { Component, DoCheck, Input, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthData } from 'src/app/auth/auth-data';
import { AuthService } from 'src/app/auth/auth.service';
import { CardWorkout } from 'src/app/models/card-workout';
import { Exercise } from 'src/app/models/exercise';
import { PageExercise } from 'src/app/models/page-exercise';
import { User } from 'src/app/models/user';
import { CardWorkoutService } from 'src/app/services/card-workout.service';
import { ExerciseService } from 'src/app/services/exercise.service';
import { RequestService } from 'src/app/services/request.service';
import { UserService } from 'src/app/services/user.service';
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

  userFoundCoach: User[] = [];
  coach: string = 'COACH';

  RequestCard!: FormGroup;

  constructor(
    private cardSrv: CardWorkoutService,
    private fb: FormBuilder,
    private router: Router,
    private workoutSrv: WorkoutService,
    private authSrv: AuthService,
    private userSrv: UserService,
    private requestSrv: RequestService
  ) {}
  ngDoCheck(): void {
    this.muscle = this.generateCard.controls['partMuscle'].value;
    this.workoutSrv.workoutId.forEach((el) => {
      if (!this.workoutsId.includes(el)) {
        this.workoutsId.push(el);
      }
    });
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
      restTimer: [null],
      user_id: [null],
      coach_id: [null],
    });
    this.RequestCard = this.fb.group({
      coachId: [null],
      function: [null],
    });
    this.getUser();
    if (this.user?.role.includes('COACH')) {
      this.getCardCoach();
    } else {
      this.getCardWorkout();
    }
    this.takeCoach();
  }

  addPanel() {
    this.panels.push(this.panels.length + 1);
  }
  takeCoach() {
    this.userSrv.getAllUser().subscribe((el) => {
      el.forEach((userF) => {
        if (userF.role.includes('COACH')) {
          this.userFoundCoach.push(userF);
        }
      });
      console.log(el);
    });
  }

  getCardWorkout() {
    this.cardSrv.getCardWorkout().subscribe((el: CardWorkout[]) => {
      this.cardWorkout = el;
      console.log(this.cardWorkout);
    });
  }

  getCardCoach() {
    this.cardSrv.getCardCoach().subscribe((el: CardWorkout[]) => {
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
      this.cardSrv.generateAutoCard(data).subscribe(() => {
        this.router.navigate(['/workout']);
      });
    } catch (err) {
      console.log(err);
    }
  }

  getUser() {
    this.authSrv.user$.subscribe((user) => {
      this.user = user;
    });
  }

  savenewCard() {
    const data = {
      name: this.AddCardForm.controls['name'].value,
      workouts_id: this.workoutsId,
      restTimer: this.AddCardForm.controls['restTimer'].value,
    };

    console.log(data);
    try {
      this.cardSrv.saveNewCard(data).subscribe(() => {
        this.router.navigate(['/workout']);
      });
    } catch (err) {
      console.log(err);
    } finally {
      this.workoutSrv.workoutId = [];
      this.workoutsId = [];
    }
  }

  changePage(id: string) {
    this.router.navigate(['/details/', id]);
  }

  sandRequest() {
    const data = {
      coachId: this.RequestCard.controls['coachId'].value,
      function: this.RequestCard.controls['function'].value,
    };
    try {
      this.requestSrv.sendRequestCreateCard(data).subscribe(() => {
        this.router.navigate(['/workout']);
      });
    } catch (err) {
      console.log(err);
    }
  }

  deleteCard(id: string) {
    try {
      this.cardSrv.deleteCard(id).subscribe(() => {
        this.router.navigate(['/workout']);
      });
    } catch (error) {
      console.log(error);
    }
  }
}
