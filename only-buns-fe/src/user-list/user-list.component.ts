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
  sortBy = 'email';
  isAscending = true;

  // Parametri pretrage
  searchName = '';
  searchEmail = '';
  minPosts: number | null = null;
  maxPosts: number | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    console.log("Parameters sent to getUsers:", {
      sortBy: this.sortBy,
      isAscending: this.isAscending,
      name: this.searchName || undefined,
      email: this.searchEmail || undefined,
      minPosts: this.minPosts !== null ? this.minPosts : undefined,
      maxPosts: this.maxPosts !== null ? this.maxPosts : undefined
    });

    this.userService.getUsers({
      sortBy: this.sortBy,
      isAscending: this.isAscending,
      name: this.searchName || undefined,
      email: this.searchEmail || undefined,
      minPosts: this.minPosts !== null ? this.minPosts : undefined,
      maxPosts: this.maxPosts !== null ? this.maxPosts : undefined
    }).subscribe((data: UserDTO[]) => {
      this.users = data;
      console.log("Filtered users:", this.users);
    });
  }

  onSortChange(sortBy: string): void {
    if (this.sortBy === sortBy) {
      this.isAscending = !this.isAscending;
    } else {
      this.sortBy = sortBy;
      this.isAscending = true;
    }
    this.loadUsers();
  }

  onSearch(): void {
    this.loadUsers();
  }
}
