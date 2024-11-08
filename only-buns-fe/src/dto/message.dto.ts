export interface MessageDTO {
  id: number;
  content: string;
  sentAt: Date;
  senderId: number;
  recipientId: number;
  isRead: boolean;
}
