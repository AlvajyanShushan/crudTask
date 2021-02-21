package egs.task.utils;

import egs.task.models.dtos.user.SendCodeTextDto;

public class AppConstants {

    public static String EMAIL_IS_WRONG(String languageName) {
        return switch (languageName) {
            case "ru" -> "Адрес электронной почты неправильный";
            case "hy" -> "Էլ.փոստը սխալ է";
            default -> "Email is wrong";
        };
    }

    public static String PHONE_NUMBER_IS_WRONG(String languageName) {
        return switch (languageName) {
            case "ru" -> "Неверный номер телефона";
            case "hy" -> "Հեռախոսահամարը սխալ է";
            default -> "PhoneNumber is wrong";
        };
    }

    public static String NOT_REGISTERED(String languageName) {
        return switch (languageName) {
            case "ru" -> "Вы должны зарегистрироваться";
            case "hy" -> "Դուք պետք է գրանցվեք";
            default -> "You must register";
        };
    }

    public static String COD_IS_WRONG(String languageName) {
        return switch (languageName) {
            case "ru" -> "Неверный код подтверждения";
            case "hy" -> "Հաստատման կոդը սխալ է";
            default -> "Incorrect verification code";
        };
    }

    public static SendCodeTextDto SEND_CODE_MESSAGE(String languageName) {
        String subject;
        String text;
        switch (languageName) {
            case "ru" -> {
                text = "Ваш проверочный код ";
                subject = "Проверяющий код";
            }
            case "hy" -> {
                text = "Ձեր հաստատման կոդն է ";
                subject = "Հաստատման կոդ";
            }
            default -> {
                text = "Your verification code is ";
                subject = "Verification code";
            }
        }
        return new SendCodeTextDto(text, subject);
    }

    public static String USERNAME_OR_PASSWORD_IS_INCORRECT(String languageName) {
        return switch (languageName) {
            case "ru" -> "Неверное имя пользователя или пароль";
            case "hy" -> "Մուտքանունը կամ գաղտնաբառը սխալ է";
            default -> "Incorrect username or password";
        };
    }

    public static String ADD_BOOK(String languageName) {
        String successMessage = switch (languageName) {
            case "hy" -> "Գիրքը հաջողությամբ պահպանվեց:";
            case "ru" -> "Книга успешно сохранена.";
            default -> "Book was saved successfully.";
        };
        return successMessage;
    }
}
