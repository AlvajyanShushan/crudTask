package egs.task.models.dtos.device;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDto {
    @NotNull
    private String deviceId;
    @NotNull
    private String deviceToken;
}
