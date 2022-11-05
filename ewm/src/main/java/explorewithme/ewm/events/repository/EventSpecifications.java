package explorewithme.ewm.events.repository;

import explorewithme.ewm.events.model.Event;
import explorewithme.ewm.search.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class EventSpecifications implements Specification<Event> {

    private List<SearchCriteria> list;

    public EventSpecifications() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Specification<Event> and(Specification<Event> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Event> or(Specification<Event> other) {
        return Specification.super.or(other);
    }

    //This method requires work to accomodate arbitrary parameter types (Objects)
    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //add criteria to predicate
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) { // Hardcoded for LocalDateTime for brevity
                predicates.add(builder.greaterThan(
                        root.get("eventDate"), LocalDateTime.parse(criteria.getValue().toString())));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(  // Hardcoded for LocalDateTime for brevity
                        root.get("eventDate"), LocalDateTime.parse(criteria.getValue().toString())));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates.add(builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.LIKE)) {
                predicates.add(builder.or(
                        builder.like(builder.lower(root.get("title")), "%" + String.valueOf(criteria.getValue()) + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + String.valueOf(criteria.getValue()) + "%"),
                        builder.like(builder.lower(root.get("annotation")), "%" + String.valueOf(criteria.getValue()) + "%")));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                List<Predicate> toStore = new ArrayList<>();
                if (criteria.getType() == "List<Long>") {
                    List<Long> list = castTypeL(criteria.getValue().toString());
                    if (list.size() == 1) {
                        predicates.add(builder.equal(root.get(criteria.getKey()), list.get(0)));
                    } else {
                        for (Long id : list) {
                            toStore.add(builder.equal(root.get(criteria.getKey()), id));
                        }
                        predicates.add(builder.or(toStore.toArray(new Predicate[0])));
                    }
                } else if (criteria.getType() == "List<String") {
                    List<String> list = (List<String>) castTypeS(criteria.getValue().toString());
                    if (list.size() == 1) {
                        predicates.add(builder.equal(root.get(criteria.getKey()), list.get(0)));
                    } else {
                        for (String str : list) {
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

    private static List<String> castTypeS(String value) {
        String[] string = value.split(",");
        return Arrays.asList(string);
    }


}


