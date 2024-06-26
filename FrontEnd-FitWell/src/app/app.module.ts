import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Route} from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { TokenInterceptor } from './auth/token.interceptor';
import { FormsModule ,ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from './auth/auth.guard';

import { AppComponent } from './app.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { HomeComponent } from './components/home/home.component';
import { WorkoutsComponent } from './components/workouts/workouts.component';
import { WorkoutComponent } from './components/workout/workout.component';
import { SetsComponent } from './components/sets/sets.component';
import { DetailsCardComponent } from './components/details-card/details-card.component';
import { DetailsExerciseComponent } from './components/details-exercise/details-exercise.component';
import { DietComponent } from './components/diet/diet.component';
import { DietDetailsComponent } from './components/diet-details/diet-details.component';
import { CardCoachComponent } from './components/card-coach/card-coach.component';
import { DietNutritionistComponent } from './components/diet-nutritionist/diet-nutritionist.component';
import { NutrientComponent } from './components/nutrient/nutrient.component';
import { RecipeComponent } from './components/recipe/recipe.component';
import { FoodIComponent } from './components/food-i/food-i.component';

const routes: Route[] = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'workout',
    component: WorkoutsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'card',
    component: CardCoachComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'diet',
    component: DietComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'card-diet',
    component: DietNutritionistComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'dietDetails/:id',
    component: DietDetailsComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'details/:id',
    component: DetailsCardComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'exDetails/:cardId/:id',
    component: DetailsExerciseComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '',
  },
]





@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    NavBarComponent,
    HomeComponent,
    WorkoutsComponent,
    WorkoutComponent,
    SetsComponent,
    DetailsCardComponent,
    DetailsExerciseComponent,
    DietComponent,
    DietDetailsComponent,
    CardCoachComponent,
    DietNutritionistComponent,
    NutrientComponent,
    RecipeComponent,
    FoodIComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
