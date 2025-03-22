package croissantnova.sanitydim.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

public class ListHelper {

    public static <T> Optional<T> getLast(@NotNull List<T> list) {
        try {
            return Optional.of(list.get(list.size() - 1));
        }
        catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public static <T> @Unmodifiable @NotNull List<T> getLastAsList(@NotNull List<T> list) {
        return list.isEmpty() ? List.of() : List.of(list.get(list.size() - 1));
    }

    public static <T> @Unmodifiable @NotNull List<T> getLastAsList(T @NotNull [] array) {
        return array.length == 0 ? List.of() : List.of(array[array.length - 1]);
    }
}
