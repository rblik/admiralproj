package isr.naya.admiralproj.entity;

import javax.persistence.*;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "clients", schema = "public", catalog = "admiral")
public class ClientsEntity {
    private int id;
    private int companyNumber;
    private String name;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "company_number")
    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientsEntity that = (ClientsEntity) o;

        if (id != that.id) return false;
        if (companyNumber != that.companyNumber) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + companyNumber;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
