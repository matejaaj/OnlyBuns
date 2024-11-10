import {Component, OnInit} from '@angular/core';
import { AuthService } from '../app/service/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  constructor(public authService: AuthService) {} // Koristimo public da bismo mogli koristiti authService direktno u HTML-u
  ngOnInit(): void {
    this.authService.checkIfAdmin().then(isAdmin => {
      console.log("Is user admin?", isAdmin);
    });
  }
  logout(): void {
    console.log(this.authService.isAuthenticated() + "   AUTHENTICATED");
    this.authService.logout();
  }
}
