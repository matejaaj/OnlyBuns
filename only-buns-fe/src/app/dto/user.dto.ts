export interface UserDTO {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  address?: string;
  isActive: boolean;
  registeredAt: Date;
  role: string;
}
