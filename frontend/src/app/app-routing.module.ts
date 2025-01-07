import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddlocationComponent } from './location/addlocation/addlocation.component';
import { ViewalllocationComponent } from './location/viewalllocation/viewalllocation.component';
const routes: Routes = [
  {path:'add-location',component:AddlocationComponent},
  {path:'view-all-location',component: ViewalllocationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
