import { Injectable } from '@angular/core';
import { ApiService } from '../../infrastructure/api.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User } from '../../infrastructure/auth/model/user';
import { HttpParams } from '@angular/common/http';
import {Follow} from '../../shared/model/follow';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly whoamiUrl = 'http://localhost:8080/api/whoami'; // URL za trenutnog korisnika
  private readonly usersUrl = 'http://localhost:8080/api/user/all'; // URL za listu svih korisnika (samo za admin)
  private readonly apiBase = 'http://localhost:8080/api';
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

    // Kreiramo URL string sa svim parametrima, uključujući i prazne vrednosti
    const url = `http://localhost:8080/api/user/sort?sortBy=${params.sortBy}&isAscending=${params.isAscending}` +
      `&name=${params.name || ''}&email=${params.email || ''}` +
      `&minPosts=${params.minPosts !== undefined ? params.minPosts : ''}` +
      `&maxPosts=${params.maxPosts !== undefined ? params.maxPosts : ''}`;

    console.log("Sending direct URL with parameters:", url);
    return this.apiService.get(url);
  }

  // Preuzimanje korisnika prema ID
  getUserById(userId: number): Observable<any> {
    return this.apiService.get(`http://localhost:8080/api/user/${userId}`);
  }

  // Promena lozinke
  changePassword(userId: number, newPassword: string): Observable<any> {
    return this.apiService.post(`http://localhost:8080/api/user/${userId}/change-password`, { password: newPassword });
  }

  // Ažuriranje adrese korisnika
  updateAddress(userId: number, address: string): Observable<any> {
    return this.apiService.put(`http://localhost:8080/api/user/${userId}/update-address`, { address });
  }

// Praćenje korisnika
  followUser(userId: number): Observable<any> {
    return this.apiService.post(`${this.apiBase}/follow/${userId}`, {});
  }

  // Prekid praćenja korisnika
  unfollowUser(userId: number): Observable<any> {
    return this.apiService.delete(`${this.apiBase}/follow/${userId}`);
  }

  // Preuzimanje pratilaca
  getFollowers(userId: number): Observable<Follow[]> {
    return this.apiService.get(`${this.apiBase}/follow/followers/${userId}`);
  }

  // Preuzimanje korisnika koje korisnik prati
  getFollowing(userId: number): Observable<Follow[]> {
    return this.apiService.get(`${this.apiBase}/follow/following/${userId}`);
  }

  getUserByEmail(email: string): Observable<User> {
    const url = `http://localhost:8080/api/user/searchEmail?email=${encodeURIComponent(email)}`;
    return this.apiService.get(url);
  }
}
