import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../service/auth.service';



@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate
{
  constructor(
    private authService: AuthService,
    private router:Router,
    @Inject(PLATFORM_ID) private platform:Object
  ){}


  canActivate(): boolean {
    if(this.authService.isLogging()){
      return true;
    }else{
      this.router.navigate(['/login']);
      return false;
    }
     
    
  }


}