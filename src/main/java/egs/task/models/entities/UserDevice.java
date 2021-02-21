package egs.task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import egs.task.enums.OsTypeEnum;
import egs.task.models.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserDevice", uniqueConstraints = @UniqueConstraint(columnNames = {"DeviceId", "UserId"}))
public class UserDevice extends AbstractEntity {

    @Column(name = "DeviceId", unique = true)
    public String deviceId;

    @Column(name = "DeviceToken")
    public String deviceToken;

    @Column(name = "OsType")
    private OsTypeEnum osTypeEnum;

    @Column(name = "ModelName")
    private String modelName;

    @Column(name = "OsVersion")
    private String osVersion;

    @Column(name = "AppVersion")
    private String appVersion;

    @Column(name = "Language")
    private String languageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    @JsonIgnore
    private User user;
}