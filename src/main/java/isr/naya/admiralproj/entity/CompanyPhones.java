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
public class CompanyPhones {
    private int companyId;
    private String telephone;

    @Basic
    @Column(name = "company_id")
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

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

        CompanyPhones that = (CompanyPhones) o;

        if (companyId != that.companyId) return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = companyId;
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        return result;
    }
}
