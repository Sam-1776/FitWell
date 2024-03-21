import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Workout } from '../models/workout';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WorkoutService {
  workoutUrl = environment.workoutUrl;
  workoutId: string[] = [];

  constructor(private http: HttpClient) { }

  saveWorkout(data:{
    exerciseId: string,
    setId: string[]
  }){
    return this.http.post<Workout>(`${this.workoutUrl}`, data).pipe(
      tap(el =>{
        this.workoutId.push(el.id);
      })
    );
  }
}
