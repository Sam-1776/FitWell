import { Component, DoCheck, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
export class WorkoutsComponent implements OnInit, DoCheck {
  pageExercise!: PageExercise;
  exercises!: Exercise[];
  cardWorkout!: CardWorkout[];
  generateCard!: FormGroup;
  muscle!: string;

  showForm = false;
  dynamicForm!: FormGroup;
  accordionSets: { id: number, showForm: boolean, dynamicForm: FormGroup }[] = [];
  nextId = 0;
  accordionWorkouts: { id: number, accordionSets: { id: number, showForm: boolean, dynamicForm: FormGroup }[] }[] = [];
  nextWorkoutId = 0;


  filteredExercise!: Exercise[];
  selectExercise!: string;

  constructor(
    private exerciseSrv: ExerciseService,
    private cardSrv: CardWorkoutService,
    private fb: FormBuilder,
    private router: Router
  ) {}
  ngDoCheck(): void {
    this.muscle = this.generateCard.controls['partMuscle'].value;
  }

  ngOnInit(): void {
    this.generateCard = this.fb.group({
      name: [null, Validators.required],
      partMuscle: [null],
      exp: [null],
      type: [null],
      weight: [null],
    });
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


  createAccordionWorkout() {
    const dynamicForm = this.fb.group({
      name: ['', Validators.required]
    });

    const newWorkout = {
      id: this.nextWorkoutId++,
      accordionSets: [{ id: 0, showForm: true, dynamicForm: this.createDynamicForm() }]
    };

    this.accordionWorkouts.push(newWorkout);
  }



  createAccordionSet(workoutId: number) {
    const newSet = {
      id: this.accordionWorkouts[workoutId].accordionSets.length,
      showForm: true,
      dynamicForm: this.createDynamicForm()
    };

    this.accordionWorkouts[workoutId].accordionSets.push(newSet);
  }

  createDynamicForm() {
    return this.fb.group({
      rep: [0, Validators.required],
      weight: [0, Validators.required]
    });
  }

  submitForm(form: FormGroup) {
    if (form.valid) {
      console.log(form.value);
    }
  }
  searchByName(): void {

    this.selectExercise = this.accordionWorkouts[0].accordionSets[0].dynamicForm.controls['exerciseSelect'].value;
    console.log(this.selectExercise);
    
      // Effettua la ricerca solo se è stato selezionato un esercizio
      this.exerciseSrv.getByName(this.selectExercise).subscribe(
        (list: Exercise[]) => {
          this.exercises = list; 
          console.log(this.exercises);
          // Assegna la lista di esercizi ottenuta dall'API all'array exercises
        },
        (error) => {
          console.error('Errore durante il recupero degli esercizi', error);
        }
      );
  }

}
