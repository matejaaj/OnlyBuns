import { Component, OnInit } from '@angular/core';
import { ChatService } from '../chat.service';
import { AuthService} from '../../../infrastructure/auth/auth.service';
import { FormsModule } from '@angular/forms';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {ChatMessage} from '../../../shared/model/chatMessage';

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
  groupId = 1; // Aktivni grupni čat
  newGroupName = ''; // Ime nove grupe
  showCreateGroupModal = false; // Kontrola prikaza modala za kreiranje grupe

  constructor(private chatService: ChatService, private authService: AuthService) {
    console.log('ChatService initialized: ', chatService)
  }

  ngOnInit(): void {
    // Učitaj poruke za trenutnu grupu
    this.chatService.loadMessages(this.groupId).subscribe({
      next: (messages) => {
        this.messages = messages.map((message) => ({
          ...message,
          createdAt: new Date(message.createdAt), // Pretvorite string u datum
        }));
      },
      error: (error) => console.error('Error loading messages:', error),
    });

    // Pretplata na WebSocket poruke
    this.chatService.subscribeToMessages(this.groupId).subscribe({
      next: (message) => {
        console.log('Received WebSocket message:', message);
        message.createdtAt = new Date(message.createdAt);
        this.messages.push(message);
      },
      error: (error) => console.error('Error receiving message:', error),
    });
  }


  sendMessage(): void {
    if (this.message.trim()) {
      const messagePayload: ChatMessage = {
        senderId: this.authService.getUserId(), // Trenutni korisnik
        content: this.message,
        groupId: this.groupId, // Grupni čat
        createdAt: new Date().toISOString(),
        type: 'CHAT', // Default tip poruke
      };

      console.log('Attempting to send message to API:', messagePayload);

      // Prvo pošalji poruku REST API-ju
      this.chatService.sendMessageToApi(messagePayload).subscribe({
        next: (savedMessage) => {
          console.log('Message saved successfully:', savedMessage);

          // Emituj poruku putem WebSocket-a
          this.chatService.sendMessage(this.groupId, savedMessage);
          this.message = '';
        },
        error: (error) => {
          console.error('Error sending message to API:', error);
        },
      });
    }
  }
  // Otvori modal za kreiranje grupe
  openCreateGroupModal(): void {
    this.showCreateGroupModal = true;
  }

  // Zatvori modal za kreiranje grupe
  closeCreateGroupModal(): void {
    this.showCreateGroupModal = false;
    this.newGroupName = '';
  }

  // Kreiranje nove grupe
  createGroup(): void {
    if (this.newGroupName.trim()) {
      this.chatService.createGroup(this.newGroupName).subscribe((group) => {
        console.log('Group created:', group);
        this.groupId = group.id; // Postavi novi grupni čat kao aktivni
        this.messages = []; // Očisti poruke
        this.closeCreateGroupModal();
      });
    }
  }
}
