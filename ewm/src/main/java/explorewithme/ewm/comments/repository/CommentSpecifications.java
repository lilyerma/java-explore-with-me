package explorewithme.ewm.comments.repository;

import explorewithme.ewm.comments.model.Comment;
import explorewithme.ewm.events.State;
import explorewithme.ewm.events.repository.SearchOperation;
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

import static explorewithme.ewm.search.SearchOperation.*;

@Slf4j
@Component
@Transactional
public class CommentSpecifications  implements Specification<Comment> {


    private List<SearchCriteria> list;

    public CommentSpecifications() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Specification<Comment> and(Specification<Comment> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Comment> or(Specification<Comment> other) {
        return Specification.super.or(other);
    }

    //This method requires work to accomodate arbitrary parameter types (Objects)
    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add criteria to predicate
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(GREATER_THAN)) { // Hardcoded for LocalDateTime for brevity
                predicates.add(builder.greaterThan(
                        root.get(criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())));
            } else if (criteria.getOperation().equals(LESS_THAN)) {
                predicates.add(builder.lessThan(  // Hardcoded for LocalDateTime for brevity
                        root.get(criteria.getKey()), LocalDateTime.parse(criteria.getValue().toString())));
            } else if (criteria.getOperation().equals(GREATER_THAN_EQUAL)) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(NOT_EQUAL)) {
                predicates.add(builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(LIKE)) {
                String search = String.valueOf(criteria.getValue()).substring(1,
                        String.valueOf(criteria.getValue()).length() - 1);
                predicates.add(builder.like(builder.lower(root.get("text")),
                          "%" + search + "%"));   //(String) criteria.getValue()
            } else if (criteria.getOperation().equals(IN)) {
                List<Predicate> toStore = new ArrayList<>();
                if (criteria.getType().equals("List<Long>")) {
                    List<Long> list = castTypeL(criteria.getValue().toString());
                    if (list.size() == 1) {
                        predicates.add(builder.equal(root.get(criteria.getKey()), list.get(0)));
                    } else {
                        for (Long id : list) {
                            toStore.add(builder.equal(root.get(criteria.getKey()), id));
                        }
                        predicates.add(builder.or(toStore.toArray(new Predicate[0])));
                    }
                } else if (criteria.getType().equals("List<String>")) {
                    List<State> list =  castTypeS(criteria.getValue().toString());
                    if (list.size() == 1) {
                        predicates.add(builder.equal(root.get(criteria.getKey()),list.get(0)));
                    } else {
                        for (State str : list) {
                            toStore.add(builder.equal(root.get(criteria.getKey()), str));
                        }
                        predicates.add(builder.or(toStore.toArray(new Predicate[0])));
                    }
                }
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    //Methods to cast lists to the correct type

    private static List<Long> castTypeL(String value) {
        String[] string = (value.substring(1, value.length() - 1)).split(",");
        List<Long> longList = new ArrayList<>();
        for (String element : string) {
            longList.add(Long.parseLong(element));
        }
        return longList;
    }

    private static List<State> castTypeS(String value) {
        String[] string = (value.substring(1, value.length() - 1)).split(",");
        List<State> stringList = new ArrayList<>();
        for (String element : string) {
            stringList.add(State.valueOf(element));
        }
        return stringList;
    }


}

