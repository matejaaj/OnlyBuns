// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { PostComponent } from '../../feature-modules/posts/post/post.component';
import { HomeComponent } from '../../feature-modules/layout/home/home.component';
import { UserListComponent } from '../../feature-modules/users/user-list/user-list.component';
import { SignUpComponent } from '../auth/sign-up/sign-up.component';
import { LoginComponent } from '../auth/login/login.component';
import { CreatePostComponent } from '../../feature-modules/posts/create-post/create-post.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'posts', component: PostComponent },
  { path: 'users', component: UserListComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create-post', component: CreatePostComponent },
];
