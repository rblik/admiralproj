package isr.naya.admiralproj.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "employee_roles", schema = "public", catalog = "admiral")
public class EmployeeRoles {
    private int employeeId;
    private String role;

    @Basic
    @Column(name = "employee_id")
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeRoles that = (EmployeeRoles) o;

        if (employeeId != that.employeeId) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = employeeId;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
