package isr.naya.admiralproj.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "work_agreements", schema = "public", catalog = "admiral")
public class WorkAgreements {
    private int id;
    private String tariffType;
    private Integer tariffAmount;
    private int employeeId;
    private int projectId;
    private Timestamp since;
    private Timestamp until;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tariff_type")
    public String getTariffType() {
        return tariffType;
    }

    public void setTariffType(String tariffType) {
        this.tariffType = tariffType;
    }

    @Basic
    @Column(name = "tariff_amount")
    public Integer getTariffAmount() {
        return tariffAmount;
    }

    public void setTariffAmount(Integer tariffAmount) {
        this.tariffAmount = tariffAmount;
    }

    @Basic
    @Column(name = "employee_id")
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "project_id")
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Basic
    @Column(name = "since")
    public Timestamp getSince() {
        return since;
    }

    public void setSince(Timestamp since) {
        this.since = since;
    }

    @Basic
    @Column(name = "until")
    public Timestamp getUntil() {
        return until;
    }

    public void setUntil(Timestamp until) {
        this.until = until;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkAgreements that = (WorkAgreements) o;

        if (id != that.id) return false;
        if (employeeId != that.employeeId) return false;
        if (projectId != that.projectId) return false;
        if (tariffType != null ? !tariffType.equals(that.tariffType) : that.tariffType != null) return false;
        if (tariffAmount != null ? !tariffAmount.equals(that.tariffAmount) : that.tariffAmount != null) return false;
        if (since != null ? !since.equals(that.since) : that.since != null) return false;
        if (until != null ? !until.equals(that.until) : that.until != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (tariffType != null ? tariffType.hashCode() : 0);
        result = 31 * result + (tariffAmount != null ? tariffAmount.hashCode() : 0);
        result = 31 * result + employeeId;
        result = 31 * result + projectId;
        result = 31 * result + (since != null ? since.hashCode() : 0);
        result = 31 * result + (until != null ? until.hashCode() : 0);
        return result;
    }
}
