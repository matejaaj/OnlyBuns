import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private socket: WebSocket | null = null;
  private messageSubject: Subject<any> = new Subject();
  private notificationSocket: Subject<any> = new Subject();

  constructor() {}

  onUserNotification(): Observable<any> {
    return this.notificationSocket.asObservable(); // Pretplata na sve poruke sa WebSocket-a
  }
  connect(): void {
    if (this.socket && (this.socket.readyState === WebSocket.OPEN || this.socket.readyState === WebSocket.CONNECTING)) {
      console.warn('WebSocket is already open or connecting');
      return;
    }

    const token = localStorage.getItem('jwt');
    const wsUrl = `ws://localhost:8080/api/chat?token=${token}`;


    this.socket = new WebSocket(wsUrl);

    this.socket.onopen = () => {
      console.log('WebSocket connection established');
    };

    this.socket.onmessage = (event) => {
      try {
        const message = JSON.parse(event.data);

        if (message.type === 'REMOVED_FROM_GROUP') {
          this.notificationSocket.next(message); // Emituj obaveštenje
        }

        // Emitovanje za sve ostale poruke
        this.messageSubject.next(message);
      } catch (error) {
        console.error('Error parsing WebSocket message:', error, event.data);
      }
    };

    this.socket.onerror = (error) => {
      console.error('WebSocket error occurred:', error);
    };

    this.socket.onclose = (event) => {
      console.log('WebSocket connection closed:', event);
      setTimeout(() => this.reconnect(), 5000); // Attempt to reconnect after 5 seconds
    };
  }

  private reconnect(): void {
    console.log('Attempting to reconnect to WebSocket...');
    this.connect();
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
      console.log('WebSocket connection closed manually');
    }
  }

  subscribeToMessages(groupId: number): Observable<any> {
    return new Observable((observer) => {
      const subscription = this.messageSubject.asObservable().subscribe((message) => {
        if (message.groupId === groupId) {
          observer.next(message);
        }
      });

      return () => {
        subscription.unsubscribe();
      };
    });
  }

  sendMessage(groupId: number, message: any): void {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      const payload = {
        ...message,
        groupId,
      };
      this.socket.send(JSON.stringify(payload));
    } else if (this.socket && this.socket.readyState === WebSocket.CONNECTING) {
      console.warn('WebSocket is still connecting. Retrying...');
      setTimeout(() => this.sendMessage(groupId, message), 1000); // Retry after 1 second
    } else {
      console.error('WebSocket is not open. Unable to send message.');
      this.reconnect();
    }
  }

  createGroup(name: string): Observable<any> {
    const apiUrl = `http://localhost:8080/api/groups/create`;
    const adminId = localStorage.getItem('userId');
    const groupPayload = { name, adminId };

    return new Observable((observer) => {
      fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
        body: JSON.stringify(groupPayload),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error creating group: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          console.error('Error creating group:', error);
          observer.error(error);
        });
    });
  }
  getRecentOrAllMessages(groupId: number, userId: number): Observable<any[]> {
    const apiUrl = `http://localhost:8080/api/messages/${groupId}/recent-messages?userId=${userId}`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error loading messages: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          console.error('Error loading messages:', error);
          observer.error(error);
        });
    });
  }

  loadMessagesAfter(groupId: number, afterTimestamp: string): Observable<any[]> {
    const apiUrl = `http://localhost:8080/api/messages/${groupId}/messages-after?afterTimestamp=${afterTimestamp}`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error loading messages after: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          observer.error(error);
        });
    });
  }

  sendMessageToApi(message: any): Observable<any> {
    const apiUrl = `http://localhost:8080/api/messages`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
        body: JSON.stringify(message),
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error sending message: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          console.error('Error sending message to API:', error);
          observer.error(error);
        });
    });
  }

  addMemberToGroupByEmail(groupId: number, email: string): Observable<any> {
    const apiUrl = `http://localhost:8080/api/groups/${groupId}/members`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
        body: JSON.stringify({ email }), // Slanje email-a
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error adding member: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          observer.error(error);
        });
    });
  }


  removeMemberFromGroup(groupId: number, userId: number): Observable<any> {
    const apiUrl = `http://localhost:8080/api/groups/${groupId}/members/${userId}`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error removing member: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          observer.error(error);
        });
    });
  }

  getGroupMembers(groupId: number): Observable<any[]> {
    const apiUrl = `http://localhost:8080/api/groups/${groupId}/members`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error loading members: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          observer.error(error);
        });
    });
  }

  getUserGroups(userId: number): Observable<any[]> {
    const apiUrl = `http://localhost:8080/api/user/${userId}/groups`;
    return new Observable((observer) => {
      fetch(apiUrl, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwt')}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`Error loading user groups: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          observer.error(error);
        });
    });
  }
}
