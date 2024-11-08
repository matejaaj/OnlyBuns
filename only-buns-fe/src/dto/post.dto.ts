export interface PostDTO {
  id: number;
  description: string;
  imageUrl?: string;
  createdAt: Date;
  latitude?: number;
  longitude?: number;
  userId: number;
}
