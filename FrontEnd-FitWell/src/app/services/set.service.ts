import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Set } from '../models/set';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SetService {
  setUrl = environment.setUrl;
  idString: string[] = [];

  constructor(private http: HttpClient) { }

  saveSet(data:{
    rep: number,
    weight: number
  }){
    return this.http.post<Set>(`${this.setUrl}`, data).pipe(
      tap((el) =>{
        this.idString.push(el.id);
      })
    )
  }
}
