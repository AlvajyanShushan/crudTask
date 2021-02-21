package egs.task.enums;

import java.util.Arrays;
import java.util.Optional;

public enum BookStatus {
    APPROVED(1),
    NON_APPROVED(2);

    private final int mValue;

    BookStatus(int value) {
        mValue = value;
    }

    public int getValue() {
        return ordinal() + 1;
    }

    private static BookStatus[] allValues = values();
    public static Optional<BookStatus> valueOf(int value) {
        return Arrays.stream(values())
                .filter(osTypeEnum -> osTypeEnum.mValue == value)
                .findFirst();
    }
}
