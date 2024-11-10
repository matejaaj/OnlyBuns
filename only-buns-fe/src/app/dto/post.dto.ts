import {CommentDTO} from './comment.dto';

export interface PostDTO {
  id: number;
  description: string;
  imageUrl: string;
  createdAt: string;
  latitude: number;
  longitude: number;
  userId: number;
  likeCount: number;
  comments: CommentDTO[];
  isLiked?: boolean; // Dodatno polje za praćenje lajka
  newCommentContent?: string; // Polje za unos novog komentara
}
