import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { UserService } from '../users.service';
import {AuthService} from '../../../infrastructure/auth/auth.service';
import {Follow} from '../../../shared/model/follow';

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
  followers: Follow[] | undefined;
  following: Follow[] | undefined;
  emailToFollow: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  protected followSuccess: string = '';

  constructor(private userService: UserService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser(): void {
    this.userService.getMyInfo().subscribe(
      (user) => {
        this.user = user;
        this.loadFollowers();
        this.loadFollowing();
      },
      (err) => {
        this.errorMessage = 'Failed to load user data.';
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

  followUser(): void {
    if (!this.emailToFollow) {
      console.error('Please enter an email to follow.');
      return;
    }

    this.userService.getUserByEmail(this.emailToFollow).subscribe(
      (user) => {
        this.userService.followUser(user.id).subscribe(
          () => {
            this.followSuccess = `You are now following ${user.username}!`;
            this.emailToFollow = '';
            this.loadUser(); // Osvežava podatke korisnika
          },
          (err) => {
            if (err.status === 429) {
              console.error('Rate limit exceeded. Please try again later.');
              this.followSuccess = 'You have reached the follow limit. Please wait a minute before trying again.';
            } else {
              console.error('Failed to follow user. ' + user.id);
            }
          }
        );
      },
      (err) => {
        console.error('User not found.');
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
