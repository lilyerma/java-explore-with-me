package explorewithme.ewm.requests.services;

import explorewithme.ewm.requests.RequestRepository;
import explorewithme.ewm.requests.Status;
import explorewithme.ewm.requests.model.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service // Service to avoid cyclic dependecy with event service
@RequiredArgsConstructor
@Slf4j
public class UtilRequestServiceImpl implements UtilRequestService {

    @PersistenceContext
    private EntityManager em;
    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        cb = em.getCriteriaBuilder();
    }

    private final RequestRepository requestRepository;


    @Override
    public int getCountOfApproveRequest(long eventId) {
        log.debug("requesting count of confirmed requests from request repo");
        return requestRepository.countRequestByEventAndStatus(eventId);
    }

    @Override
    public boolean hasApproveRequests(long userId, long eventId) {
        log.debug("requesting request repo for approveed requests for the user and for the event");
        if (requestRepository.getRequestsByEventAndAndRequesterAndStatus(eventId, userId, Status.CONFIRMED) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Map<Long, Long> getConfirmedRequestsByEvents(List<Long> eventIds) {
        CriteriaQuery<Request> criteriaQuery =
                cb.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);

        criteriaQuery.select(root)
                .where(root.get("event")
                        .in(eventIds));

        List<Request> list = em.createQuery(criteriaQuery).getResultList();


        return list.stream()
                .map(request -> request.getEvent())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


}


