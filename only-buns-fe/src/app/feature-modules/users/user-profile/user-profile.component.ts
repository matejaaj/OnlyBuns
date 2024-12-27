import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { UserService } from '../users.service';
import {AuthService} from '../../../infrastructure/auth/auth.service';
import {Follow} from '../../../shared/model/follow';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf
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

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const userId = this.route.snapshot.params['id'];

    if (userId) {
      this.isCurrentUserProfile = false;
      this.loadUser(userId);
    } else {
      this.isCurrentUserProfile = true;
      this.loadCurrentUser();
    }
  }

  loadCurrentUser(): void {
    this.userService.getMyInfo().subscribe(
      (user) => {
        this.user = user;
        this.currentUser = user;
        this.loadFollowers();
        this.loadFollowing();
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
    if (!this.emailToSearch) {
      return;
    }

    this.userService.getUserByEmail(this.emailToSearch).subscribe(
      (user) => {
        window.location.href = `/profile/${user.id}`;
      },
      (err) => {
        this.errorMessage = 'User not found.';
      }
    );
    this.emailToSearch = '';
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
          this.followSuccess = 'You have reached the follow limit. Please wait a minute before trying again.';
        } else {
          this.errorMessage = 'Failed to follow user.';
        }
      }
    );
  }

  unfollowUser(userId: number): void {
    this.userService.unfollowUser(userId).subscribe(
      () => {
        this.successMessage = 'User unfollowed successfully.';
        this.loadFollowing();
      },
      (err) => {
        this.errorMessage = 'Failed to unfollow user.';
      }
    );
  }
}
