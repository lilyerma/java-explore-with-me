package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.dto.CommentDtoForLists;
import explorewithme.ewm.comments.model.Comment;
import explorewithme.ewm.comments.repository.CommentRepository;
import explorewithme.ewm.events.State;
import explorewithme.ewm.requests.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service // Service to avoid cyclic depependency, returns count of comments fot EventService
@RequiredArgsConstructor
public class UtilCommentServiceImpl implements UtilCommentService {


    private final CommentRepository commentRepository;

    @Override
    public int countOfCommentsforEvent(long eventId) {

        return commentRepository.countCommentsByEventEqualsAndStateEquals(eventId, State.PUBLISHED);

    }



    @Override
    public Map<Long,Long> getCommetnsByEvent(List<Long> eventsIds){

        Map<Long, Long> map = commentRepository.findCommentsByEventInAndStateEquals(eventsIds, State.PUBLISHED).stream()
                .map(comment -> comment.getEvent())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return map;
    }


}
