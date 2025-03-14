import {
  ChangeDetectorRef,
  Component,
  Input,
  NgZone,
  OnInit,
} from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post';
import { Comment } from '../model/comment';
import { AuthService } from '../../../infrastructure/auth/auth.service';
import { Like } from '../model/like';
import { Observable } from 'rxjs';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

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
  imports: [NgIf, NgForOf, FormsModule, DatePipe],
  standalone: true,
})
export class PostComponent implements OnInit {
  @Input() posts: ExtendedPostDTO[] = [];
  userLikes: Like[] = []; // Svi lajkovi za proveru
  constructor(
    private postService: PostService,
    protected authService: AuthService,
    private cdr: ChangeDetectorRef, // Dodato
    private zone: NgZone
  ) {}

  async ngOnInit(): Promise<void> {
    const isAdmin = await this.authService.checkIfAdmin();
    console.log(this.authService.isAdmin());
    if (
      this.authService.isAuthenticated() == false ||
      this.authService.isAdmin()
    ) {
      this.loadPosts();
    } else {
      this.loadLikes();
      this.loadUserFeed();
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

      this.posts.forEach((post) => {
        if (post.image && post.image.path) {
          this.postService.getCachedImage(post.image.path).subscribe((url) => {
            post.cachedImagePath = url; // Čuvamo keširani URL slike
          });
        }
      });

      this.updateIsLikedStatus(); // Ažuriraj `isLiked` kada su postovi učitani
    });
  }

  loadLikes(): void {
    this.postService.getLikes().subscribe((likes) => {
      this.userLikes = likes;
      this.loadUserFeed(); // Učitaj postove tek nakon što imamo sve lajkove
    });
  }

  // likePost(postId: number): void {
  //   const userId = this.getCurrentUserId();
  //   this.postService.likePost(postId, userId).subscribe(() => {
  //     const post = this.posts.find((p) => p.id === postId);
  //     if (post && !post.isLiked) {
  //       post.isLiked = true;
  //       post.likeCount += 1;
  //     }
  //   });
  // }

  likePost(postId: number): void {
    this.postService.likePost(postId).subscribe((updatedPost) => {
      const post = this.posts.find((p) => p.id === postId);
      if (post) {
        post.isLiked = true; // Ažuriramo status lajka
        post.likeCount = updatedPost.likeCount; // Ažuriramo broj lajkova iz odgovora
      }
    });
  }

  addComment(postId: number, content?: string): void {
    if (typeof content !== 'string') return;
    const userId = this.getCurrentUserId();
    if (content.trim()) {
      this.postService.addComment(postId, userId, content).subscribe(
        (comment: Comment) => {
          // Nakon uspešnog dodavanja komentara, ponovo učitaj postove
          this.loadUserFeed();
        },
        (error) => {
          if (error.status === 429) {
            alert(
              'You have exceeded the number of comments allowed. Only 5 comments are allowed per hour.'
            );
          }
        }
      );
    }
  }

  deletePost(postId: number): void {
    const userId = this.getCurrentUserId();
    this.postService.deletePost(postId, userId).subscribe(() => {
      this.posts = this.posts.filter((p) => p.id !== postId);
    });
  }

  isOwner(userId: number): boolean {
    return userId === this.getCurrentUserId();
  }

  getCurrentUserId(): number {
    return this.authService.getUserId();
  }

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

  loadUserFeed(): void {
    const userId = this.getCurrentUserId(); // ID trenutno ulogovanog korisnika
    this.postService.getUserFeed(userId).subscribe((data) => {
      this.posts = data.map((post) => ({
        ...post,
        isLiked: false,
        newCommentContent: '',
        isEditing: false,
        newDescription: '',
      }));

      // Keširaj slike
      this.posts.forEach((post) => {
        if (post.image && post.image.path) {
          this.postService.getCachedImage(post.image.path).subscribe((url) => {
            post.cachedImagePath = url;
          });
        }
      });

      this.updateIsLikedStatus(); // Ažuriraj status lajkova
    });
  }

  onCheckboxToggle(post: Post): void {
    // Sačuvaj originalno stanje
    const originalState = post.eligibleForAd;

    // Ažuriraj lokalno stanje odmah
    post.eligibleForAd = true;

    // Izvrši API poziv
    this.postService.updateAdEligibility(post.id).subscribe(
      (updatedPost) => {
        // Uspešno: Ostavlja lokalnu promenu
        console.log('Ad eligibility updated:', updatedPost);
      },
      (error) => {
        // Ako API poziv ne uspe, vrati originalno stanje
        console.error('Failed to update ad eligibility:', error);
        post.eligibleForAd = originalState;
        alert('Failed to mark post as eligible for ads. Please try again.');
      }
    );
  }

  toggleAdEligibility(post: Post): void {
    if (post.eligibleForAd) {
      return;
    }

    this.postService.updateAdEligibility(post.id).subscribe(
      (updatedPost) => {
        // Ažuriraj lokalni model nakon uspešnog odgovora
        const postToUpdate = this.posts.find((p) => p.id === post.id);
        if (postToUpdate) {
          postToUpdate.eligibleForAd = updatedPost.eligibleForAd;
        }
      },
      (error) => {
        console.error('Failed to update ad eligibility:', error);
        alert('Failed to mark post as eligible for ads. Please try again.');
      }
    );
  }
}
