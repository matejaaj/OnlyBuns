export interface ChatMessage {
  id?: number;           // ID poruke
  content: string;      // Sadržaj poruke
  createdAt: string;         // Vreme slanja poruke
  senderId: number;     // ID pošiljaoca
  groupId?: number;     // ID grupe (null za privatne poruke)
  type: MessageType;    // Tip poruke (CHAT, JOIN, LEAVE)
}

export type MessageType = 'CHAT' | 'JOIN' | 'LEAVE';
