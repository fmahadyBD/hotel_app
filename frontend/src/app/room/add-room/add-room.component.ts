import { Component, OnInit } from '@angular/core';
import { Room } from '../../model/room';
import { Hotel } from '../../model/hotel';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RoomService } from '../../service/room.service';
import { HotelService } from '../../service/hotel.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrl: './add-room.component.css'
})
export class AddRoomComponent implements OnInit{

  room:Room = new Room();
  hotels:Hotel[]=[];
  formGroup!: FormGroup;
  image:File |null =null;

  constructor(
    private roomService:RoomService,
    private hotelService:HotelService,
    private router:Router,
    private formBuilder:FormBuilder
  ){}
  ngOnInit(): void {
    this.locadHotel();

    this.formGroup = this.formBuilder.group({
      name:['',Validators.required],
      price:['',Validators.required],
      area:['',Validators.required],
      adultNo:['',Validators.required],
      childNo:['',Validators.required],
      hotel:[null,Validators.required]


    })

    
  }

  onFileSelected(evt:Event):void{
    const input = evt.target as HTMLInputElement;
    if(input?.files && input.files[0]){
      this.image= input.files[0];
    }
  }
  locadHotel(){
    this.hotelService.getAllHotel().subscribe({
      next:res=>{
        this.hotels=res;

      },error:err=>{
        console.log(err);
      }
    })
  }

  onSubmit(){
    if(this.formGroup.invalid){
      alert("Please fill all the required fileds with valid data");
      return;
    }
    if(this.image){
      const room:Room={
        ...this.formGroup.value,
        hotel:{id:this.formGroup.value.hotel} as Hotel
      };
      this.roomService.createRoom(room,this.image).subscribe({
        next:res=>{
          console.log("Add room successfullyy",room);
          this.router.navigate(['view-all-room']);
        },
        error:err=>{
          console.log('Error in adding room');
        }
      })

      
    }
  }



}
