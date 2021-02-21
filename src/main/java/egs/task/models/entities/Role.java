package egs.task.models.entities;

import egs.task.models.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Role")
public class Role extends AbstractEntity {

    @Lob
    @Basic
    @Nationalized
    @Column(name = "RoleName")
    private String roleName;

    @Column(name = "Description")
    private String description;
}