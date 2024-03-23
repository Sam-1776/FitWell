import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  profileUrl = environment.userUrl;

  constructor(private http: HttpClient) { }

  getProfile(){return this.http.get<User>(this.profileUrl);}
}
