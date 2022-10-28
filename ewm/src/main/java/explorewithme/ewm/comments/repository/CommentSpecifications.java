package explorewithme.ewm.comments.repository;

import explorewithme.ewm.comments.model.Comment;
import explorewithme.ewm.search.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Transactional
public class CommentSpecifications  implements Specification<Comment> {


        public Specification<Comment> getSpecificationFromFilters(List<SearchCriteria> filter) {
            Specification<Comment> specification = createSpecification(filter.get(0));
            for (SearchCriteria input : filter) {
                specification.and(createSpecification(input));
            }
            return specification;
        }

        public Specification<Comment> createSpecification(SearchCriteria input) {
            switch (input.getOperator()){

                case EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(input.getKey()),
                                    castToRequiredType(root.get(input.getKey()).getJavaType(),
                                            input.getValue()));

                case NOT_EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.notEqual(root.get(input.getKey()),
                                    castToRequiredType(root.get(input.getKey()).getJavaType(),
                                            input.getValue()));
                //Only for LocalDateTime
                case GREATER_THAN:
                    log.debug("buiding specification for greater than for LocalDateTime");
                    Specification<Comment> specification = (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThan(root.get("created"), LocalDateTime.parse(input.getValue()));
                    return specification;
                //Only for LocalDateTime
                case LESS_THAN:
                    log.debug("buiding specification for less than for LocalDateTime");
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThan(root.get("created"), LocalDateTime.parse(input.getValue()));
                //Only for text
                case LIKE:
                    log.debug("buiding specification for search by text in annotation, description and title");
                    return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("text")), "%" + input.getValue() + "%"));
                case IN:
                    log.debug("buiding specification to search in lists of Strings, Enums and long");
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.in(root.get(input.getKey()))
                                    .value(castToRequiredType(
                                            root.get(input.getKey()).getJavaType(),
                                            input.getValues()));

                default:
                    throw new RuntimeException("Operation not supported yet");
            }
        }


        private static Object castToRequiredType(Class fieldType, String value) {
            log.debug("Casting class for value parameters for specification builder");
            if(fieldType.isAssignableFrom(Long.class)) {
                log.debug("Casting class - Long");
                return Long.valueOf(value);
            } else if(fieldType.isAssignableFrom(LocalDateTime.class)) {
                log.debug("Casting class - LocalDateTime");
                return LocalDateTime.parse(value);
            } else if(Enum.class.isAssignableFrom(fieldType)) {
                log.debug("Casting class - Enum");
                return Enum.valueOf(fieldType, value);
            }
            return null;
        }

        private Object castToRequiredType(Class fieldType, List<String> value) {
            List<Object> lists = new ArrayList<>();
            for (String s : value) {
                lists.add(castToRequiredType(fieldType, s));
            }
            return lists;
        }

        private static String getContainsLikePattern(String searchTerm) {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return "%";
            }
            else {
                return "%" + searchTerm.toLowerCase() + "%";
            }
        }

    @Override
    public Specification<Comment> and(Specification<Comment> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Comment> or(Specification<Comment> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}

