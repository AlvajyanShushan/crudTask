package egs.task.utils;

import java.util.Date;
import java.util.Random;

public class GenerateCodeUtil {
    public static String generateCode(int length) {
        String randomNumber = String.valueOf((new Date()).getTime() + Math.abs(new Random().nextInt()));
        StringBuilder stringBuilder = new StringBuilder(randomNumber);
        stringBuilder.reverse();
        String code = stringBuilder.toString();
        if (stringBuilder.length() > length)
            code = code.substring(0, length);
        return code;
    }
}
