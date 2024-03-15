import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthData } from './auth-data';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { BehaviorSubject, throwError, tap, catchError } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
Router;
environment;
HttpClient;

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  jwt = new JwtHelperService();
  url = environment.authUrl;
  private authSubj = new BehaviorSubject<null | AuthData>(null);
  user$ = this.authSubj.asObservable();
  utente!: AuthData;

  constructor(private Http: HttpClient, private router: Router) {}

  login(data: { email: string; password: string }) {
    return this.Http.post<AuthData>(`${this.url}/login`, data).pipe(
      tap((log) => {
        console.log(log);
        this.authSubj.next(log);
        this.utente = log;
        console.log(this.utente);
        localStorage.setItem('user', JSON.stringify(log));
        this.router.navigate(['/']);
      }),
      catchError(this.errors)
    );
  }

  restore() {
    const user = localStorage.getItem('user');
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }
    const newUser = JSON.parse(user);
    if (this.jwt.isTokenExpired(newUser.accessToken)) {
      this.router.navigate(['/login']);
      return;
    }
    this.authSubj.next(newUser);
  }

  register(data: {
    username: string;
    name: string;
    surname: string;
    email: string;
    password: string;
    gender: string;
  }) {
    return this.Http.post(`${this.url}/register`, data);
  }

  logout() {
    this.authSubj.next(null);
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  private errors(err: any) {
    switch (err.error) {
      case 'Email already exist':
        return throwError('Email già registrata');
        break;
      case 'Email format is invalid':
        return throwError('Formato mail non valido');
        break;

      case 'Cannot find user':
        return throwError('Utente inesistente');
        break;

      default:
        return throwError('Errore nella chiamata');
        break;
    }
  }


}
