import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private authSrv: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onLogin(form: NgForm) {
    console.log(form);
    const data = {
      email: form.value.email,
      password: form.value.password,
    };
    console.log(data);
    try {
      this.authSrv.login(data).subscribe();
    } catch (error) {
      alert('login errato');
      console.log(error);
      this.router.navigate(['/login-page']);
    }
  }
  

}
