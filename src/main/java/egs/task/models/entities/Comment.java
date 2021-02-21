package egs.task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import egs.task.models.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comment")
public class Comment extends AbstractEntity {

    @Lob
    @Basic
    @Nationalized
    @Column(name = "Text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookId")
    @JsonIgnore
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    @JsonIgnore
    private User user;
}
