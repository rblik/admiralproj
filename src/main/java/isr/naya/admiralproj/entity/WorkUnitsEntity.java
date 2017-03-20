package isr.naya.admiralproj.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by maximduhovniy on 20/03/2017.
 */
@Entity
@Table(name = "work_units", schema = "public", catalog = "admiral")
public class WorkUnitsEntity {
    private int id;
    private Timestamp start;
    private Timestamp finish;
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

        WorkUnitsEntity that = (WorkUnitsEntity) o;

        if (id != that.id) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (finish != null ? !finish.equals(that.finish) : that.finish != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (approved != null ? !approved.equals(that.approved) : that.approved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (finish != null ? finish.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (approved != null ? approved.hashCode() : 0);
        return result;
    }
}
