package egs.task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import egs.task.enums.BookStatus;
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
@Table(name = "Book")
public class Book extends AbstractEntity {

    @Lob
    @Basic
    @Nationalized
    @Column(name = "Title")
    private String title;

    @Lob
    @Basic
    @Nationalized
    @Column(name = "Description")
    private String description;

    @Column(name = "PublishedDate")
    private Long publishedDate;

    @Column(name = "PageCount")
    private int pageCount;

    @Column(name = "coverPhoto")
    private String coverPhoto;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "BookStatus")
    private BookStatus bookStatus;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AuthorBook", joinColumns = @JoinColumn(name = "BookId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "AuthorId", referencedColumnName = "id"))
    @JsonIgnore
    private List<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    @JsonIgnore
    private User user;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<Comment> comments;
}