import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

/*
This After Angular 15
Recommendation:
For new Angular projects (Angular 15+): Use Type 01 (Functional Guard) as it aligns with Angular's modern features and coding style.
*/

export const AdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn() && authService.isAdmin()) {
    return true; // Allow navigation
  } else {
    console.log("Unauthorized access, redirecting to login.");
    router.navigate(['/login']);
    return false; // Block navigation
  }
};
