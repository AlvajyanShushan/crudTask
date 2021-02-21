package egs.task.models.dtos.book;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookSearchDto {
    private String text;
    private List<Integer> statusList;
}