import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post';
import { Comment } from '../model/comment';
import { AuthService } from '../../../infrastructure/auth/auth.service';
import { Like } from '../model/like';
import { Observable } from 'rxjs';

interface ExtendedPostDTO extends Post {
  isLiked: boolean;
  newCommentContent?: string;
  isEditing?: boolean; // Flag za uređivanje
  newDescription?: string; // Privremeni opis za uređivanje
}

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {
  posts: ExtendedPostDTO[] = [];
  userLikes: Like[] = []; // Svi lajkovi za proveru
  constructor(
    private postService: PostService,
    protected authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.isAuthenticated() == false) {
      this.loadPosts();
    } else {
      this.loadLikes();
    }
  }

  loadPosts(): void {
    this.postService.getAllPosts().subscribe((data) => {
      this.posts = data.map((post) => ({
        ...post,
        isLiked: false,
        newCommentContent: '',
        isEditing: false,
        newDescription: '',
      }));
      this.updateIsLikedStatus(); // Ažuriraj `isLiked` kada su postovi učitani
    });
    console.log(this.posts);
    console.log("******************")
  }

  loadLikes(): void {
    this.postService.getLikes().subscribe((likes) => {
      console.log('DOBAVLJAM SVE LAJKOVE');
      this.userLikes = likes;
      this.loadPosts(); // Učitaj postove tek nakon što imamo sve lajkove
    });
  }

  likePost(postId: number): void {
    const userId = this.getCurrentUserId();
    this.postService.likePost(postId, userId).subscribe(() => {
      const post = this.posts.find((p) => p.id === postId);
      if (post && !post.isLiked) {
        post.isLiked = true;
        post.likeCount += 1;
      }
    });
  }

  addComment(postId: number, content?: string): void {
    if (typeof content !== 'string') return;
    const userId = this.getCurrentUserId();
    if (content.trim()) {
      this.postService
        .addComment(postId, userId, content)
        .subscribe((comment: Comment) => {
          const post = this.posts.find((p) => p.id === postId);
          if (post) {
            post.comments.push(comment);
            post.newCommentContent = ''; // Reset polja za unos komentara
          }
        });
    }
  }

  deletePost(postId: number): void {
    const userId = this.getCurrentUserId();
    this.postService.deletePost(postId, userId).subscribe(() => {
      this.posts = this.posts.filter((p) => p.id !== postId);
    });
  }

  isOwner(userId: number): boolean {
    console.log(userId);
    console.log(userId === this.getCurrentUserId());
    console.log( "OVO JE OWNER");
    return userId === this.getCurrentUserId();
  }

  getCurrentUserId(): number {
    console.log(this.authService.getUserId());
    console.log("OVO JE USERID");
    return this.authService.getUserId();
  }

  // Omogućava uređivanje
  enableEdit(post: ExtendedPostDTO): void {
    post.isEditing = true;
    post.newDescription = post.description;
  }

  // Sprema promene i šalje ih na backend
  saveEdit(post: ExtendedPostDTO): void {
    if (!post.newDescription || post.newDescription.trim() === '') {
      return;
    }

    const userId = this.getCurrentUserId();
    this.postService
      .updatePost(post.id, userId, post.newDescription)
      .subscribe((updatedPost) => {
        // Ažuriramo samo vrednost `description`
        post.description = updatedPost.description;
        post.isEditing = false; // Zatvorimo uređivanje nakon izmene
      });
  }
  // Otkazuje uređivanje
  cancelEdit(post: ExtendedPostDTO): void {
    post.isEditing = false;
    post.newDescription = post.description;
  }

  updateIsLikedStatus(): void {
    const userId = this.getCurrentUserId();
    this.posts.forEach((post) => {
      post.isLiked = this.userLikes.some(
        (like) => like.postId === post.id && like.userId === userId
      );
    });
  }
}
