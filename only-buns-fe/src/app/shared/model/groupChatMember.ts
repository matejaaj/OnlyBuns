export interface GroupChatMember {
  id: number;                    // ID članstva
  groupId: number;               // ID grupe
  userId: number;                // ID korisnika
  joinedAt: Date;                // Datum pridruživanja
}
