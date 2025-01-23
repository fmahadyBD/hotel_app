import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewallhotelComponent } from './viewallhotel.component';

describe('ViewallhotelComponent', () => {
  let component: ViewallhotelComponent;
  let fixture: ComponentFixture<ViewallhotelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewallhotelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewallhotelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
