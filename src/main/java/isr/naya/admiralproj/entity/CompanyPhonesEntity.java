package isr.naya.admiralproj.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "company_phones", schema = "public", catalog = "admiral")
public class CompanyPhonesEntity {
    private String telephone;

    @Basic
    @Column(name = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyPhonesEntity that = (CompanyPhonesEntity) o;

        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return telephone != null ? telephone.hashCode() : 0;
    }
}
