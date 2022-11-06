package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface UtilCommentService {

    int countOfCommentsforEvent(long eventId);

    Map<Long, Long> getCommetnsByEvent (List<Long> eventsId);
}
