import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  subscribeForm!: FormGroup;
  confirmPass$: BehaviorSubject<boolean> = new BehaviorSubject(true);
  constructor(private fb: FormBuilder, private authSrv: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.subscribeForm = this.fb.group({
      username: [null, Validators.required],
      name: [null, Validators.required],
      email: [null, [Validators.required, Validators.email]],
      surname: [null, Validators.required],
      password: [null, [Validators.required, Validators.minLength(8)]],
      gender: [null, [Validators.required]],
      acceptTerms: [null, Validators.required],
    });
  }

  onRegister() {
    const data = {
      username: this.subscribeForm.controls['username'].value,
      name: this.subscribeForm.controls['name'].value,
      surname: this.subscribeForm.controls['surname'].value,
      email: this.subscribeForm.controls['email'].value,
      password: this.subscribeForm.controls['password'].value,
      gender: this.subscribeForm.controls['gender'].value,
    };
    try {
      this.authSrv.register(data).subscribe();
      this.router.navigate(["/login"])
      console.log(data);
    } catch (error) {
      console.log(error);
      alert(error);
    }
  }
}
