import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthData } from 'src/app/auth/auth-data';
import { AuthService } from 'src/app/auth/auth.service';
import { Diet } from 'src/app/models/diet';
import { User } from 'src/app/models/user';
import { DietService } from 'src/app/services/diet.service';
import { RequestService } from 'src/app/services/request.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss'],
})
export class DietComponent implements OnInit {
  autoDietForm!: FormGroup;
  RequestDietForm!: FormGroup;
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
  MyDiet!: Diet[];
  user!: AuthData | null;
  userFoundnutritionist: User[] = [];
  nutritionist: string = 'NUTRITIONIST';

  constructor(
    private fb: FormBuilder,
    private dietSrv: DietService,
    private router: Router,
    private authSrv: AuthService,
    private userSrv: UserService,
    private requestSrv: RequestService
  ) {}

  ngOnInit(): void {
    this.getUser();
    this.autoDietForm = this.fb.group({
      numberMeals: [null],
      weight: [null],
      gender: [null],
      age: [null],
      work: [null],
      workout: [null],
      target: [null],
      user_id: [null],
      recipe_id: [null],
    });
    this.RequestDietForm = this.fb.group({
      numberMeals: [null],
      weight: [null],
      age: [null],
      work: [null],
      workout: [null],
      target: [null],
      nutritionistId: [null],
      diet_id: [null],
      function: [null]
    });
    if(this.user?.role.includes('NUTRITIONIST')){
      this.takenutritionistDiet();
    }else{
      this.takeDiet();
    }
    this.takeNutritionist()
  }

  takeDiet() {
    this.dietSrv.getAllDiet().subscribe((el: Diet[]) => {
      this.MyDiet = el;
      console.log(el[1].rmr);
    });
  }

  takenutritionistDiet() {
    this.dietSrv.getNutritionistDiet().subscribe((el: Diet[]) => {
      this.MyDiet = el;
    });
  }


  takeNutritionist(){
    this.userSrv.getAllUser().subscribe(el =>{
      el.forEach(userF => {
        if(userF.role.includes('NUTRITIONIST')){
          this.userFoundnutritionist.push(userF);
        }
      })
      console.log(el);
    })
  }

  getUser() {
    this.authSrv.user$.subscribe((user) => {
      this.user = user;
    });
  }

  generateDiet() {
    const data = {
      numberMeals: this.autoDietForm.controls['numberMeals'].value,
      weight: this.autoDietForm.controls['weight'].value,
      gender: this.autoDietForm.controls['gender'].value,
      age: this.autoDietForm.controls['age'].value,
      work: this.autoDietForm.controls['work'].value,
      workout: this.autoDietForm.controls['workout'].value,
      target: this.autoDietForm.controls['target'].value,
    };
    try {
      this.dietSrv.generateDiet(data).subscribe((el) => {
        console.log(el);
      });
    } catch (err) {
      console.log(err);
    }
  }

  changePage(id: string) {
    this.router.navigate(['/dietDetails/', id]);
  }

  sendRequest(){
     const data = {
      numberMeals: this.RequestDietForm.controls['numberMeals'].value,
      weight: this.RequestDietForm.controls['weight'].value,
      age: this.RequestDietForm.controls['age'].value,
      work: this.RequestDietForm.controls['work'].value,
      workout: this.RequestDietForm.controls['workout'].value,
      target: this.RequestDietForm.controls['target'].value,
      nutritionist_id: this.RequestDietForm.controls['nutritionistId'].value,
      function: this.RequestDietForm.controls['function'].value,
    };
    try {
      this.requestSrv.sendRequestDiet(data).subscribe(() => {
        console.log(data);
      })
    } catch (err) {
      console.log(err);
    }
  }

  
}
