import { Component, OnInit } from '@angular/core';
import { UserService } from '../app/service/user.service';
import { UserDTO } from '../app/dto/user.dto';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: UserDTO[] = [];
  sortBy = 'email'; // Podrazumevani kriterijum sortiranja
  isAscending = true; // Podrazumevani smer sortiranja (rastuće)

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers(this.sortBy, this.isAscending).subscribe(data => {
      this.users = data;
    });
  }

  onSortChange(sortBy: string): void {
    // Ako je isti kriterijum, promeni smer sortiranja
    if (this.sortBy === sortBy) {
      this.isAscending = !this.isAscending;
    } else {
      // Ako je novi kriterijum, postavi na podrazumevani (rastući)
      this.sortBy = sortBy;
      this.isAscending = true;
    }
    this.loadUsers();
  }
}
