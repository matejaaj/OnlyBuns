import { Component, OnInit } from '@angular/core';
import { PostDTO } from '../dto/post.dto';
import { PostService } from '../service/post.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'] // Ispravka: `styleUrl` u `styleUrls`
})
export class PostComponent implements OnInit {
  posts: PostDTO[] = []; // Polje za sve objave

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(data => {
      this.posts = data;
    });
  }
}
