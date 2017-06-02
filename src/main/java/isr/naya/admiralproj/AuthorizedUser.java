package isr.naya.admiralproj;

import isr.naya.admiralproj.model.Employee;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

public class AuthorizedUser extends User {

    @Getter
    private Integer id;
    private String name;
    private String surname;

    public AuthorizedUser(Employee employee) {
        super(employee.getEmail(), employee.getPassword(), employee.isEnabled(), true, true, true, employee.getRoles());
        this.id = employee.getId();
        this.name = employee.getName();
        this.surname = employee.getSurname();
    }

    public String getFullName() {
        return String.format("%s %s", this.name, this.surname);
    }
}
