import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../service/hotel.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-hotel-details',
  templateUrl: './hotel-details.component.html',
  styleUrl: './hotel-details.component.css'
})
export class HotelDetailsComponent implements OnInit {
 hotel:any;
 hotelId:any;

 constructor(
  private route: ActivatedRoute, 
  private hotelService:HotelService
 ){}


  ngOnInit(): void {
   
    this.hotelId = parseInt(this.route.snapshot.paramMap.get('id') || '0'); 
    this.getHotelDetails(this.hotelId);
  }
  getHotelDetails(id: number) {
   this.hotelService.getHotelById(id).subscribe({
    next:res=>{
      this.hotel=res;
    },
    error: err=>{
      console.log(err);
    }
  });
  }
}
