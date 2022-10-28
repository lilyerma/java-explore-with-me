package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // Service to avoid cyclic depependency, returns count of comments fot EventService
@RequiredArgsConstructor
public class UtilCommentServiceImpl implements UtilCommentService {

    private final CommentRepository commentRepository;

    @Override
    public int countOfCommentsforEvent(long eventId) {

        return commentRepository.countCommentById(eventId);
    }
}
