import { Comment } from './comment';
import { Image } from '../../../shared/model/image';
import { Location } from '../../../shared/model/location';

export interface Post {
  id: number;
  description: string;
  createdAt: Date;
  userId: number;
  image: Image;
  location: Location;
  likeCount: number;
  comments: Comment[];
}
