import { Component, DoCheck, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Diet } from 'src/app/models/diet';
import { User } from 'src/app/models/user';
import { DietService } from 'src/app/services/diet.service';
import { NutrientService } from 'src/app/services/nutrient.service';
import { RecipeService } from 'src/app/services/recipe.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-diet-nutritionist',
  templateUrl: './diet-nutritionist.component.html',
  styleUrls: ['./diet-nutritionist.component.scss'],
})
export class DietNutritionistComponent implements OnInit, DoCheck {
  MyDiet!: Diet[];
  panels: number[] = [];
  panelsDiet: number[] = [];
  nutrientList: string[] = [];
  foodForm!: FormGroup;
  dietForm!: FormGroup;
  categories = [
    { value: 'cereals and tubers', label: 'Cereals and Tubers' },
    { value: 'fruits and vegetables', label: 'Fruits and Vegetables' },
    { value: 'milk and dairy products', label: 'Milk and Dairy Products' },
    { value: 'meat', label: 'Meat' },
    { value: 'fish', label: 'Fish' },
    { value: 'eggs', label: 'Eggs' },
    { value: 'seasoning_fats', label: 'Seasoning and Fats' }
  ];
  userFound?: User;
  works: string[] = [
    'Employees',
    'Administrative',
    'Manager',
    'Freelancers',
    'Technicians',
    'Student',
    'Housewives',
    'Domestic collaborators',
    'Sales personnel',
    'Tertiary workers',
    'Agriculture',
    'Livestock',
    'Forestry',
    'Fishing',
    'Manual workers',
    'Production operators',
    'Transport operators',
  ];

  recipeId: string[] = [];

  constructor(
    private dietSrv: DietService,
    private router: Router,
    private nutrientSrv: NutrientService,
    private fb: FormBuilder,
    private userSrv: UserService,
    private recipeSrv: RecipeService
  ) {}
  ngDoCheck(): void {
    this.nutrientSrv.nutrientId.forEach((el) => {
      if (!this.nutrientList.includes(el)) {
        this.nutrientList.push(el);
      }
    });
    this.recipeSrv.RecipeId.forEach((el) => {
      if (!this.recipeId.includes(el)) {
        this.recipeId.push(el);
      }
    });
  }

  ngOnInit(): void {
    this.foodForm = this.fb.group({
      name: [null],
      category: this.fb.array([]),
      calories: [null]
    })
    this.dietForm = this.fb.group({
      numberMeals: [null],
      weight: [null],
      gender: [null],
      age: [null],
      work: [null],
      workout: [null],
      target: [null],
      user_id: [null],
      inputUser: [null],
      recipe_id: [null],
    })
    this.takeDiet();
  }

  takeDiet() {
    this.dietSrv.getAllDiet().subscribe((el: Diet[]) => {
      this.MyDiet = el;
      console.log(el);
    });
  }

  changePage(id: string) {
    this.router.navigate(['/dietDetails/', id]);
  }
  addPanel() {
    this.panels.push(this.panels.length + 1);
  }
  addPanelD() {
    this.panelsDiet.push(this.panelsDiet.length + 1);
    this.dietSrv.foodIterID = []
  }

  saveFood(){
    const data = {
      name: this.foodForm.controls['name'].value, 
      nutrients_id: this.nutrientList,
      category: this.categoriesFormArray.value,
      calories: this.foodForm.controls['calories'].value,
    }
    console.log(data);
    try{
      this.dietSrv.saveFood(data).subscribe(el =>{
        console.log(el);
      })
    }catch(err){
      console.log(err);
    }
    
  }

  get categoriesFormArray() {
    return this.foodForm.controls['category'] as FormArray;
  }

  onCheckboxChange(event: any) {
    const formArray: FormArray = this.foodForm.get('category') as FormArray;

    if (event.target.checked) {
      // Aggiungi un nuovo FormControl al FormArray quando una checkbox è selezionata
      formArray.push(new FormControl(event.target.value));
    }else {
      // Rimuovi il FormControl corrispondente dal FormArray quando una checkbox è deselezionata
      const index = formArray.controls.findIndex(x => x.value === event.target.value);
      formArray.removeAt(index);
    }
  }

  searchById() {
    const id = this.dietForm.controls['inputUser'].value;
     this.userSrv.getUser(id).subscribe((el: User) => {
      this.userFound = el;
    }); 
  }

  saveDiet(){
    const data = {
      numberMeals: this.dietForm.controls['numberMeals'].value,
      weight: this.dietForm.controls['weight'].value,
      gender: this.dietForm.controls['gender'].value,
      age: this.dietForm.controls['age'].value,
      work: this.dietForm.controls['work'].value,
      workout: this.dietForm.controls['workout'].value,
      target: this.dietForm.controls['target'].value,
      user_id: this.dietForm.controls['user_id'].value,
      recipe_id: this.recipeId,
    };
    try{
      this.dietSrv.saveDiet(data).subscribe(el =>{
        console.log(el);
      })
    }catch(err){
      console.log(err);
    }
  }
}
