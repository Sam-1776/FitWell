import { Component, DoCheck, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DietService } from 'src/app/services/diet.service';
import { RecipeService } from 'src/app/services/recipe.service';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit, DoCheck {
  newRecipe!: FormGroup;
  panels: number[] = [];
  id!: string;
  FoodsId: string[] = [];

  constructor(private fb: FormBuilder, private foodSrv: DietService, private recipeSrv: RecipeService) { }


  ngDoCheck(): void {
    this.foodSrv.foodIterID.forEach(el =>{
      if (!this.FoodsId.includes(el)) {
        this.FoodsId.push(el);
      }
    })
  }

  ngOnInit(): void {
    this.newRecipe = this.fb.group({
      name: [null]
    })
  }
  addPanel() {
    this.panels.push(this.panels.length + 1);
  }


  saveRecipe(){
    const data = {
      name: this.newRecipe.controls['name'].value,
      food_id: this.FoodsId
    }
    console.log(data);
    try{
      this.recipeSrv.saveNewRecipe(data).subscribe(el => {
        console.log(el);
      })
    }catch(err){
      console.log(err);
    }
    
  }

}
