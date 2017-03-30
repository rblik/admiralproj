package isr.naya.admiralproj.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationUtil {

    public static boolean checkTimeOverlap(Integer count) {

        if (count == null)
            return false;
        if (count != 0) {
            throw new TimeOverlappingException("There is already a record for this period of time.");
        } else
            return true;
    }

    public static <T> T checkNotFound(T obj, Integer id, Class<T> clazz) {
        checkNotFound(obj != null, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id);
        return obj;
    }

    private static void checkNotFound(boolean found, String message) {
        if (!found) throw new NotFoundException("Not found " + message);
    }
}
