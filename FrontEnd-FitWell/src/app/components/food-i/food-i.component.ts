import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Food } from 'src/app/models/food';
import { DietService } from 'src/app/services/diet.service';

@Component({
  selector: 'app-food-i',
  templateUrl: './food-i.component.html',
  styleUrls: ['./food-i.component.scss']
})
export class FoodIComponent implements OnInit {
  foodsFound: Food[] = [];
  FoodIForm!: FormGroup;
  id!: string;

  constructor(private foodSrv: DietService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.FoodIForm = this.fb.group({
      amount: [null],
      inputName: [null],
      foodId:[null]
    })
  }

  searchFoodName(){
    this.foodSrv.searchFood(this.FoodIForm.controls['inputName'].value).subscribe((el: Food[]) => {
      this.foodsFound = el;
    })
  }
  

  saveFoodI(){
    const data = {
      food_id: this.FoodIForm.controls['foodId'].value,
      amount: this.FoodIForm.controls['amount'].value
    }
    console.log(data);
    try{
      this.foodSrv.saveFoodInter(data).subscribe(el =>{
        console.log(el);
        
      })
    }catch(err){
      console.log(err);
    }
  }

}
