package ch.bader.budget.server.type;

import java.util.Optional;
import java.util.stream.Stream;

public class EnumUtil {

	public static <V extends ValueEnum<T>, T> V getEnumForValue(Class<V> clazz, int value) {
		Optional<V> type = Stream.of(clazz.getEnumConstants()).filter(i -> i.getValue().equals(value)).findFirst();
		return type.orElse(null);
	}

}
