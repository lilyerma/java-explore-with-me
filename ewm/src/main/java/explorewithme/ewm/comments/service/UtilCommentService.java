package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.repository.CommentRepository;
import org.springframework.stereotype.Service;


public interface UtilCommentService {

    int countOfCommentsforEvent(long eventId);

}
