import { Comment } from '../posts/model/comment';

export interface PostDTO {
  id: number;
  description: string;
  imageUrl: string;
  createdAt: string;
  latitude: number;
  longitude: number;
  userId: number;
  likeCount: number;
  comments: Comment[];
  isLiked?: boolean; // Dodatno polje za praćenje lajka
  newCommentContent?: string; // Polje za unos novog komentara
}
