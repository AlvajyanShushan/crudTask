package egs.task.utils;

@org.springframework.context.annotation.Configuration
public class Configuration {
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGNING_KEY = "task_secret_key101520";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 365 * 24 * 60 * 60;

    public static final String BOOK_IMAGE_DIR_IN_PROJECT = "src/main/resources/medias/images/book/";
    public static final String USER_IMAGE_DIR_IN_PROJECT = "src/main/resources/medias/images/user/";

    public static final String bookPath = "http://localhost:8080/api/book/getImage/";
    public static final String userPath = "http://localhost:8080/api/user/getImage/";

}