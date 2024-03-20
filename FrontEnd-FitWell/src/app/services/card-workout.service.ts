import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CardWorkout } from '../models/card-workout';

@Injectable({
  providedIn: 'root'
})
export class CardWorkoutService {

  cardUrl = environment.cardWorkoutUrl;

  constructor(private http: HttpClient) { }

  getCardWorkout(){
    return this.http.get<CardWorkout[]>(this.cardUrl);
  }

  generateAutoCard(data:{
    name: string;
    partMuscle: string;
    exp: string;
    type: string;
    weight: number;
  }){
    return this.http.post<CardWorkout>(`${this.cardUrl}/generate`, data);
  }
}
