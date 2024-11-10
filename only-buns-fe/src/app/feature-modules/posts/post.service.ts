import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostDTO } from '../posts/model/post';
import { Comment } from '../posts/model/comment';
import { ApiService } from '../../infrastructure/api.service';
import { Like } from '../posts/model/like';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private postApiUrl = 'http://localhost:8080/api/posts';
  private likeApiUrl = 'http://localhost:8080/api/likes';
  private commentApiUrl = 'http://localhost:8080/api/comments';

  constructor(private apiService: ApiService) {}

  getAllPosts(): Observable<PostDTO[]> {
    console.log('salje se zahtev');
    return this.apiService.get(this.postApiUrl);
  }

  likePost(postId: number, userId: number): Observable<void> {
    console.log(postId, userId);
    return this.apiService.post(
      `${this.likeApiUrl}/${postId}?userId=${userId}`,
      {}
    );
  }

  addComment(
    postId: number,
    userId: number,
    content: string
  ): Observable<Comment> {
    console.log(postId + ' userid ' + userId + ' content ' + content);
    return this.apiService.post(
      `${this.commentApiUrl}/${postId}?userId=${userId}`,
      JSON.stringify({ content })
    );
  }

  deletePost(postId: number, userId: number): Observable<void> {
    return this.apiService.delete(
      `${this.postApiUrl}/${postId}?userId=${userId}`
    );
  }

  updatePost(id: number, userId: number, newDescription: string) {
    return this.apiService.put(
      `${this.postApiUrl}/${id}?userId=${userId}`,
      newDescription
    );
  }

  getLikes(): Observable<Like[]> {
    console.log('DOBAVLJAM SVE LAJKOVE');
    return this.apiService.get(this.likeApiUrl);
  }
}
