package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.CommentMapper;
import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.repository.CommentRepository;
import explorewithme.ewm.events.dto.EventMiniDto;
import explorewithme.ewm.events.mappers.EventMapper;
import explorewithme.ewm.requests.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        return commentRepository.countCommentById(eventId);
    }

    @Override
    public Map<Long,Long> getCommetnsByEvent(List<Long> eventsIds){
        Map<Long, Long> map = commentRepository.findCommentsByEventIn(eventsIds).stream()
                .map(comment -> comment.getEvent())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return map;
    }



}
