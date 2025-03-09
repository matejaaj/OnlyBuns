import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule, // FormsModule is required for ngModel
  ],
  exports: [],
})
export class SharedModule {}
