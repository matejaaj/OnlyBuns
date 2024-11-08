export interface NotificationDTO {
  id: number;
  userId: number;
  content: string;
  sentAt: Date;
  isRead: boolean;
}
