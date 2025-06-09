import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem('authToken');
    console.log('INTERCEPTOR', token);
    const clonedRequest = token ? req.clone({
        setHeaders: {Authorization: `Bearer ${token}`}
      }) : req;

    return next.handle(clonedRequest).pipe(
      catchError((error) => {
        if(error.status === 403) {
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    )
  }
}
