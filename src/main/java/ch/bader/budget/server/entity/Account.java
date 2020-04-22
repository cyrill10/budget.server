package ch.bader.budget.server.entity;

import java.time.LocalDateTime;

public interface Account<T extends Account<?>> extends Comparable<T> {

	public float getBalance(LocalDateTime date);
}
