import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { User } from 'src/app/models/user';
import { NutrientService } from 'src/app/services/nutrient.service';

@Component({
  selector: 'app-nutrient',
  templateUrl: './nutrient.component.html',
  styleUrls: ['./nutrient.component.scss']
})
export class NutrientComponent implements OnInit {
  newNutrient!: FormGroup;
  id!: string;
  

  constructor(private fb: FormBuilder, private nutrientSrv: NutrientService) { }

  ngOnInit(): void {
    this.newNutrient = this.fb.group({
      name: [null],
      amount: [null]
    })
  }

  saveNutrient(){
    const data ={
      name: this.newNutrient.controls['name'].value,
      amount: this.newNutrient.controls['amount'].value
    }
    try{
      this.nutrientSrv.saveNutrient(data).subscribe();
    }catch(err){
      console.log(err);
    }
  }

}
