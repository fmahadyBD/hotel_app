import { Component, OnInit } from '@angular/core';
import { Hotel } from '../../model/hotel';
import { ActivatedRoute } from '@angular/router';
import { LocationService } from '../../service/location.service';
import { HotelService } from '../../service/hotel.service';

@Component({
  selector: 'app-location-details',
  templateUrl: './location-details.component.html',
  styleUrl: './location-details.component.css'
})
export class LocationDetailsComponent implements OnInit{
  location:any;
  locationId:any;
  hotels:Hotel[]=[];

  constructor(
    private router:ActivatedRoute,
    private locationService:LocationService,
    private hotelService: HotelService
  ){}
  ngOnInit(): void {
    this.locationId = parseInt(this.router.snapshot.paramMap.get('id')||'0');
    this.getLocationById(this.locationId);
    this.getHotelByLocationId(this.locationId);
  }

  getLocationById(id:number){
    this.locationService.getLocationById(id).subscribe({
      next:res=>{
        this.location=res;
      },
      error:err=>{
        console.log(err);
      }
    })
  }

  getHotelByLocationId(id:number){
    this.hotelService.getHotelbyLocationId(id).subscribe({
      next:res=>{
        this.hotels=res;
      },
      error:err=>{
        console.log(err);
      }
    })
    
  }
}
