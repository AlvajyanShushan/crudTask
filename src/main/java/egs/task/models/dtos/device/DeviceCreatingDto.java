package egs.task.models.dtos.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCreatingDto {
    private String deviceId;
    private String deviceToken;
    private String modelName;
    private int osTypeId;
    private String languageName;
}
