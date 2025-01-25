import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Room } from '../model/room';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  baseUrl: string = 'http://localhost:8080/api/room/';

  constructor(
    private httpClind: HttpClient
  ) { }

  createRoom(room:Room,image:File):Observable<Room>{
    const formData = new FormData();
    formData.append( 'room', new Blob([JSON.stringify(room)],{type:'application/json'}));
    formData.append('image',image);
    return this.httpClind.post<Room>(this.baseUrl+'save',formData)
    .pipe(
      catchError(this.handelError)
    );
  }
  
  private handelError(error:any){
    console.error("An error occured: ",error);
    return throwError(()=>new Error(error.message|| "Server Error!"));
  }
}
