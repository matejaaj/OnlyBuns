// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { PostComponent } from './post/post.component';
import {HomeComponent} from '../home/home.component';
import {SignUpComponent} from './sign-up/sign-up.component';
import {LoginComponent} from './login/login.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Početna stranica
  { path: 'posts', component: PostComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'login', component: LoginComponent },
];
