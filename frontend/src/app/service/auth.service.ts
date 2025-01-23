import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Route, Router } from '@angular/router';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { AuthResponse } from '../model/AuthResponse';
import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080';
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  private userRoleSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);



  public userRole$: Observable<string | null> = this.userRoleSubject.asObservable();

  

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) 
   {

    console.log("In constractor ");
    if (this.isBrowser()) {
      const role = this.getUserRole();
      console.log('This is the role:', role);
      if (role) {
        this.userRoleSubject.next(role);
      }
    }

    console.log("In constractor fotter");
    
   }

  register(
    user: {
      name: string;
      email: string;
      password: string;
      cell: string;
      address: string;
      dob: Date;
      gender: string;
      image: string;


    },
  image:File
  ): Observable<AuthResponse> {

    const formData = new FormData();
    formData.append('user',new Blob([JSON.stringify(user)],{type:'application/json'}));
    formData.append('image',image)


    return this.http.post<AuthResponse>(this.baseUrl + '/register', formData) 
      .pipe(
        map(
          (response: AuthResponse) => {
            if (this.isBrowser() && response.token) {
              localStorage.setItem('authToken', response.token);
            }
            return response;
          }
        )
      );
  }


  login(
    email: string,
    password: string

  ): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.baseUrl + '/login', { email, password }, { headers: this.headers })
      .pipe(
        map(
          (response: AuthResponse) => {
            if (this.isBrowser() && response.token) {

              localStorage.setItem('authToken', response.token);
              const decodeToken = this.decodeToken(response.token);
              localStorage.setItem('userRole', decodeToken.role);
              this.userRoleSubject.next(decodeToken.role);
            }
            return response;
          }
        )
      );

  }


  getToken(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('authToken');
    }
    return null;
  }


  getUserRole(): string | null {
    if (this.isBrowser()) {
      return localStorage.getItem('userRole');
    }
    // return null;
    return localStorage.getItem('userRole');
  }


  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }


  isAdminOrHotel(): boolean {
    const role = this.getUserRole();
    return role === 'ADMIN' || role === 'HOTEL';
  }

  isHotel(): boolean {
    return this.getUserRole() === 'HOTEL';
  }

  isUser(): boolean {
    return this.getUserRole() === 'USER';
  }


  isTokenExpired(token: string): boolean {
    const decodeToken = this.decodeToken(token);
    const expire = decodeToken.exp * 1000;
    return Date.now() > expire;
  }

  isLogging(): boolean {
    const token = this.getToken();
    if (token && !this.isTokenExpired(token)) {
      return true;
    }
    this.logout();
    return false;

  }

  logout(): void {
    if (this.isBrowser()) {
      localStorage.removeItem('userRole');
      localStorage.removeItem('authToken');
      this.userRoleSubject.next(null);

    }
    this.router.navigate(['/login']);
  }

  hasRole(roles: string[]): boolean {
    const userRole = this.getUserRole();
    return userRole ? roles.includes(userRole) : false;

  }


  // private isBrowser(): boolean {
  //   return isPlatformBrowser(this.platformId);
  // }
  private isBrowser(): boolean {
    const isBrowser = isPlatformBrowser(this.platformId);
    console.log('Is this a browser?', isBrowser);  
    return isBrowser;
  }


  // private decodeToken(token: string) {
  //   const payload = token.split('.')[1];//sbuject,role,expired
  //   return JSON.parse(atob(payload));

  // }

  private decodeToken(token: string) {
    try {
        const payload = token.split('.')[1];
        return JSON.parse(atob(payload));
    } catch (error) {
        console.error('Error decoding token:', error);
        return null;
    }
}

}
