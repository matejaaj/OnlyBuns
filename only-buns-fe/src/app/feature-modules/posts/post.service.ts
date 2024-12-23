import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../posts/model/post';
import { Comment } from '../posts/model/comment';
import { ApiService } from '../../infrastructure/api.service';
import { Like } from '../posts/model/like';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private postApiUrl = 'http://localhost:8080/api/posts';
  private likeApiUrl = 'http://localhost:8080/api/likes';
  private commentApiUrl = 'http://localhost:8080/api/comments';

  private imageCache = new Map<string, string>();

  constructor(private apiService: ApiService, private http: HttpClient) {}

  getAllPosts(): Observable<Post[]> {
    return this.apiService.get(this.postApiUrl);
  }

  likePost(postId: number): Observable<Post> {
    return this.apiService
      .postWithResponse<Post>(`${this.likeApiUrl}/${postId}`, null)
      .pipe(
        map((response) => {
          const post = response.body; // Ekstrakcija tela odgovora
          if (!post) {
            throw new Error('Invalid response: Post data not found');
          }
          return post; // Vraćamo ažurirani post objekat
        })
      );
  }

  addComment(
    postId: number,
    userId: number,
    content: string
  ): Observable<Comment> {
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

  getCachedImage(imagePath: string): Observable<string> {
    const cachedImage = this.imageCache.get(imagePath);
    if (cachedImage) {
      // Ako je slika u kešu, vraćamo je kao Observable
      return new Observable((observer) => {
        observer.next(cachedImage);
        observer.complete();
      });
    }

    // Ako nije u kešu, preuzimamo je sa servera
    return this.http
      .get(`http://localhost:8080/uploads/${imagePath}`, {
        responseType: 'blob',
      })
      .pipe(
        map((blob) => {
          const objectUrl = URL.createObjectURL(blob); // Kreiranje URL-a za blob
          this.imageCache.set(imagePath, objectUrl); // Keširanje slike
          return objectUrl;
        })
      );
  }

  updateAdEligibility(postId: number): Observable<Post> {
    return this.apiService
      .put(`${this.postApiUrl}/ad-eligibility/${postId}`, {})
      .pipe(
        map((response: any) => {
          // Pretpostavka da API vraća ažurirani post
          const updatedPost: Post = response;
          return updatedPost; // Osiguravamo da Observable vrati ažurirani post
        })
      );
  }
}
