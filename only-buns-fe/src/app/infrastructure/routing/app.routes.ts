// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { PostComponent } from '../../feature-modules/posts/post/post.component';
import { HomeComponent } from '../../feature-modules/layout/home/home.component';
import { UserListComponent } from '../../feature-modules/users/user-list/user-list.component';
import { SignUpComponent } from '../auth/sign-up/sign-up.component';
import { LoginComponent } from '../auth/login/login.component';
import { CreatePostComponent } from '../../feature-modules/posts/create-post/create-post.component';
import { UserProfileComponent } from '../../feature-modules/users/user-profile/user-profile.component';
import { ChatComponent } from '../../feature-modules/chat/chat/chat.component';
import {TrendsComponent} from '../../feature-modules/trends/trends.component';
import {AnalyticsComponent} from '../../feature-modules/analytics/analytics.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'posts', component: PostComponent },
  { path: 'users', component: UserListComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'login', component: LoginComponent },
  { path: 'create-post', component: CreatePostComponent },
  { path: 'profile', component: UserProfileComponent },
  { path: 'chat', component: ChatComponent },
  { path: 'trends', component: TrendsComponent},
  { path: 'analytics', component: AnalyticsComponent},
];
