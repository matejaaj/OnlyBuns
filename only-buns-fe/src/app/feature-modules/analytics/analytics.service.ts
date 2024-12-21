import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  private baseUrl = 'http://localhost:8080/api/analytics'; // Zameni sa stvarnim URL-om

  constructor(private http: HttpClient) {}

  // Preuzimanje statistike objava i komentara
  getPostsAndCommentsStats(): Observable<any> {
    return this.http.get(`${this.baseUrl}/posts-comments`);
  }

  // Preuzimanje statistike korisničke aktivnosti
  getUserActivityStats(): Observable<any> {
    return this.http.get(`${this.baseUrl}/user-activity`);
  }
}
