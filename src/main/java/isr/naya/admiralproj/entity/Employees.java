package isr.naya.admiralproj.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
public class Employees {
    private int id;
    private String name;
    private String surname;
    private int passport;
    private Timestamp birthday;
    private String email;
    private Timestamp worksSince;
    private Boolean active;
    private String privatePhone;
    private String companyPhone;
    private String password;
    private int departmentId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "passport")
    public int getPassport() {
        return passport;
    }

    public void setPassport(int passport) {
        this.passport = passport;
    }

    @Basic
    @Column(name = "birthday")
    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "works_since")
    public Timestamp getWorksSince() {
        return worksSince;
    }

    public void setWorksSince(Timestamp worksSince) {
        this.worksSince = worksSince;
    }

    @Basic
    @Column(name = "active")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "private_phone")
    public String getPrivatePhone() {
        return privatePhone;
    }

    public void setPrivatePhone(String privatePhone) {
        this.privatePhone = privatePhone;
    }

    @Basic
    @Column(name = "company_phone")
    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "department_id")
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employees employees = (Employees) o;

        if (id != employees.id) return false;
        if (passport != employees.passport) return false;
        if (departmentId != employees.departmentId) return false;
        if (name != null ? !name.equals(employees.name) : employees.name != null) return false;
        if (surname != null ? !surname.equals(employees.surname) : employees.surname != null) return false;
        if (birthday != null ? !birthday.equals(employees.birthday) : employees.birthday != null) return false;
        if (email != null ? !email.equals(employees.email) : employees.email != null) return false;
        if (worksSince != null ? !worksSince.equals(employees.worksSince) : employees.worksSince != null) return false;
        if (active != null ? !active.equals(employees.active) : employees.active != null) return false;
        if (privatePhone != null ? !privatePhone.equals(employees.privatePhone) : employees.privatePhone != null)
            return false;
        if (companyPhone != null ? !companyPhone.equals(employees.companyPhone) : employees.companyPhone != null)
            return false;
        if (password != null ? !password.equals(employees.password) : employees.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + passport;
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (worksSince != null ? worksSince.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (privatePhone != null ? privatePhone.hashCode() : 0);
        result = 31 * result + (companyPhone != null ? companyPhone.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + departmentId;
        return result;
    }
}
