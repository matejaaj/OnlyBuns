// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { PostComponent } from './post/post.component';
import {HomeComponent} from '../home/home.component';
import {UserListComponent} from "../user-list/user-list.component";

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Početna stranica
  { path: 'posts', component: PostComponent },
  { path: 'users', component: UserListComponent }
];
