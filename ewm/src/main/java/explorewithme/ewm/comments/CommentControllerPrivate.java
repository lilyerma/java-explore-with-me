package explorewithme.ewm.comments;


import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.dto.FullCommentDto;
import explorewithme.ewm.comments.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Private comments controller for admins and authenticated users")
public class CommentControllerPrivate {

    private final CommentService commentService;

    @Operation(description = "User who has approved participation request posts comment")
    @PostMapping("users/{userId}/comments/{eventId}")
    public CommentDto postComment(@PathVariable long userId,
                                               @Positive @PathVariable long eventId,
                                               @Valid @RequestBody CommentDto commentDto) throws RuntimeException {
        log.debug("Post request to post comment to event");
        return commentService.postComment(userId, eventId, commentDto);
    }
    @Operation(description = "User gets self-created comments")
    @GetMapping("users/{userId}/comments")
    public List<FullCommentDto> getCommentsByUser(@Positive @PathVariable long userId) throws RuntimeException {
        log.debug("Get request to get list of comments by user");
        return commentService.getCommentsByUser(userId);
    }


    @Operation(description = "User deletes self created comment if not published")
    @DeleteMapping("users/{userId}/comment/{commentId}")
    public void deleteComment (@Positive @PathVariable long userId,
                               @Positive @PathVariable long commentId)
            throws RuntimeException {
        log.debug("Request to delete comment " + commentId + " to private controller from userId "
                + userId);
        commentService.deleteComment(userId, commentId);
    }

    @Operation(description = "User edits self-created comment if not published")
    @PatchMapping("users/{userId}/comment")
    public CommentDto updateComment (@Positive @PathVariable long userId,
                                     @Valid @RequestBody CommentDto commentDto)
            throws RuntimeException {
        log.debug("Patch comment request to private controller from userId " + userId + " to event ");
        return commentService.updateComment(userId, commentDto);
    }

    //Admin work with comments

    @Operation(description = "Admin sets comment to PUBLISHED")
    @PatchMapping("admin/comments/{commentId}/publish")
    public FullCommentDto publishCommentAdmin(@Positive @PathVariable long commentId) throws RuntimeException {
        log.debug("Patch publish comment by admin request");
        return commentService.publishComment(commentId);
    }

    @Operation(description = "Admin rejects comment")
    @PatchMapping("admin/comments/{commentId}/reject")
    public FullCommentDto rejectCommmentAdmin(@Positive @PathVariable long commentId)
            throws RuntimeException {
        log.debug("Patch reject comment by admin request");
        return commentService.rejectComment(commentId);
    }

    @Operation(description = "Admin sets comment to Rejected from Published")
    @DeleteMapping("admin/comments/{commentId}/unpublish")
    public void unpublishCommmentAdmin(@Positive @PathVariable long commentId) throws RuntimeException {
        log.debug("Unpublish  comment by admin request");
        commentService.unpublishComment(commentId);
    }

    @Operation(description = "Get comments by filters")
    @GetMapping("admin/comments")
    public List<FullCommentDto> getCommentsForAdmin(@RequestParam(name = "users", required = false) Long[] users,
                                                    @RequestParam(name = "event", required = false) Long[] eventId,
                                                    @RequestParam(name = "states", required = false) String[] states,
                                                    @RequestParam(name = "moderation", required = false) Boolean moderation,
                                                    @RequestParam(name = "text", required = false) String text,
                                                    @RequestParam(name = "rangeStart", required = false) String start,
                                                    @RequestParam(name = "rangeEnd", required = false) String end,
                                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "10") int size)
            throws RuntimeException {

        log.debug("Get comments request from admin, filters: users " + users + " states" + states + " categories " + " start "
                + start + " end " + end + " from " + from +  "size " + size);
        return commentService.getCommentsForAdmin(users, eventId, states, text,  moderation, start, end, from, size);
    }

}
