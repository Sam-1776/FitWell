import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {
  newRecipe!: FormGroup;
  panels: number[] = [];
  id!: string;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.newRecipe = this.fb.group({
      name: [null]
    })
  }
  addPanel() {
    this.panels.push(this.panels.length + 1);
  }


  saveRecipe(){}

}
