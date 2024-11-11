import {
  HttpClient,
  HttpHeaders,
  HttpRequest,
  HttpResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, filter, map } from 'rxjs/operators';

export enum RequestMethod {
  Get = 'GET',
  Head = 'HEAD',
  Post = 'POST',
  Put = 'PUT',
  Delete = 'DELETE',
  Options = 'OPTIONS',
  Patch = 'PATCH',
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  headers = new HttpHeaders({
    Accept: 'application/json',
    'Content-Type': 'application/json', // Default header for JSON requests
  });

  constructor(private http: HttpClient) {}

  // GET request with optional query params
  get(path: string, args?: any): Observable<any> {
    const options = {
      headers: this.headers,
      params: args ? this.serialize(args) : undefined,
    };
    return this.http
      .get(path, options)
      .pipe(catchError(this.checkError.bind(this)));
  }

  // POST request with configurable headers
  post(path: string, body: any, customHeaders?: HttpHeaders): Observable<any> {
    const headers = customHeaders
      ? customHeaders
      : this.setHeadersForBody(body);
    return this.request(path, body, RequestMethod.Post, headers);
  }

  // PUT request with configurable headers
  put(path: string, body: any, customHeaders?: HttpHeaders): Observable<any> {
    const headers = customHeaders
      ? customHeaders
      : this.setHeadersForBody(body);
    return this.request(path, body, RequestMethod.Put, headers);
  }

  // DELETE request with optional body and headers
  delete(
    path: string,
    body?: any,
    customHeaders?: HttpHeaders
  ): Observable<any> {
    const headers = customHeaders ? customHeaders : this.headers;
    return this.request(path, body, RequestMethod.Delete, headers);
  }

  // General request method with custom headers
  private request(
    path: string,
    body: any,
    method = RequestMethod.Post,
    customHeaders?: HttpHeaders
  ): Observable<any> {
    const req = new HttpRequest(method, path, body, {
      headers: customHeaders,
    });

    return this.http.request(req).pipe(
      filter((response) => response instanceof HttpResponse),
      catchError((error) => this.checkError(error))
    );
  }

  // Automatically set Content-Type based on body type (FormData or JSON)
  private setHeadersForBody(body: any): HttpHeaders | undefined {
    if (body instanceof FormData) {
      return undefined; // Angular will set multipart/form-data headers automatically
    }
    return this.headers; // Use JSON headers
  }

  // Serialize query parameters
  private serialize(obj: any): HttpParams {
    let params = new HttpParams();
    for (const key in obj) {
      if (obj.hasOwnProperty(key) && !this.looseInvalid(obj[key])) {
        params = params.set(key, obj[key]);
      }
    }
    return params;
  }

  private looseInvalid(a: string | number): boolean {
    return a === '' || a === null || a === undefined;
  }

  private checkError(error: any): any {
    throw error;
  }
}
