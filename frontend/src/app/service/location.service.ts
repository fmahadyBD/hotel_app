import { Location } from '../model/location';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  baseUrl: string = 'http://localhost:8080/api/location/';

  constructor(private httpClient: HttpClient) {}

  createLocation(location: Location, image: File): Observable<Location> {
    const formData = new FormData();
    formData.append(
      'location',
      new Blob([JSON.stringify(location)], { type: 'application/json' })
    );
    formData.append('image', image);

    return this.httpClient.post<Location>(this.baseUrl + 'save', formData);
  }

  //get all location
  getAllLocation(): Observable<any> {
    return this.httpClient.get(this.baseUrl);
  }
  getLocationById(id:number):Observable<Location>{
    return this.httpClient.get<Location>(this.baseUrl+id)
    .pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error:any){
    console.log("An error occured: ",error);
    return throwError(()=>new Error(error.message ||"Server errir"));
  }
}
