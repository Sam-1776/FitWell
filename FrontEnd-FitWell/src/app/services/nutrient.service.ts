import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Nutrient } from '../models/nutrient';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NutrientService {

  nutrientUrl = environment.nutrientUrl;
  nutrientId: string[] =[];
  constructor(private http: HttpClient) { }

  saveNutrient(data:{
    name: string,
    amount: number
  }){
    return this.http.post<Nutrient>(this.nutrientUrl, data).pipe(
      tap(el =>{
        this.nutrientId.push(el.id);
      })
    );
  }
}
