import { Component, OnInit, NgZone } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../model/post';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { tileLayer, latLng, Map, Marker, marker, Icon } from 'leaflet';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  standalone: true,
  imports: [FormsModule, LeafletModule, HttpClientModule, CommonModule],
  styleUrls: ['./create-post.component.css'],
})
export class CreatePostComponent implements OnInit {
  post: Post = {
    id: 0,
    description: '',
    createdAt: new Date(),
    userId: 1,
    image: { path: '' },
    location: {
      country: '',
      city: '',
      address: '',
      number: 0,
      latitude: 44.8176,
      longitude: 20.4633,
    },
    likeCount: 0,
    comments: [],
    eligibleForAd: false,
  };
  selectedImage: File | null = null;

  // Opcije za Leaflet mapu
  options: any;
  map: Map | null = null;
  marker: Marker | null = null;
  imagePreviewUrl: string | null = null;

  constructor(
    private postService: PostService,
    private router: Router,
    private ngZone: NgZone,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.options = {
      layers: [
        tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          maxZoom: 18,
          attribution:
            'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
        }),
      ],
      zoom: 13,
      center: latLng(this.post.location.latitude, this.post.location.longitude),
    };
  }

  onMapReady(map: Map) {
    this.map = map;
    this.addMarker(this.post.location.latitude, this.post.location.longitude);

    map.on('click', (e: any) => {
      const lat = e.latlng.lat;
      const lng = e.latlng.lng;

      this.post.location.latitude = lat;
      this.post.location.longitude = lng;

      // Ukloni postojeći marker
      if (this.marker) {
        this.map!.removeLayer(this.marker);
      }
      // Dodaj marker na kliknutoj lokaciji
      this.addMarker(lat, lng);

      // Reverzno geokodiranje za dobijanje detalja adrese
      this.reverseGeocode(lat, lng);
    });
  }

  addMarker(lat: number, lng: number) {
    this.marker = marker([lat, lng], {
      icon: new Icon({
        iconUrl: 'assets/marker-icon.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
      }),
    }).addTo(this.map!);
  }

  onAddressChange() {
    this.geocodeAddress();
  }

  geocodeAddress() {
    const address = `${this.post.location.address} ${this.post.location.number}, ${this.post.location.city}, ${this.post.location.country}`;

    const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(
      address
    )}&format=json&limit=1`;

    this.http.get(url).subscribe(
      (data: any) => {
        if (data && data.length > 0) {
          const result = data[0];
          const lat = parseFloat(result.lat);
          const lon = parseFloat(result.lon);

          this.ngZone.run(() => {
            this.post.location.latitude = lat;
            this.post.location.longitude = lon;

            // Ažurirajte mapu i marker
            if (this.map) {
              this.map.setView([lat, lon], 13);

              // Ukloni postojeći marker
              if (this.marker) {
                this.map.removeLayer(this.marker);
              }

              // Dodaj novi marker
              this.addMarker(lat, lon);
            }
          });
        }
      },
      (error) => {
        console.error('Greška pri geokodiranju adrese:', error);
      }
    );
  }

  reverseGeocode(lat: number, lng: number) {
    const url = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}`;
    this.http.get(url).subscribe(
      (data: any) => {
        if (data && data.address) {
          this.ngZone.run(() => {
            this.post.location.country = data.address.country || '';
            this.post.location.city =
              data.address.city ||
              data.address.town ||
              data.address.village ||
              '';
            this.post.location.address = data.address.road || '';
            this.post.location.number = data.address.house_number
              ? parseInt(data.address.house_number)
              : 1;
          });
        }
      },
      (error) => {
        console.error('Greška pri reverznom geokodiranju:', error);
      }
    );
  }
  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedImage = input.files[0];

      // Ensure the selected file is an image
      if (this.selectedImage.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.imagePreviewUrl = e.target.result; // Set the preview URL to the base64 data URL
        };
        reader.readAsDataURL(this.selectedImage); // Read the file as a data URL
      } else {
        alert('Please upload a valid image file.');
        this.selectedImage = null;
      }
    }
  }

  createPost() {
    if (this.selectedImage && this.post.description) {
      this.postService.createPost(this.post, this.selectedImage).subscribe(
        (response) => {
          console.log('Post uspešno kreiran:', response);
          this.router.navigate(['/']);
        },
        (error) => {
          console.error('Greška pri kreiranju posta:', error);
        }
      );
    } else {
      alert('Molimo odaberite sliku i unesite opis.');
    }
  }
}
