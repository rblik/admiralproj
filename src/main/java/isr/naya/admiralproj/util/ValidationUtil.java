package isr.naya.admiralproj.util;

import isr.naya.admiralproj.exception.*;
import isr.naya.admiralproj.model.DateLock;
import isr.naya.admiralproj.model.WorkUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Month;
import java.util.List;

import static isr.naya.admiralproj.web.security.password.PasswordUtil.isMatch;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static WorkUnit checkTimeRange(WorkUnit workUnit){
        if (workUnit.getStart().isAfter(workUnit.getFinish())) {
            throw new TimeRangeException("Start has to be before finish");
        }
        return workUnit;
    }

    public static boolean checkTimeOverlap(@NonNull Integer count) {

        if (count != 0) {
            throw new TimeOverlappingException("There is already a record for this period of time.");
        } else
            return true;
    }

    public static WorkUnit checkLock(WorkUnit unit, List<DateLock> lock) {
        if (lock != null && !lock.isEmpty()) throw new DateLockedException("This date is locked");
        else return unit;
    }

    public static <T> void checkNotFound(Integer obj, Integer id, Integer employeeId, Class<T> clazz) {
        checkNotFound(obj != 0, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id + " for user with ID " + employeeId);
    }
    public static <T> void checkNotFound(Integer obj, Integer year, Integer month, Integer employeeId, Class<T> clazz) {
        checkNotFound(obj != 0, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with year " + year + " and month " + Month.of(month) + " for user with ID " + employeeId);
    }

    public static <T> int checkNotFound(Integer obj, Integer id, Class<T> clazz) {
        checkNotFound(obj != 0, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id + " for this user");
        return obj;
    }

    public static <T> boolean isNotSamePass(String pass, String encodedDbPass) {
        if (!isMatch(pass, encodedDbPass)) {
        return true;
        } else {
            throw new SamePasswordException("Same pass");
        }
    }

    public static <T> T checkNotFound(T obj, Integer id, Class<T> clazz) {
        checkNotFound(obj != null, clazz.getSimpleName().replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase() + " with ID " + id);
        return obj;
    }

    private static void checkNotFound(boolean found, String message) {
        if (!found) throw new NotFoundException("Not found " + message);
    }
}
