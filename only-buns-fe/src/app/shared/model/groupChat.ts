import {User} from '../../infrastructure/auth/model/user';
import {GroupChatMember} from './groupChatMember';

export interface GroupChat {
  id: number;                     // ID grupe
  name: string;                   // Ime grupe
  adminId: number;                // ID admina grupe
  members: User[];                // Lista članova grupe
  createdAt: Date;                // Datum kreiranja grupe
  memberDetails: GroupChatMember[]; // Detalji članstva korisnika u grupi
}
