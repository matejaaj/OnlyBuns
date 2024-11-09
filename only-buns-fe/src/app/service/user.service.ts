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

  getUsers(sortBy: string = 'email', isAscending: boolean = true): Observable<UserDTO[]> {
    const args = {
      sortBy: sortBy,
      isAscending: isAscending.toString()
    };

    return this.apiService.get(this.usersUrl, args);
  }
}
