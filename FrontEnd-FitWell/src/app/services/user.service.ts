import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  profileUrl = environment.userUrl;
  userUrl = environment.usersUrl;
  

  constructor(private http: HttpClient) { }

  getProfile(){return this.http.get<User>(this.profileUrl);}

  getUser(id: string){
    return this.http.get<User>(`${this.userUrl}/${id}`);
  }
  getAllUser(){
    return this.http.get<User[]>(this.userUrl)
  }
}
