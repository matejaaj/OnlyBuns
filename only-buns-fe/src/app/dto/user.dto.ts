export interface UserDTO {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  address?: string;
  isActive: boolean;
  registeredAt: Date;
  role: string;
  postCount: number;        // Broj objava
  followingCount: number;    // Broj korisnika koje prati
  followerCount: number;     // Broj pratilaca
}
