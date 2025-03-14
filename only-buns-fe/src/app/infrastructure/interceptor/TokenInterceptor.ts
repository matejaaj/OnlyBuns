import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpEvent,
} from '@angular/common/http';
import { AuthService } from '../auth/auth.service';

import { Observable } from 'rxjs';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(public auth: AuthService) {}
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (this.auth.tokenIsPresent()) {
      const token = this.auth.getToken();
      console.log('Authorization header:', `Bearer ${token}`); // Dodaj ovu liniju za provere
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.auth.getToken()}`,
        },
      });
    }
    return next.handle(request);
  }
}
