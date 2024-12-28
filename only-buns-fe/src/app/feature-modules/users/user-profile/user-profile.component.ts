import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import { UserService } from '../users.service';
import {AuthService} from '../../../infrastructure/auth/auth.service';
import {Follow} from '../../../shared/model/follow';
import {ActivatedRoute, Router} from '@angular/router';
import {PostComponent} from '../../posts/post/post.component';
import {AppModule} from '../../../app.module';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    DatePipe,
    PostComponent
  ],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: any = null;
  currentUser: any = null;
  isCurrentUserProfile: boolean = true;
  followers: Follow[] | undefined;
  following: Follow[] | undefined;
  emailToSearch: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  followSuccess: string = '';
  posts: any[] = [];
  routeSubscription!: Subscription;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Subscribujemo se na promene u URL parametru `id`
    this.routeSubscription = this.route.params.subscribe((params) => {
      const userId = params['id'];

      if (userId) {
        this.isCurrentUserProfile = false;
        this.loadUser(userId); // Učitaj podatke za drugog korisnika
      } else {
        this.isCurrentUserProfile = true;
        this.loadCurrentUser(); // Učitaj podatke za trenutno prijavljenog korisnika
      }
    });
  }

  ngOnDestroy(): void {
    // Odjavljujemo se iz `routeSubscription` kako bismo izbegli curenje memorije
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  loadCurrentUser(): void {
    this.userService.getMyInfo().subscribe(
      (user) => {
        this.user = user;
        this.currentUser = user;
        this.loadFollowers();
        this.loadFollowing();
        this.loadUserPosts(this.currentUser.id)
      },
      (err) => {
        this.errorMessage = 'Failed to load user data.';
      }
    );
  }

  loadUser(userId: number): void {
    this.userService.getUserById(userId).subscribe(
      (user) => {
        this.user = user;
        this.loadUserPosts(userId); // Učitaj postove korisnika
      },
      (err) => {
        this.errorMessage = 'Failed to load user profile.';
      }
    );
  }

  loadFollowers(): void {
    this.userService.getFollowers(this.user.id).subscribe(
      (followers) => {
        this.followers = followers;
      },
      (err) => {
        this.errorMessage = 'Failed to load followers.';
      }
    );
  }

  loadFollowing(): void {
    this.userService.getFollowing(this.user.id).subscribe(
      (following) => {
        this.following = following;
      },
      (err) => {
        this.errorMessage = 'Failed to load following.';
      }
    );
  }

  searchUserByEmail(): void {
    if (!this.emailToSearch.trim()) {
      return;
    }

    this.userService.getUserByEmail(this.emailToSearch).subscribe(
      (user) => {
        this.router.navigate(['/profile', user.id]); // Preusmeravanje na profil korisnika
      },
      (err) => {
        this.errorMessage = 'User not found.';
      }
    );
    this.emailToSearch = ''; // Resetovanje polja
  }
  followUser(): void {
    if (!this.user) {
      this.errorMessage = 'No user to follow.';
      return;
    }

    this.userService.followUser(this.user.id).subscribe(
      () => {
        this.followSuccess = `You are now following ${this.user.username}!`;
      },
      (err) => {
        if (err.status === 429) {
        } else {
        }
      }
    );
  }

  unfollowUser(userId: number): void {
    this.userService.unfollowUser(userId).subscribe(
      () => {
        this.loadFollowing();
      },
      (err) => {
      }
    );
  }
  loadUserPosts(userId: number): void {
    this.userService.getUserPosts(userId).subscribe(
      (posts) => {
        this.posts = posts.map((post: any) => {
          // Proveri da li post ima sliku i keširaj je ako postoji
          if (post.image && post.image.path) {
            this.userService
              .getCachedImage(post.image.path)
              .subscribe((cachedPath: string) => {
                post.image.url = cachedPath; // Postavi keširani URL slike
              });
          }
          return post;
        });
      },
      (err) => {
        this.errorMessage = 'Failed to load user posts.';
      }
    );
  }
}
