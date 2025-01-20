import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddlocationComponent } from './location/addlocation/addlocation.component';
import { ViewalllocationComponent } from './location/viewalllocation/viewalllocation.component';
import { AddhotelComponent } from './hotel/addhotel/addhotel.component';
const routes: Routes = [
  {path:'add-location',component:AddlocationComponent},
  {path:'view-all-location',component: ViewalllocationComponent},
  {path:'add-hotel',component:AddhotelComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
