import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel
import { MapSelectorComponent } from './map-selector/map-selector.component'; // Declare MapSelectorComponent

@NgModule({
  declarations: [
    MapSelectorComponent, // Declare MapSelectorComponent here
  ],
  imports: [
    CommonModule,
    FormsModule, // FormsModule is required for ngModel
  ],
  exports: [
    MapSelectorComponent, // Export MapSelectorComponent to use it in other modules
  ],
})
export class SharedModule {}
