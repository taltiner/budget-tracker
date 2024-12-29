import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UebersichtDiagrammComponent } from './uebersicht-diagramm.component';

describe('UebersichtDiagrammComponent', () => {
  let component: UebersichtDiagrammComponent;
  let fixture: ComponentFixture<UebersichtDiagrammComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UebersichtDiagrammComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UebersichtDiagrammComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
