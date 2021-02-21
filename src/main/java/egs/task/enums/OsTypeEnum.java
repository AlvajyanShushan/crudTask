package egs.task.enums;

import java.util.Arrays;
import java.util.Optional;

public enum OsTypeEnum {
    ANDROID(1),
    IOS(2),
    WEB(3),
    ALL(4);

    private final int mValue;

    OsTypeEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return ordinal() + 1;
    }

    private static OsTypeEnum[] allValues = values();
    public static Optional<OsTypeEnum> valueOf(int value) {
        return Arrays.stream(values())
                .filter(osTypeEnum -> osTypeEnum.mValue == value)
                .findFirst();
    }
}
