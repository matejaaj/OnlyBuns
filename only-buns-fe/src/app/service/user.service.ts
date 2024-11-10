import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import {UserDTO} from '../dto/user.dto';
import {HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly whoamiUrl = 'http://localhost:8080/api/whoami'; // URL za trenutnog korisnika
  private readonly usersUrl = 'http://localhost:8080/api/user/all'; // URL za listu svih korisnika (samo za admin)

  currentUser!: any;

  constructor(private apiService: ApiService) {}

  // Metoda za preuzimanje informacija o trenutno prijavljenom korisniku
  getMyInfo(): Observable<any> {
    return this.apiService.get(this.whoamiUrl)
      .pipe(map(user => {
        this.currentUser = user; // Čuva trenutnog korisnika u `currentUser`
        return user;
      }));
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
  }): Observable<UserDTO[]> {

    // Kreiramo URL string sa svim parametrima, uključujući i prazne vrednosti
    const url = `http://localhost:8080/api/user/sort?sortBy=${params.sortBy}&isAscending=${params.isAscending}` +
      `&name=${params.name || ''}&email=${params.email || ''}` +
      `&minPosts=${params.minPosts !== undefined ? params.minPosts : ''}` +
      `&maxPosts=${params.maxPosts !== undefined ? params.maxPosts : ''}`;

    console.log("Sending direct URL with parameters:", url);
    return this.apiService.get(url);
  }
}
