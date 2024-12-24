export interface User {
  id: number;                    // ID korisnika
  email: string;                 // Email adresa
  username: string;              // Korisničko ime
  firstName: string;             // Ime
  lastName: string;              // Prezime
  address?: string;              // Adresa (opciono)
  isActive: boolean;             // Da li je korisnik aktivan (enabled)
  registeredAt: Date;            // Datum registracije (lastLoginDate)
  role: string;                  // Uloga korisnika
  postCount: number;             // Broj objava
  followingCount: number;        // Broj korisnika koje prati
  followerCount: number;         // Broj pratilaca
  sentMessageIds: number[];      // Lista ID-eva poslatih poruka
  receivedMessageIds: number[];  // Lista ID-eva primljenih poruka
  groupIds: number[];            // Lista ID-eva grupa u kojima je korisnik član
}
