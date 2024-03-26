import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Recipe } from '../models/recipe';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  recipeUrl = environment.recipeUrl;
  RecipeId: string[] = [];

  constructor(private http: HttpClient) { }


  saveNewRecipe(data:{
    name: string,
    food_id: string[]
  }){
    return this.http.post<Recipe>(this.recipeUrl, data).pipe(
      tap(el =>{
        this.RecipeId.push(el.id);
      })
    );
  }
}
