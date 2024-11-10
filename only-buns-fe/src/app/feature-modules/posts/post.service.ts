import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post as Post } from '../posts/model/post';
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

  getAllPosts(): Observable<Post[]> {
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

  createPost(post: Post, image: File): Observable<Post> {
    const formData = new FormData();

    // Dodaj JSON podatke kao Blob sa MIME tipom "application/json"
    formData.append(
      'post',
      new Blob([JSON.stringify(post)], { type: 'application/json' })
    );

    // Dodaj sliku
    formData.append('image', image);

    // Pozivamo ApiService bez postavljanja Content-Type zaglavlja, jer će
    // Angular automatski postaviti "multipart/form-data" kada koristi FormData
    return this.apiService.post(this.postApiUrl, formData);
  }
}
