import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Hotel } from '../model/hotel';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  baseUrl: string = 'http://localhost:8080/api/hotel/';

  constructor(private httpClent: HttpClient) { }

  getAllHotel():Observable<any>{

    return this.httpClent.get(this.baseUrl)
    .pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any){
    console.error("An error occured: ",error);
    return throwError(()=> new Error(error.message || "Server Error!"));

  }


  createHotel(hotel: Hotel,image:File): Observable<Hotel>{
    const formData= new FormData();
    formData.append('hotel', new Blob([JSON.stringify(hotel)],{type:'application/json'}));
    formData.append('image',image);
    return this.httpClent.post<Hotel>(this.baseUrl+'save',formData)
    .pipe(
      catchError(this.handleError)
    );
  
  }
}
