package isr.naya.admiralproj.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "work_units", schema = "public", catalog = "admiral")
public class WorkUnits {
    private int id;
    private Timestamp start;
    private Timestamp finish;
    private int workAgreementId;
    private String comment;
    private Boolean approved;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "start")
    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    @Basic
    @Column(name = "finish")
    public Timestamp getFinish() {
        return finish;
    }

    public void setFinish(Timestamp finish) {
        this.finish = finish;
    }

    @Basic
    @Column(name = "work_agreement_id")
    public int getWorkAgreementId() {
        return workAgreementId;
    }

    public void setWorkAgreementId(int workAgreementId) {
        this.workAgreementId = workAgreementId;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "approved")
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkUnits workUnits = (WorkUnits) o;

        if (id != workUnits.id) return false;
        if (workAgreementId != workUnits.workAgreementId) return false;
        if (start != null ? !start.equals(workUnits.start) : workUnits.start != null) return false;
        if (finish != null ? !finish.equals(workUnits.finish) : workUnits.finish != null) return false;
        if (comment != null ? !comment.equals(workUnits.comment) : workUnits.comment != null) return false;
        if (approved != null ? !approved.equals(workUnits.approved) : workUnits.approved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (finish != null ? finish.hashCode() : 0);
        result = 31 * result + workAgreementId;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (approved != null ? approved.hashCode() : 0);
        return result;
    }
}
