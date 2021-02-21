package egs.task.facade.book;

import egs.task.models.entities.Book;
import egs.task.models.entities.User;
import egs.task.utils.FindUser;
import egs.task.utils.RoleConstants;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BookDeletingDtoFacade {

    @Lazy
    private final FindUser findUser;
    @Lazy
    private final BookUtil bookUtil;

    public BookDeletingDtoFacade(FindUser findUser, BookUtil bookUtil) {
        this.findUser = findUser;
        this.bookUtil = bookUtil;
    }

    public Boolean deleteImage(Long bookId, Authentication authentication) throws Exception {
        Book book = mainLogicForDelete(bookId, authentication);
        book.setCoverPhoto(null);
        return true;
    }

    public Boolean deleteBook(Long id, Authentication authentication) throws Exception {
        Book book = mainLogicForDelete(id, authentication);
        book.setHidden(true);
        return true;
    }

    private Book mainLogicForDelete(Long id, Authentication authentication) throws Exception {
        User user = findUser.findUserByToken(authentication);
        Book book = bookUtil.getBook(id);
        if (user.getRole().getRoleName().equals(RoleConstants.USER_NAME)
                && !book.getUser().getId().equals(user.getId())) {
            throw new Exception("You do not have permission.");
        }
        return book;
    }
}
