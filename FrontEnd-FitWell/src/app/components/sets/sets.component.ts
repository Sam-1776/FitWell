import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Set } from 'src/app/models/set';
import { SetService } from 'src/app/services/set.service';

@Component({
  selector: 'app-sets',
  templateUrl: './sets.component.html',
  styleUrls: ['./sets.component.scss']
})
export class SetsComponent implements OnInit {
  newSet!: FormGroup;
  id!: string;
  setId!: string;


  @Input() setList!: Set[];
  @Input() index!: number;

  constructor(private fb: FormBuilder, private setSrv: SetService, private route: ActivatedRoute,) { }

  ngOnInit(): void {
    this.newSet = this.fb.group({
      rep: [null, Validators.required],
      weight: [null, Validators.required],
    });
    console.log(this.setSrv.idString);
    console.log(this.setList);
    this.takeId()
    if (this.id) {
      const SetValue = this.setList[this.index];
      this.setId = SetValue.id;
      this.setValueForSet(SetValue);
    }
    
  }


  saveNewSet(){
    const data = {
      rep: this.newSet.controls['rep'].value,
      weight: this.newSet.controls['weight'].value
    };
    try{
      this.setSrv.saveSet(data).subscribe()
    }catch(err){
      console.log(err);
    }
  }



  takeId() {
    this.route.params.subscribe((parm) => {
      this.id = parm['id'];
    });
  }

  setValueForSet(setted: Set){
    this.newSet.patchValue({
      rep: setted.rep,
      weight: setted.weight
    })
  }


  modSet(){
    const data = {
      rep: this.newSet.controls['rep'].value,
      weight: this.newSet.controls['weight'].value
    };
    try{
      this.setSrv.modSet(this.setId, data).subscribe()
    }catch(err){
      console.log(err);
    }
  }

}
