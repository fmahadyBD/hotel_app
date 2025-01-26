import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../service/hotel.service';
import { ActivatedRoute } from '@angular/router';
import { RoomService } from '../../service/room.service';
import { Room } from '../../model/room';

@Component({
  selector: 'app-hotel-details',
  templateUrl: './hotel-details.component.html',
  styleUrl: './hotel-details.component.css'
})
export class HotelDetailsComponent implements OnInit {
 hotel:any;
 hotelId:any;
 rooms:Room []=[];

 constructor(
  private route: ActivatedRoute, 
  private hotelService:HotelService,
  private roomService:RoomService

 ){}


  ngOnInit(): void {
   
    this.hotelId = parseInt(this.route.snapshot.paramMap.get('id') || '0'); 
    this.getHotelDetails(this.hotelId);
    this.getRoomsOfTheHotel(this.hotelId);
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

  getRoomsOfTheHotel(id:number){
    this.roomService.getAllRoomByHotelId(id).subscribe({
      next:res=>{
        this.rooms=res;
      },error:err=>{
        console.log("error in finding room by hotel id: "+err);
      }
    })
  }

}
