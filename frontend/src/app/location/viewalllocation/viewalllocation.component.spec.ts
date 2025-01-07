import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewalllocationComponent } from './viewalllocation.component';

describe('ViewalllocationComponent', () => {
  let component: ViewalllocationComponent;
  let fixture: ComponentFixture<ViewalllocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewalllocationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewalllocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
