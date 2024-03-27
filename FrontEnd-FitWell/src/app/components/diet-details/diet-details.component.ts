import { getLocaleFirstDayOfWeek } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Chart } from 'chart.js';
import { Diet } from 'src/app/models/diet';
import { FoodsIntermediate } from 'src/app/models/foods-intermediate';
import { Recipe } from 'src/app/models/recipe';
import { User } from 'src/app/models/user';
import { DietService } from 'src/app/services/diet.service';
import { NotebookService } from 'src/app/services/notebook.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-diet-details',
  templateUrl: './diet-details.component.html',
  styleUrls: ['./diet-details.component.scss'],
})
export class DietDetailsComponent implements OnInit {
  diet!: Diet;
  recipe!: Recipe[];
  id!: string;
  user!: User;
  Update!: FormGroup;

  constructor(
    private dietSrv: DietService,
    private route: ActivatedRoute,
    private userSrv: UserService,
    private noteBookSrv: NotebookService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.Update = this.fb.group({
      weight: [null]
    })
    this.getUser()
    this.takeId();
    this.takeDiet();
  }

  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  getUser() {
    this.userSrv.getProfile().subscribe((utente: User) => {
      this.user = utente;
      console.log(this.user);
    });
  }

  takeDiet() {
    console.log(this.id);

    this.dietSrv.getDiet(this.id).subscribe((el) => {
      this.diet = el;
      console.log(this.diet);
      this.recipe = el.recipes
      this.getMacro()
    });
  }


  saveUpdateDiet(){
    const data = {
      dietId: this.id,
      weught: this.Update.controls['weight'].value
    }
    console.log(data);
    console.log(this.user);
    try{
      this.noteBookSrv.saveStatsOnNoteBookD(this.user.noteBookD.id, data).subscribe(el => {
        console.log(el);
      })
    }catch(err){
      console.log(err);
    }
  }


  getMacro() {
    new Chart('Macro', {
      type: 'doughnut',
      data: {
        labels: this.diet.nutrients.map(el => el.name),
        datasets: [
          {
          label: "",
          data: this.diet.nutrients.map(el => el.amount),
          backgroundColor: [
            'rgb(255, 205, 86)',
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)'
          ],
          hoverOffset: 4
        },
      ]
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
}
