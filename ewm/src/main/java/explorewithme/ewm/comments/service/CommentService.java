package explorewithme.ewm.comments.service;

import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.dto.CommentDtoForLists;
import explorewithme.ewm.comments.dto.FullCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto postComment(long userId, long eventId, CommentDto commentDto);

    List<FullCommentDto> getCommentsByUser(long userId);

    FullCommentDto publishComment(long commentId);

    FullCommentDto rejectComment(long commentId);

    List<FullCommentDto> getCommentsForAdmin(Long[] users, Long[] eventIds, String[] states, String text,
                                             Boolean moderation, String startStr, String endStr, int from, int size);

    void unpublishComment(long commentId);

    void deleteComment(long userId, long commentId);

    CommentDto updateComment(long userId, CommentDto commentDto);


    List<CommentDtoForLists> getCommetnDtosByEvent(long eventsId);
}
