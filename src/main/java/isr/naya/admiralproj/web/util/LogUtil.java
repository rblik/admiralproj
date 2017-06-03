package isr.naya.admiralproj.web.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtil {

    public static String logMessage(String type, String contentType, Optional<Integer> employeeId, Optional<Integer> departmentId) {
        return logMessage(type, contentType, employeeId, departmentId, Optional.empty(), Optional.empty());
    }

    public static String logMessage(String type, String contentType, Optional<Integer> employeeId, Optional<Integer> departmentId, Optional<Integer> projectId, Optional<Integer> clientId) {
        return "Admin {} is creating " + contentType + " " + type + " report from {} to {}" +
                (employeeId.isPresent() ? " for employee (id = {})" : (departmentId.isPresent() ? " for department (id = {})" : "")) +
                (projectId.isPresent() ? " and project (id = {})" : (clientId.isPresent() ? " and client (id = {})" : ""));
    }
}
