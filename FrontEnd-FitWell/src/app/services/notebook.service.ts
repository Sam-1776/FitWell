import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { NotebookW } from '../models/notebook-w';
import { NotebookD } from '../models/notebook-d';

@Injectable({
  providedIn: 'root'
})
export class NotebookService {

  noteBooksUrl = environment.noteBookseUrl;

  constructor(private http: HttpClient) { }

  saveStatsOnNotebookW(id: string, data:{
    cardWorkoutId: string,
  }){
    return this.http.patch<NotebookW>(`${this.noteBooksUrl}/gym/${id}`, data);
  }

  saveStatsOnNoteBookD(id: string, data:{
    dietId: string,
    weught: number
  }){
    return this.http.patch<NotebookD>(`${this.noteBooksUrl}/diet/${id}`, data);
  }
}
