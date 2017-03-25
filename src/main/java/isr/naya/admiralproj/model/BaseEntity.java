package isr.naya.admiralproj.model;

import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Data
@MappedSuperclass
public class BaseEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}