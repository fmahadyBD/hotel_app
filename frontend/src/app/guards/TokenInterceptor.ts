import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthService } from "../service/auth.service";
import { catchError, Observable, throwError } from "rxjs";

@Injectable()
// Aumatice run every where
export class TokenInterceptor implements HttpInterceptor{
    constructor(
        private autthService:AuthService,
    ){}
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.autthService.getToken();
//         if(token){
//             req = req.clone({
//                 setHeaders: {
//                     Authorization: `Bearer ${token}`,
//                 }
//             });
//         }
//         return next.handle(req);
//     }
// }


if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }
  return next.handle(req).pipe(
    catchError(err => {
      if (err.status === 401) {
        this.autthService.logout();
      }
      return throwError(err);
    })
  );
}
}