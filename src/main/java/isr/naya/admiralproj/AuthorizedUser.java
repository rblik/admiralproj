package isr.naya.admiralproj;

import isr.naya.admiralproj.model.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.util.Objects.requireNonNull;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private Integer id;
    private String name;
    private String surname;

    public AuthorizedUser(Employee employee) {
        super(employee.getEmail(), employee.getPassword(), employee.isEnabled(), true, true, true, employee.getRoles());
        this.id = employee.getId();
        this.name = employee.getName();
        this.surname = employee.getSurname();
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static Integer id() {
        return get().id;
    }

    public static String fullName() {
        AuthorizedUser user = get();
        return user.getFullName();
    }

    public String getFullName() {
        return String.format("%s %s", this.name, this.surname);
    }
}
