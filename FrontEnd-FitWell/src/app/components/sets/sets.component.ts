import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SetService } from 'src/app/services/set.service';

@Component({
  selector: 'app-sets',
  templateUrl: './sets.component.html',
  styleUrls: ['./sets.component.scss']
})
export class SetsComponent implements OnInit {
  newSet!: FormGroup;

  constructor(private fb: FormBuilder, private setSrv: SetService) { }

  ngOnInit(): void {
    this.newSet = this.fb.group({
      rep: [null, Validators.required],
      weight: [null, Validators.required],
    });
    console.log(this.setSrv.idString);
    
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

}
