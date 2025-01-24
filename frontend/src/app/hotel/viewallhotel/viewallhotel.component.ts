import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../service/hotel.service';

@Component({
  selector: 'app-viewallhotel',
  templateUrl: './viewallhotel.component.html',
  styleUrl: './viewallhotel.component.css'
})
export class ViewallhotelComponent implements OnInit{

  hotels:any;

  constructor(
    private hotelService:HotelService
  ){}

  ngOnInit(): void {
    this.loadHotels();
  }

  loadHotels(){
    this.hotelService.getAllHotel().subscribe({
      next:res=>{
        this.hotels=res;
      },
      error: err=>{
        console.log(err);
      }
    });
  }

}
