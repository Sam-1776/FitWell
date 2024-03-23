import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Diet } from '../models/diet';

@Injectable({
  providedIn: 'root'
})
export class DietService {

  dietUrl = environment.dietUrl;

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


}
