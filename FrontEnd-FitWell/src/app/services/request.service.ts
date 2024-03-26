import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  cardUrl = environment.cardWorkoutUrl

  constructor(private http: HttpClient) { }

  sendRequestCreateCard(data:{
    coachId: string,
    cardId?: string,
    function: string
  }){
    return this.http.post(`${this.cardUrl}/requestMail`,data)
  }
}
