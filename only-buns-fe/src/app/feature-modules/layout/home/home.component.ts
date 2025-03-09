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

  isAdmin: boolean = false;

  ngOnInit(): void {
    this.authService.checkIfAdmin().then((isAdmin) => {
      this.isAdmin = isAdmin;
    });
  }

  setViewMode(mode: 'following' | 'nearby' | 'trending'): void {
    this.viewMode = mode;
  }

  logout(): void {
    this.authService.logout();
  }
}
