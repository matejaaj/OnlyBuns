import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  form: FormGroup;
  submitted = false;
  errorMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    // Inicijalizujemo formu za unos email-a i lozinke
    this.form = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  // Metoda koja se poziva prilikom slanja forme
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = null;

    if (this.form.invalid) {
      this.submitted = false;
      return;
    }

    const loginData = {
      email: this.form.get('email')?.value,
      password: this.form.get('password')?.value,
    };

    this.authService.login(loginData).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        // Check if error message is a plain text response
        if (typeof err.error === 'string') {
          this.errorMessage = err.error;  // Display plain text message directly
        } else if (err.error?.error) {
          this.errorMessage = err.error.error;  // Display JSON formatted error message if available
        } else {
          this.errorMessage = 'Login failed. Please check your credentials.';
        }
        this.submitted = false;
      },
    });
  }
}
