import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registrationForm: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  image: File | null = null

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {


    this.registrationForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      cell: [''],
      address: [''],
      dob: [''],
      gender: [''],

    },
      { validator: this.passwordMatchValidator }

    );
  }
  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const ConfirmedPassword = formGroup.get('confirmPassword')?.value;
    return password === ConfirmedPassword ? null : { mismatch: true }
  }


  //File Selection method
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement; // insure the correct formate
    if (input?.files && input.files[0]) {
      this.image = input.files[0];
    }
  }

  onSubmit() {
    if (this.registrationForm.invalid) {
      return;
    }
    if (!this.image) {
      this.errorMessage = 'Please select an image.';
      return;
    }


    const { name, email, password, cell, address, dob, gender, image } = this.registrationForm.value;

    this.authService.register(
      { name, email, password, cell, address, dob, gender, image: '' },
      this.image

    ).subscribe({
      next: AuthResponse => {
        this.successMessage = 'Registration Successful! Pleace Check your email to active your account';
        this.router.navigate(['/add-hotel']);
      },
      error: error => {
        this.errorMessage = 'Registration failed. Pleace try again!';
      }
    });



  }

}
