package ch.bader.budget.server.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface ValueEnum<T> {

	public T getValue();

	public String getName();

}
