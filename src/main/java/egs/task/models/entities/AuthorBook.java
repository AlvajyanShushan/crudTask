package egs.task.models.entities;

import egs.task.models.AbstractEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AuthorBook")
public class AuthorBook extends AbstractEntity {
    @Column(name = "AuthorId")
    private Long authorId;

    @Column(name = "BookId")
    private Long bookId;
}