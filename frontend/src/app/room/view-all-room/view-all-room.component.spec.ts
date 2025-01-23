import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllRoomComponent } from './view-all-room.component';

describe('ViewAllRoomComponent', () => {
  let component: ViewAllRoomComponent;
  let fixture: ComponentFixture<ViewAllRoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewAllRoomComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewAllRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
