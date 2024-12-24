import { Component, OnInit } from '@angular/core';
import { GroupChatService} from '../groupChat.service';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-group-chat',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './group-chat.component.html',
  styleUrls: ['./group-chat.component.css'],
})
export class GroupChatComponent implements OnInit {
  groupName = '';
  adminId = 1; // Podesiti iz autentikacije
  groups: any[] = [];

  constructor(private groupChatService: GroupChatService) {}

  ngOnInit(): void {
    // Inicijalno učitavanje grupa (po potrebi)
  }

  createGroup(): void {
    this.groupChatService.createGroupChat(this.groupName, this.adminId).subscribe((group) => {
      this.groups.push(group);
      this.groupName = '';
    });
  }

  addMember(groupId: number, userId: number): void {
    this.groupChatService.addMember(groupId, userId).subscribe((group) => {
      console.log('Member added:', group);
    });
  }

  removeMember(groupId: number, userId: number): void {
    this.groupChatService.removeMember(groupId, userId).subscribe((group) => {
      console.log('Member removed:', group);
    });
  }
}
