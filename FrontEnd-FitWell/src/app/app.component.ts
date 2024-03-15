import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'FrontEnd-FitWell';
  constructor(private authSrv: AuthService) {}

  ngOnInit() {
    this.authSrv.restore();
  }
}
