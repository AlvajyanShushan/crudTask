package egs.task.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> implements Serializable {
    private Boolean success;
    private T data;
    private String message;
}