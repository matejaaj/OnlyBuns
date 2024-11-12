import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../infrastructure/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  viewMode: 'following' | 'nearby' | 'trending' | 'chat' = 'following';

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    this.authService.checkIfAdmin().then((isAdmin) => {
      console.log('Is user admin?', isAdmin);
    });
  }

  setViewMode(mode: 'following' | 'nearby' | 'trending'): void {
    this.viewMode = mode;
  }

  logout(): void {
    console.log(this.authService.isAuthenticated() + ' AUTHENTICATED');
    this.authService.logout();
  }
}
