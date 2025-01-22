import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { response } from 'express';
import { error } from 'console';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
  loginForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ){

    this.loginForm = this.fb.group({
      email:['',[Validators.required,Validators.email]],
      password:['',Validators.required]
    });
  }

  onSubmit():void{
    if(this.loginForm.invalid){
      return;
    }
    const {email,password}= this.loginForm.value;
    this.authService.login(email,password).subscribe({
      next:response =>{
        this.successMessage="Login Successfull";
        this.errorMessage=null;
        this.router.navigate(['view-all-location']);
      },
      error: error=>{
        this.errorMessage= "Login failed!";
        this.successMessage=null;
      }
    })
  }

}
