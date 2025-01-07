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
    private locationSerice: LocationService
  ){}


  ngOnInit(): void {
   this.loadLocation();
  }

  loadLocation(){
    this.locationSerice.getAllLocation().subscribe({
      next:res=>{
        this.locations=res;
      },error:err=> {
          console.log(err);
      },
    });
  }

}
