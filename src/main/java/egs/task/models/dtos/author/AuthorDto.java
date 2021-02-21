package egs.task.models.dtos.author;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDto {
    private String firstName;
    private String lastName;
    private String patronymic;
}
