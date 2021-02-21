package egs.task.models.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendCodeTextDto {
    private String text;
    private String subject;

    public SendCodeTextDto(String text, String subject) {
        this.text =text;
        this.subject =subject;
    }
}
