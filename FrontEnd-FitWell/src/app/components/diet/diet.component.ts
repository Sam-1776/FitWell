import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Diet } from 'src/app/models/diet';
import { DietService } from 'src/app/services/diet.service';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss']
})
export class DietComponent implements OnInit {

  autoDietForm!: FormGroup;
  works: string[] = [
    "Employees",
    "Administrative",
    "Manager",
    "Freelancers",
    "Technicians",
    "Student",
    "Housewives",
    "Domestic collaborators",
    "Sales personnel",
    "Tertiary workers",
    "Agriculture",
    "Livestock",
    "Forestry",
    "Fishing",
    "Manual workers",
    "Production operators",
    "Transport operators"
  ];
  MyDiet!: Diet[];

  constructor(private fb: FormBuilder, private dietSrv: DietService, private router: Router) { }

  ngOnInit(): void {
    this.autoDietForm = this.fb.group({
      numberMeals: [null],
      weight: [null],
      gender: [null],
      age: [null],
      work: [null],
      workout: [null],
      target: [null],
      user_id: [null],
      recipe_id: [null]
    })
    this.takeDiet();
  }

  takeDiet(){
    this.dietSrv.getAllDiet().subscribe((el: Diet[]) =>{
      this.MyDiet = el;
      console.log(el[1].rmr);
    })
  }


  generateDiet(){
    const data = {
      numberMeals: this.autoDietForm.controls['numberMeals'].value,
      weight: this.autoDietForm.controls['weight'].value,
      gender: this.autoDietForm.controls['gender'].value,
      age: this.autoDietForm.controls['age'].value,
      work: this.autoDietForm.controls['work'].value,
      workout: this.autoDietForm.controls['workout'].value,
      target: this.autoDietForm.controls['target'].value,
    }
    try{
      this.dietSrv.generateDiet(data).subscribe((el) =>{
        console.log(el);
      });
    }catch(err){
      console.log(err);
    }
  }

  changePage(id: string){
    this.router.navigate(['/dietDetails/',id])
  }

}
