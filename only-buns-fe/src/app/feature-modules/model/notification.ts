export interface Notification {
  id: number;
  userId: number;
  content: string;
  sentAt: Date;
  isRead: boolean;
}
