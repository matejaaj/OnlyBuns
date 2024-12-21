import { Component, OnInit } from '@angular/core';
import { UserService } from '../users.service';
import { User } from '../../../infrastructure/auth/model/user';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  sortBy = 'email';
  isAscending = true;

  // Parametri pretrage
  searchName = '';
  searchEmail = '';
  minPosts: number | null = null;
  maxPosts: number | null = null;

  filteredUsers: User[] = [];
  currentPage = 1;
  pageSize = 5;
  totalPages = 1;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    console.log('Parameters sent to getUsers:', {
      sortBy: this.sortBy,
      isAscending: this.isAscending,
      name: this.searchName || undefined,
      email: this.searchEmail || undefined,
      minPosts: this.minPosts !== null ? this.minPosts : undefined,
      maxPosts: this.maxPosts !== null ? this.maxPosts : undefined,
    });

    this.userService
      .getUsers({
        sortBy: this.sortBy,
        isAscending: this.isAscending,
        name: this.searchName || undefined,
        email: this.searchEmail || undefined,
        minPosts: this.minPosts !== null ? this.minPosts : undefined,
        maxPosts: this.maxPosts !== null ? this.maxPosts : undefined,
      })
      .subscribe((data: User[]) => {
        this.users = data;
        this.updateFilteredUsers();
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
    this.currentPage = 1;
    this.loadUsers();
  }

  updateFilteredUsers(): void {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;

    // Ažuriramo korisnike na osnovu trenutne stranice
    this.filteredUsers = this.users.slice(startIndex, endIndex);

    // Računamo ukupan broj stranica
    this.totalPages = Math.ceil(this.users.length / this.pageSize);
  }

  changePage(newPage: number): void {
    if (newPage < 1 || newPage > this.totalPages) {
      return;
    }
    this.currentPage = newPage;
    this.updateFilteredUsers();
  }
}
