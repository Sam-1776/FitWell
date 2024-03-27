import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  cardUrl = environment.cardWorkoutUrl
  dietUrl = environment.dietUrl;

  constructor(private http: HttpClient) { }

  sendRequestCreateCard(data:{
    coachId: string,
    cardId?: string,
    function: string
  }){
    return this.http.post(`${this.cardUrl}/requestMail`,data)
  }

  sendRequestDiet(data:{
     nutritionist_id: string,
     diet_id?: string,
     function: string,
     weight: number,
     age: number,
     numberMeals: number,
     work: string,
     workout?: string,
     target: string
  }){
    return this.http.post(`${this.dietUrl}/requestMail`,data)
  }
}
