import { getLocaleFirstDayOfWeek } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Diet } from 'src/app/models/diet';
import { FoodsIntermediate } from 'src/app/models/foods-intermediate';
import { Recipe } from 'src/app/models/recipe';
import { DietService } from 'src/app/services/diet.service';

@Component({
  selector: 'app-diet-details',
  templateUrl: './diet-details.component.html',
  styleUrls: ['./diet-details.component.scss']
})
export class DietDetailsComponent implements OnInit {
  diet!: Diet;
  recipe!: Recipe[];
  ingredients!: FoodsIntermediate[];
  id!: string;

  constructor(private dietSrv: DietService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.takeId();
    this.takeDiet();
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  takeDiet(){
    console.log(this.id);
    
    this.dietSrv.getDiet(this.id).subscribe((el) =>{
      this.diet = el;
      console.log(this.diet);
      this.takeIngredients(this.diet)
    })
  }

   takeIngredients(diet: Diet){
    console.log(diet);
    
    diet.recipes.forEach(element =>{
      this.ingredients = element.ingredients;
    })
  }

}
