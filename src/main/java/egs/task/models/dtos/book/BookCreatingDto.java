package egs.task.models.dtos.book;

import egs.task.models.dtos.author.AuthorDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class BookCreatingDto {
    @NotNull
    @Size(min = 2, message = "Title should have at least 2 characters.")
    private String title;
    private String description;
    private Long publishedDate;
    private int pageCount;
    private List<AuthorDto> authorDtoList;
}