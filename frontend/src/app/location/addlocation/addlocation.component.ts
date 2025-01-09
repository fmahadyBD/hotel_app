import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LocationService } from '../../service/location.service';
import { Location } from '../../model/location';
import { Router } from '@angular/router';

@Component({
  selector: 'app-addlocation',
  templateUrl: './addlocation.component.html',
  styleUrl: './addlocation.component.css'
})
export class AddlocationComponent implements OnInit{

  location: Location = new Location();
  formGroup!: FormGroup;
  image:File |null =null;



  constructor(
    private locationService:LocationService,
    private formBuilder:FormBuilder,
    private router: Router
  ){}

  ngOnInit(): void {
   this.formGroup =this.formBuilder.group({
      name: ['',Validators.required]
    
    });

  }

  //File Selection method
  onFilesSelected(event: Event):void{
    const input = event.target as HTMLInputElement; // insure the correct formate
    if(input?.files && input.files[0]){
      this.image = input.files[0];
    }
  }

  onSubmit(){
    if(this.formGroup.invalid){
      console.log("Please fill all the required fileds with valid data");

    }

    if(this.image){

      const location:Location={
        ...this.formGroup.value
      };

      this.locationService.createLocation(location,this.image).subscribe({
        next:res=>{
          console.log('Location saved successfully',location);
          this.router.navigate(['view-all-location']);
        },
        error:err=>{
          console.log('Error in saving location',err);
        }
      });

    }else{
      alert('Please selete and image');
    }



  }

}
