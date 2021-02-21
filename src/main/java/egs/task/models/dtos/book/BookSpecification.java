package egs.task.models.dtos.book;

import com.google.common.base.Strings;
import egs.task.enums.BookStatus;
import egs.task.models.entities.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

public class BookSpecification {
    public static Specification<Book> searchAdminBooks(BookSearchDto bookSearchDto) {
        return Specification.where((Specification<Book>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate hidden = search(bookSearchDto).toPredicate(root, criteriaQuery, criteriaBuilder);
            if (bookSearchDto.getStatusList() != null && bookSearchDto.getStatusList().size() > 0) {
                List<Integer> collect = bookSearchDto.getStatusList().stream().map(a -> a - 1).collect(Collectors.toList());
                Predicate status = root.get("bookStatus").in(collect);
                for (Integer statusValue : bookSearchDto.getStatusList()) {
                    if (BookStatus.valueOf(statusValue).isPresent()) {
                        hidden = criteriaBuilder.and(hidden, status);
                    }
                }
            }
            criteriaQuery.distinct(true);
            return hidden;
        });
    }

    public static Specification<Book> searchUserBooks(BookSearchDto bookSearchDto, Long userId) {
        return Specification.where((Specification<Book>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate hidden = search(bookSearchDto).toPredicate(root, criteriaQuery, criteriaBuilder);
            Predicate status = criteriaBuilder.equal(root.get("bookStatus"), 0);
            Predicate bookAddedByUser = criteriaBuilder.equal(root.join("user", JoinType.LEFT).get("id"), userId);
            hidden = criteriaBuilder.and(hidden, criteriaBuilder.or(status, bookAddedByUser));
            return hidden;
        });
    }

    public static Specification<Book> search(BookSearchDto bookSearchDto) {
        return Specification.where((Specification<Book>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate hidden = criteriaBuilder.equal(root.get("hidden"), false);
            if (!Strings.isNullOrEmpty(bookSearchDto.getText())) {
                Predicate title = criteriaBuilder.like(root.get("title"), "%" + bookSearchDto.getText() + "%");
                Predicate description = criteriaBuilder.like(root.get("description"), "%" + bookSearchDto.getText() + "%");
                hidden = criteriaBuilder.and(hidden, criteriaBuilder.or(title, description));
            }
            return hidden;
        });
    }

}
