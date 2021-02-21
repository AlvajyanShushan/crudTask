package egs.task.models.dtos.user;

import com.google.common.base.Strings;
import egs.task.models.entities.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

public class UserSpecification {

    public static Specification<User> searchUser(UserSearchDto userSearchDto) {
        return Specification.where((Specification<User>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate hidden = criteriaBuilder.equal(root.get("hidden"), false);
            Predicate user = criteriaBuilder.equal(root.join("role", JoinType.LEFT).get("roleName"), "ROLE_USER");
            hidden = criteriaBuilder.and(hidden, user);
            if (!Strings.isNullOrEmpty(userSearchDto.getSearchText())) {
                Predicate firstName = criteriaBuilder.like(root.get("firstName"), "%" + userSearchDto.getSearchText() + "%");
                Predicate lastName = criteriaBuilder.like(root.get("lastName"), "%" + userSearchDto.getSearchText() + "%");
                Predicate phone = criteriaBuilder.like(root.get("phoneNumber"), "%" + userSearchDto.getSearchText() + "%");
                Predicate email = criteriaBuilder.like(root.get("email"), "%" + userSearchDto.getSearchText() + "%");
                hidden = criteriaBuilder.and(hidden, criteriaBuilder.or(phone, firstName, lastName, email));
            }
            return hidden;
        });
    }
}