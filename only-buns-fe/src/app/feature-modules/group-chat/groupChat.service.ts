import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupChat} from '../../shared/model/groupChat';

@Injectable({
  providedIn: 'root',
})
export class GroupChatService {
  private baseUrl = 'http://localhost:8080/api/groups';

  constructor(private http: HttpClient) {}

  createGroupChat(name: string, adminId: number): Observable<GroupChat> {
    return this.http.post<GroupChat>(`${this.baseUrl}`, { name, adminId });
  }

  addMember(groupId: number, userId: number): Observable<GroupChat> {
    return this.http.post<GroupChat>(`${this.baseUrl}/${groupId}/members`, { userId });
  }

  removeMember(groupId: number, userId: number): Observable<GroupChat> {
    return this.http.delete<GroupChat>(`${this.baseUrl}/${groupId}/members/${userId}`);
  }

  getGroupChat(groupId: number): Observable<GroupChat> {
    return this.http.get<GroupChat>(`${this.baseUrl}/${groupId}`);
  }
}
