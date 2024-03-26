import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { StatsW } from '../models/stats-w';

@Injectable({
  providedIn: 'root'
})
export class StatsWService {
  statsUrl = environment.statsUrl;

  constructor(private http: HttpClient) { }

  getStatsW(id: string){
    return this.http.get<StatsW[]>(`${this.statsUrl}/gym/${id}`);
  }

  getStatsD(){
   
  }
}
