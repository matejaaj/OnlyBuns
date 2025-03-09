import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NearbyPostsComponent } from './nearby-posts.component';

describe('NearbyPostsComponent', () => {
  let component: NearbyPostsComponent;
  let fixture: ComponentFixture<NearbyPostsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NearbyPostsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NearbyPostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
