package isr.naya.admiralproj.model;

import com.fasterxml.jackson.annotation.JsonView;
import isr.naya.admiralproj.util.JsonUtil.AdminView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class BaseEntity implements Persistable<Integer> {

    @Id
    @JsonView(AdminView.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Override
    public boolean isNew() {
        return getId() == null;
    }
}