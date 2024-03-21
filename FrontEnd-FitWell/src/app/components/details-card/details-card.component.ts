import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'chart.js';
import { CardWorkout } from 'src/app/models/card-workout';
import { CardWorkoutService } from 'src/app/services/card-workout.service';

@Component({
  selector: 'app-details-card',
  templateUrl: './details-card.component.html',
  styleUrls: ['./details-card.component.scss'],
})
export class DetailsCardComponent implements OnInit {
  ModCard!: FormGroup;
  id!: string;
  card!: CardWorkout;
  exercisesName: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private cardSrv: CardWorkoutService,
    private router: Router,
    private fb: FormBuilder
  ) {}

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
}
