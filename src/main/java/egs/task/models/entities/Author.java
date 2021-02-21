package egs.task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import egs.task.models.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Author")
public class Author extends AbstractEntity {
    @Lob
    @Basic
    @Nationalized
    @Column(name = "FirstName")
    private String firstName;

    @Lob
    @Basic
    @Nationalized
    @Column(name = "LastName")
    private String lastName;

    @Lob
    @Basic
    @Nationalized
    @Column(name = "Patronymic")
    private String patronymic;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "AuthorBook", joinColumns = @JoinColumn(name = "AuthorId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "BookId", referencedColumnName = "id"))
    @JsonIgnore
    private List<Book> books;
}
