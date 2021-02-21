package egs.task.models.dtos.book;

import egs.task.models.dtos.author.AuthorDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookPreviewDto {
    private Long id;
    private String title;
    private String description;
    private String coverPhoto;
    private Long publishedDate;
    private int pageCount;
    private int bookStatusValue;
    private List<AuthorDto> authorDtoList;
}