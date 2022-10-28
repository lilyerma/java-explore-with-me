package explorewithme.ewm.comments;

import explorewithme.ewm.comments.dto.CommentDto;
import explorewithme.ewm.comments.dto.CommentDtoForLists;
import explorewithme.ewm.comments.dto.FullCommentDto;
import explorewithme.ewm.comments.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public static Comment fromDtoToComment(CommentDto commentDto){
        Comment comment = new Comment(commentDto.getText());
        return comment;
    }

    public static CommentDtoForLists fromCommentToListsDto(Comment comment){
        CommentDtoForLists commentDto = new CommentDtoForLists();
        if (comment.getPublished()!= null){
            commentDto.setPublished(comment.getPublished());
        }
        commentDto.setText(comment.getText());
        return commentDto;
    }

    public static CommentDto shortFromComment(Comment comment){
        return new CommentDto(comment.getId(),comment.getText());
    }

    public static FullCommentDto fromComment(Comment comment){
        return new FullCommentDto(comment.getId(),
                comment.getText(),
                comment.isModeration(),
                comment.getCreated(),
                comment.getPublished(),
                comment.getState());
    }



}
