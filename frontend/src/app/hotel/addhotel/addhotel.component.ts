import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Hotel } from '../../model/hotel';
import { HotelService } from '../../service/hotel.service';
import { LocationService } from '../../service/location.service';
import { Router } from '@angular/router';
import { Location } from '../../model/location';

@Component({
  selector: 'app-addhotel',
  templateUrl: './addhotel.component.html',
  styleUrl: './addhotel.component.css'
})
export class AddhotelComponent implements OnInit{

  hotel:Hotel = new Hotel();
  locations: Location[]=[];
  formGroup!: FormGroup;
  image:File| null=null

  constructor(
    private hotelService:HotelService,
    private locationService: LocationService,
    private router: Router,
    private formBuilder:FormBuilder

  ){}

  ngOnInit(): void {
   this.loadLocation();



    this.formGroup = this.formBuilder.group({
      name:['',Validators.required],
      address:['',Validators.required],
      maximumPrice:['',Validators.required],
      minimumPrice:['',Validators.required],
      rating:['',Validators.required],
      location:[null,Validators.required] // this is the others modle that's why we take null

    });
  }

  // Fie selection method
  onFileSelected(evt: Event):void{
    const input = evt.target as HTMLInputElement;
    if(input?.files && input.files[0]){
      this.image = input.files[0];
    }
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



  onSubmit(){
    if(this.formGroup.invalid){
      alert("Plase fill all the required fileds with valid data");
      return;
    }
    if(this.image){
      const hotel:Hotel ={
        ...this.formGroup.value,
        location:{id:this.formGroup.value.location} as Location      
      };

      this.hotelService.createHotel(hotel,this.image).subscribe({
        next:res =>{
          console.log('Hotel Added Successfuly', hotel);
          this.router.navigate(['view-all-hotel']);
        },
        error: err=>{
          console.log('Error adding hotel');
        }
      })
    };
  }


}
