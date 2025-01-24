import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddlocationComponent } from './location/addlocation/addlocation.component';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { ViewalllocationComponent } from './location/viewalllocation/viewalllocation.component';
import { AddhotelComponent } from './hotel/addhotel/addhotel.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { ViewallhotelComponent } from './hotel/viewallhotel/viewallhotel.component';
import { HomePageComponent } from './home-page/home-page.component';
import { AddRoomComponent } from './room/add-room/add-room.component';
import { ViewAllRoomComponent } from './room/view-all-room/view-all-room.component';
import { HotelDetailsComponent } from './hotel/hotel-details/hotel-details.component';

@NgModule({
  declarations: [
    AppComponent,
    AddlocationComponent,
    ViewalllocationComponent,
    AddhotelComponent,
    RegisterComponent,
    LoginComponent,
    ViewallhotelComponent,
    HomePageComponent,
    AddRoomComponent,
    ViewAllRoomComponent,
    HotelDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(
      withFetch()
    )
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
