package isr.naya.admiralproj.util;

import isr.naya.admiralproj.exception.NotFoundException;
import isr.naya.admiralproj.exception.TimeOverlappingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static boolean checkTimeOverlap(@NonNull Integer count) {

        if (count != 0) {
            throw new TimeOverlappingException("There is already a record for this period of time.");
        } else
            return true;
    }

    public static <T> void checkNotFound(Integer obj, Integer id, Integer employeeId, Class<T> clazz) {
        checkNotFound(obj != 0, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id + " for user with ID " + employeeId);
    }

    public static <T> void checkNotFound(Integer obj, Integer id, Class<T> clazz) {
        checkNotFound(obj != 0, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id + " for this user");
    }

    public static <T> T checkNotFound(T obj, Integer id, Class<T> clazz) {
        checkNotFound(obj != null, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id);
        return obj;
    }

    private static void checkNotFound(boolean found, String message) {
        if (!found) throw new NotFoundException("Not found " + message);
    }
}
