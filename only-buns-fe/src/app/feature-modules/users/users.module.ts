import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfileComponent } from './user-profile/user-profile.component';
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [],
  imports: [CommonModule, ReactiveFormsModule, ReactiveFormsModule, UserProfileComponent],
  exports: [UserProfileComponent],
})
export class UsersModule {}
