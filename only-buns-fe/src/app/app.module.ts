import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent} from '../home/home.component';
import { AppRoutingModule } from './app-routing.module'; // Modul za rute
import { AppComponent } from './app.component';
import { PostComponent} from './post/post.component';// Uvoz komponente za prikaz objave

@NgModule({
  declarations: [
    AppComponent,
    PostComponent, // Sada možete deklarisati PostComponent jer više nije standalone
    HomeComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    AppRoutingModule // Uvoz modula za rute
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
