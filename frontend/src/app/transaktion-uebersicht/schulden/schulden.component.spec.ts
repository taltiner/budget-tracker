import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchuldenComponent } from './schulden.component';

describe('SchuldenComponent', () => {
  let component: SchuldenComponent;
  let fixture: ComponentFixture<SchuldenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchuldenComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchuldenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
