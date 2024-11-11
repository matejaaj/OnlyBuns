// posts.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule, // Importujte SharedModule za MapSelectorComponent
  ],
})
export class PostsModule {}
