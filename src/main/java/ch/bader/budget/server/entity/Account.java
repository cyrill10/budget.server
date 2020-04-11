package ch.bader.budget.server.entity;

import java.time.LocalDateTime;

public interface Account {

	public float getBalance(LocalDateTime date);

}
