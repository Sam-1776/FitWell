import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { PageExercise } from '../models/page-exercise';
import { Exercise } from '../models/exercise';

@Injectable({
  providedIn: 'root',
})
export class ExerciseService {
  private exerciseURL = environment.exerciseUrl;

  constructor(private http: HttpClient) {}

  getAllExercise() {
    return this.http.get<PageExercise>(this.exerciseURL);
  }

  getByName(str: string) {
    const params = new HttpParams().set('str', str); // Imposta il parametro di query 'str'
    return this.http.get<Exercise[]>(`${this.exerciseURL}/find`, { params });
  }
}
