import { Component, OnInit } from '@angular/core';
import { ChatService } from '../chat.service';
import { AuthService } from '../../../infrastructure/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { ChatMessage } from '../../../shared/model/chatMessage';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    DatePipe,
  ],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit {
  messages: any[] = [];
  message = '';
  groupId!: number; // Aktivni grupni čat
  newGroupName = ''; // Ime nove grupe
  showCreateGroupModal = false; // Kontrola prikaza modala za kreiranje grupe
  private isSubscribedToMessages = false;
  newMemberEmail: string = ''; // Za unos email-a novog člana
  groupMembers: any[] = []; // Lista članova grupe
  userGroups: any[] = []; // Liste korisničkih grupa
  isCurrentUserAdmin: boolean = false; // Da li je trenutni korisnik admin odabrane grupe
  lastLoadedTimestamp: string | null = null; // Vreme poslednje učitane poruke

  constructor(private chatService: ChatService, protected authService: AuthService) {}

  ngOnInit(): void {
    this.chatService.connect();

    // Učitaj dostupne grupe korisnika
    this.loadUserGroups();
  }

  loadUserGroups(): void {
    const userId = this.authService.getUserId();
    this.chatService.getUserGroups(userId).subscribe({
      next: (groups) => {
        this.userGroups = groups;
        console.log('User groups loaded:', this.userGroups);
        if (this.userGroups.length > 0) {
          this.selectGroup(this.userGroups[0].id); // Automatski izaberi prvu grupu
        }
      },
      error: (error) => console.error('Error loading user groups:', error),
    });
  }

  selectGroup(groupId: number): void {
    this.groupId = groupId;
    this.lastLoadedTimestamp = null; // Resetuj timestamp
    this.loadMessages();
    this.loadGroupMembers();
  }

  loadMessages(): void {
    const userId = this.authService.getUserId();
    this.chatService.getRecentOrAllMessages(this.groupId, userId).subscribe({
      next: (messages) => {
        this.messages = messages
          .map((message) => ({
            ...message,
            createdAt: new Date(message.createdAt),
          }))
          .sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
        if (this.messages.length > 0) {
          this.lastLoadedTimestamp = this.messages[this.messages.length - 1].createdAt.toISOString();
        }
        this.loadNewMessages();
      },
      error: (error) => console.error('Error loading messages:', error),
    });

    // Pretplata na WebSocket poruke ostaje nepromenjena
    if (!this.isSubscribedToMessages) {
      this.isSubscribedToMessages = true;
      this.chatService.subscribeToMessages(this.groupId).subscribe({
        next: (message) => {
          if (!this.messages.some((m) => m.id === message.id)) {
            message.createdAt = new Date(message.createdAt);
            this.messages.push(message);
            this.messages.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
          }
        },
        error: (error) => console.error('Error receiving message:', error),
      });
    }
  }

  loadNewMessages(): void {
    if (this.lastLoadedTimestamp) {
      this.chatService.loadMessagesAfter(this.groupId, this.lastLoadedTimestamp).subscribe({
        next: (newMessages) => {
          if (newMessages.length > 0) {
            this.messages.push(
              ...newMessages.map((message) => ({
                ...message,
                createdAt: new Date(message.createdAt),
              }))
            );
            this.lastLoadedTimestamp = newMessages[newMessages.length - 1].createdAt.toISOString();
          }
        },
        error: (error) => console.error('Error loading new messages:', error),
      });
    }
  }

  sendMessage(): void {
    if (this.message.trim() && this.groupId) {
      const messagePayload: ChatMessage = {
        senderId: this.authService.getUserId(),
        content: this.message,
        groupId: this.groupId,
        createdAt: new Date().toISOString(),
        type: 'CHAT',
      };

      this.chatService.sendMessageToApi(messagePayload).subscribe({
        next: () => {
          this.message = ''; // Resetuj unos poruke
        },
        error: (error) => console.error('Error sending message to API:', error),
      });
    } else {
      console.error('No group selected or message is empty.');
    }
  }

  loadGroupMembers(): void {
    this.chatService.getGroupMembers(this.groupId).subscribe({
      next: (members) => {
        this.groupMembers = members;
        console.log('Group members loaded:', this.groupMembers);
        this.checkIfCurrentUserIsAdmin();
      },
      error: (error) => console.error('Error loading group members:', error),
    });
  }

  checkIfCurrentUserIsAdmin(): void {
    const selectedGroup = this.userGroups.find((group) => group.id === this.groupId);
    if (selectedGroup) {
      this.isCurrentUserAdmin = selectedGroup.adminId === this.authService.getUserId();
    }
  }

  addMember(): void {
    if (this.newMemberEmail.trim()) {
      this.chatService.addMemberToGroupByEmail(this.groupId, this.newMemberEmail).subscribe({
        next: (data) => {
          console.log('Member added successfully:', data);
          this.loadGroupMembers(); // Osvježi članove grupe
          this.newMemberEmail = '';
        },
        error: (error) => console.error('Error adding member:', error),
      });
    }
  }

  removeMember(userId: number): void {
    if (this.isCurrentUserAdmin) {
      this.chatService.removeMemberFromGroup(this.groupId, userId).subscribe({
        next: (data) => {
          console.log('Member removed successfully:', data);
          this.loadGroupMembers();
        },
        error: (error) => console.error('Error removing member:', error),
      });
    } else {
      console.error('Only admins can remove members.');
    }
  }

  openCreateGroupModal(): void {
    this.showCreateGroupModal = true;
  }

  closeCreateGroupModal(): void {
    this.showCreateGroupModal = false;
    this.newGroupName = '';
  }

  createGroup(): void {
    if (this.newGroupName.trim()) {
      this.chatService.createGroup(this.newGroupName).subscribe({
        next: (group) => {
          this.userGroups.push(group); // Dodaj grupu u korisničke grupe
          this.selectGroup(group.id); // Postavi novu grupu kao aktivnu
          this.closeCreateGroupModal();
        },
        error: (error) => console.error('Error creating group:', error),
      });
    }
  }

}
