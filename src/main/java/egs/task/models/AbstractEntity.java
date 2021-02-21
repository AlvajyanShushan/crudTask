package egs.task.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated entity id")
    @Column(name = "Id")
    private Long id;

    @CreatedBy
    @Column(name = "CreatedBy")
    private String createdBy;

    @CreatedDate
    @Column(name = "CreatedDate")
    private Long createdDate;

    @LastModifiedBy
    @Column(name = "LastModifiedBy")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LastModifiedDate")
    private Long lastModifiedDate;

    @Column(name = "Hidden",columnDefinition = "bit DEFAULT 0", nullable = false)
    private Boolean hidden = false;
}