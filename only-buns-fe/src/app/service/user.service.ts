import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly whoamiUrl = 'http://localhost:8080/api/whoami'; // URL za trenutnog korisnika
  private readonly usersUrl = 'http://localhost:8080/api/user/all'; // URL za listu svih korisnika (samo za admin)

  currentUser!: any;

  constructor(private apiService: ApiService) {}

  // Metoda za preuzimanje informacija o trenutno prijavljenom korisniku
  getMyInfo(): Observable<any> {
    return this.apiService.get(this.whoamiUrl).pipe(
      map((user) => {
        this.currentUser = user; // Čuva trenutnog korisnika u `currentUser`
        return user;
      })
    );
  }

  // Metoda za preuzimanje liste svih korisnika (samo za admina)
  getAll(): Observable<any> {
    return this.apiService.get(this.usersUrl);
  }

  getUsers(params: {
    sortBy: string;
    isAscending: boolean;
    name?: string;
    email?: string;
    minPosts?: number;
    maxPosts?: number;
  }): Observable<User[]> {
    let httpParams = new HttpParams()
      .set('sortBy', params.sortBy)
      .set('isAscending', String(params.isAscending));

    if (params.name) httpParams = httpParams.set('name', params.name);
    if (params.email) httpParams = httpParams.set('email', params.email);
    if (params.minPosts !== undefined)
      httpParams = httpParams.set('minPosts', String(params.minPosts));
    if (params.maxPosts !== undefined)
      httpParams = httpParams.set('maxPosts', String(params.maxPosts));

    console.log('Sending HTTP parameters:', httpParams.toString());

    console.log('SLANJE GET ZAHTEVA ZA USEREEE');
    return this.apiService.get('http://localhost:8080/api/user/sort', {
      params: httpParams,
    });
  }
}
