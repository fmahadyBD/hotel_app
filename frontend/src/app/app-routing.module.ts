import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddlocationComponent } from './location/addlocation/addlocation.component';
import { ViewalllocationComponent } from './location/viewalllocation/viewalllocation.component';
import { AddhotelComponent } from './hotel/addhotel/addhotel.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { AdminGuard } from './guards/admin.guard';
import { AuthGuard } from './guards/auth.guard';
import { ViewallhotelComponent } from './hotel/viewallhotel/viewallhotel.component';
import { HomePageComponent } from './home-page/home-page.component';
const routes: Routes = [

  { path: '', component: HomePageComponent },

  //location
  { path: 'add-location', component: AddlocationComponent },
  { path: 'view-all-location', component: ViewalllocationComponent},

  //hotel
  { path: 'add-hotel', component: AddhotelComponent, canActivate: [AdminGuard] },
  { path: 'view-all-hotel', component: ViewallhotelComponent },

  //auth
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
