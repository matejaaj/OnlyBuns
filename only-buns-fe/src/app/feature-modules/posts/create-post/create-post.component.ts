import { Component } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  standalone: true,
  imports: [FormsModule], // Dodaj ovde FormsModule
  styleUrls: ['./create-post.component.css'],
})
export class CreatePostComponent {
  post: Post = {
    id: 0,
    description: '',
    createdAt: new Date(),
    userId: 1, // Zakucana vrednost za testiranje, ovo ćeš promeniti kasnije
    image: { path: '' },
    location: {
      country: 'Serbia',
      city: 'Belgrade',
      address: 'Knez Mihailova',
      number: 1,
      latitude: 44.8176,
      longitude: 20.4633,
    },
    likeCount: 0,
    comments: [],
  };
  selectedImage: File | null = null;

  constructor(private postService: PostService, private router: Router) {}

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedImage = input.files[0];
    }
  }

  createPost() {
    if (this.selectedImage && this.post.description) {
      this.postService.createPost(this.post, this.selectedImage).subscribe(
        (response) => {
          console.log('Post created successfully:', response);
          this.router.navigate(['/posts']); // Navigacija ka listi postova nakon kreiranja
        },
        (error) => {
          console.error('Error creating post:', error);
        }
      );
    } else {
      alert('Please select an image and enter a description.');
    }
  }
}
