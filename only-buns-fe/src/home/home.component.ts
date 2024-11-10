import { Component } from '@angular/core';
import { AuthService } from '../app/service/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(public authService: AuthService) {} // Koristimo public da bismo mogli koristiti authService direktno u HTML-u

  logout(): void {
    console.log(this.authService.isAuthenticated() + "   AUTHENTICATED");
    this.authService.logout();
  }
}
