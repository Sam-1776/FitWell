import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Diet } from '../models/diet';
import { Food } from '../models/food';

@Injectable({
  providedIn: 'root'
})
export class DietService {

  dietUrl = environment.dietUrl;
  foodUrl = environment.foodUrl;
  foodIterID: string[] = [];

  constructor(private http: HttpClient) { }


  generateDiet(data:{
    numberMeals: number,
    weight: number,
    gender: string,
    age: number,
    work: string,
    workout?: string,
    target: string,
    user_id?: string,
    recipe_id?: string[]
  }){
    return this.http.post<Diet>(`${this.dietUrl}/generate`, data);
  }

  getAllDiet(){
    return this.http.get<Diet[]>(this.dietUrl);
  }

  getDiet(id: string){
    return this.http.get<Diet>(`${this.dietUrl}/${id}`);
  }

  getNutritionistDiet(){
    return this.http.get<Diet[]>(`${this.dietUrl}/nutritionist`);
  }

  saveFood(data: {
    name: string, 
    nutrients_id: string[],
    category: string[],
    calories: number
  }){
    return this.http.post<Food>(this.foodUrl, data)
  }

  searchFood(name: string){
    const params = new HttpParams().set('name', name); // Imposta il parametro di query 'str'
    return this.http.get<Food[]>(`${this.foodUrl}/search`, { params });
  }


}
