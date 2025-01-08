import { Component, OnInit } from '@angular/core';
import { LocationService } from '../../service/location.service';

@Component({
  selector: 'app-viewalllocation',
  templateUrl: './viewalllocation.component.html',
  styleUrl: './viewalllocation.component.css'
})
export class ViewalllocationComponent implements OnInit{
  
  locations:any;

  constructor(
    private locationService: LocationService
  ){}


  ngOnInit(): void {
   this.loadLocation();
  }

  loadLocation(){
    this.locationService.getAllLocation().subscribe({
      next:res=>{
        this.locations=res;
      },error:err=> {
          console.log(err);
      },
    });
  }

}
