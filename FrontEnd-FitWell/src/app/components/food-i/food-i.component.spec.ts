import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodIComponent } from './food-i.component';

describe('FoodIComponent', () => {
  let component: FoodIComponent;
  let fixture: ComponentFixture<FoodIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FoodIComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FoodIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
