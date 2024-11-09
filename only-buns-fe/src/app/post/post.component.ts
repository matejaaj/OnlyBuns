import { Component, OnInit } from '@angular/core';
import { PostService } from '../service/post.service';
import { PostDTO } from '../dto/post.dto';
import { CommentDTO } from '../dto/comment.dto';
import {AuthService} from '../service/auth.service';

interface ExtendedPostDTO extends PostDTO {
  isLiked: boolean;
  newCommentContent?: string;
}

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: ExtendedPostDTO[] = [];
  authService: AuthService;

  constructor(private postService: PostService, private authService1: AuthService) {
    this.authService = authService1;
  }

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts(): void {
    this.postService.getAllPosts().subscribe(data => {
      this.posts = data.map(post => ({
        ...post,
        isLiked: false,
        newCommentContent: ''
      }));
    });
  }

  likePost(postId: number): void {
    const userId = this.getCurrentUserId();
    this.postService.likePost(postId, userId).subscribe(() => {
      const post = this.posts.find(p => p.id === postId);
      if (post && !post.isLiked) {
        post.isLiked = true;
        post.likeCount += 1;
      }
    });
  }

  addComment(postId: number, content?: string): void {
    if (typeof content !== 'string') return;

    const userId = this.getCurrentUserId();
    if (content.trim()) {
      this.postService.addComment(postId, userId, content).subscribe((comment: CommentDTO) => {
        const post = this.posts.find(p => p.id === postId);
        if (post) {
          post.comments.push(comment);
          post.newCommentContent = ''; // Reset polja za unos komentara
        }
      });
    }
  }

  deletePost(postId: number): void {
    const userId = this.getCurrentUserId();
    this.postService.deletePost(postId, userId).subscribe(() => {
      this.posts = this.posts.filter(p => p.id !== postId);
    });
  }

  isOwner(userId: number): boolean {
    return userId === this.getCurrentUserId();
  }

  getCurrentUserId(): number {
    return 1;
  }

  isLoggedIn(): boolean {
    return true;
  }

  editPost(postId: number): void {
    console.log('Edit post', postId);
  }
}
