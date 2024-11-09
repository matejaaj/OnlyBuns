import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostDTO } from '../dto/post.dto';
import { CommentDTO } from '../dto/comment.dto';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private postApiUrl = 'http://localhost:8080/api/posts';
  private likeApiUrl = 'http://localhost:8080/api/likes';
  private commentApiUrl = 'http://localhost:8080/api/comments';

  constructor(private apiService: ApiService) {}

  getAllPosts(): Observable<PostDTO[]> {
    return this.apiService.get(this.postApiUrl);
  }

  likePost(postId: number, userId: number): Observable<void> {
    return this.apiService.post(`${this.likeApiUrl}/${postId}?userId=${userId}`, {});
  }

  addComment(postId: number, userId: number, content: string): Observable<CommentDTO> {
    return this.apiService.post(`${this.commentApiUrl}/${postId}?userId=${userId}`, content);
  }

  deletePost(postId: number, userId: number): Observable<void> {
    return this.apiService.delete(`${this.postApiUrl}/${postId}?userId=${userId}`);
  }
}
