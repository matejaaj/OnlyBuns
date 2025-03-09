export interface Follow {
  id?: number;                 // ID of the follow record
  followerId: number;          // ID of the user who is following
  followerUsername: string;    // Username of the user who is following
  followedId: number;          // ID of the user being followed
  followedUsername: string;    // Username of the user being followed
  followedAt: string;          // Timestamp when the follow occurred
}
