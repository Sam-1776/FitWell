import { Component, OnInit } from '@angular/core';
import { Chart, registerables} from 'chart.js';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

Chart.register(...registerables);

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  user!: User;
  days!: { name: string; isToday: boolean; borderClass: string }[];
  today!: string;

  constructor(private userSrv: UserService) {}

  ngOnInit(): void {
    this.getUser();
    this.getStatsW();
    this.getStatsD();
    this.days = this.generateWeekDays();
    this.today = new Date().toLocaleDateString('en-US', { weekday: 'long' });
  }

  getUser() {
    this.userSrv.getProfile().subscribe((utente: User) => {
      this.user = utente;
      console.log(this.user);
    });
  }

  getStatsW() {
    new Chart('statsWorkout', {
      type: 'line',
      data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8'],
        datasets: [
          {
            label: 'Rep',
            data: [0],
            borderWidth: 1,
            borderColor: "rgb(0, 255, 64)",
            backgroundColor: "rgb(0, 255, 64)"
          },
          {
            label: 'Weight',
            data: [0],
            borderWidth: 1,
            borderColor: "#f1f8fa",
            backgroundColor: "#f1f8fa"
          }
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }

  getStatsD() {
    new Chart('statsDiet', {
      type: 'line',
      data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8'],
        datasets: [
          {
            label: 'Diet',
            data: [0],
            borderWidth: 1,
            borderColor: "rgb(0, 255, 64)",
            backgroundColor: "rgb(0, 255, 64)"
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }


  generateWeekDays(): { name: string; isToday: boolean; borderClass: string }[] {
    const weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const today = new Date().toLocaleDateString('en-US', { weekday: 'long' });
    const days: { name: string; isToday: boolean; borderClass: string }[] = [];

    for (let i = 0; i < 7; i++) {
      const index = i % 7;
      const dayName = weekdays[index];
      const isToday = (dayName === today);
      const borderClass = isToday ? 'today-border' : ''; // Aggiunta della classe per i bordi del giorno odierno

      days.push({ name: dayName, isToday: isToday, borderClass: borderClass });
    }

    return days;
  }
}
