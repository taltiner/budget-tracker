import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransaktionUebersichtComponent } from './transaktion-uebersicht.component';

describe('TransaktionUebersichtComponent', () => {
  let component: TransaktionUebersichtComponent;
  let fixture: ComponentFixture<TransaktionUebersichtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransaktionUebersichtComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TransaktionUebersichtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
