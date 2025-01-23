import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../service/auth.service';



@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate
{
  constructor(
    private authService: AuthService,
    private router:Router,
    @Inject(PLATFORM_ID) private platform:Object
  ){}


  canActivate(): boolean {
    if(this.authService.isAdmin()){
      return true;
    }else{
      this.router.navigate(['/login']);
      return false;
    }
     
    
  }


}