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

  getCardCoach(){
    return this.http.get<CardWorkout[]>(`${this.cardUrl}/coach`);
  }

  getSingleCard(id: string){
    return this.http.get<CardWorkout>(`${this.cardUrl}/${id}`);
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

  saveNewCard(data:{
    name: string
    workouts_id: string[],
    restTimer: number,
    user_id?: string,
    coach_id?: string
  }){
    return this.http.post<CardWorkout>(this.cardUrl, data);
  }

  modCard(id: string, data:{
    name: string
    workouts_id: string[],
    restTimer: number,
    user_id?: string,
    coach_id?: string
  }){
    return this.http.patch<CardWorkout>(`${this.cardUrl}/${id}`,data)
  }
}
