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
    'Content-Type': 'application/json', // Ovo zaglavlje će se preskočiti za FormData
  });

  constructor(private http: HttpClient) {}

  get(path: string, args?: any): Observable<any> {
    const options = {
      headers: this.headers,
      params: args ? this.serialize(args) : undefined,
    };

    return this.http
      .get(path, options)
      .pipe(catchError(this.checkError.bind(this)));
  }

  post(path: string, body: any, customHeaders?: HttpHeaders): Observable<any> {
    return this.request(path, body, RequestMethod.Post, customHeaders);
  }

  put(path: string, body: any): Observable<any> {
    return this.request(path, body, RequestMethod.Put);
  }

  delete(path: string, body?: any): Observable<any> {
    return this.request(path, body, RequestMethod.Delete);
  }

  private request(
    path: string,
    body: any,
    method = RequestMethod.Post,
    customHeaders?: HttpHeaders
  ): Observable<any> {
    // Ako je body tipa FormData, uklanjamo Content-Type zaglavlje
    const headers =
      body instanceof FormData
        ? customHeaders || new HttpHeaders({ Accept: 'application/json' })
        : customHeaders || this.headers;

    const req = new HttpRequest(method, path, body, { headers });

    return this.http
      .request(req)
      .pipe(filter((response) => response instanceof HttpResponse))
      .pipe(map((response: HttpResponse<any>) => response.body))
      .pipe(catchError((error) => this.checkError(error)));
  }

  private checkError(error: any): any {
    throw error;
  }

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
}
