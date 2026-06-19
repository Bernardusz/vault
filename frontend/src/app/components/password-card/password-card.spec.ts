import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordCard } from './password-card';

describe('PasswordCard', () => {
  let component: PasswordCard;
  let fixture: ComponentFixture<PasswordCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
