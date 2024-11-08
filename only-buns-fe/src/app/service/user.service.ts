import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO} from '../dto/user.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  getUsers(sortBy: string = 'email', isAscending: boolean = true): Observable<UserDTO[]> {
    let params = new HttpParams()
      .set('sortBy', sortBy)
      .set('isAscending', isAscending.toString());
    return this.http.get<UserDTO[]>(this.apiUrl, { params });
  }

}
