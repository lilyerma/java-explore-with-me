package explorewithme.ewm.comments.repository;

import explorewithme.ewm.events.State;
import explorewithme.ewm.comments.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, JpaSpecificationExecutor<Comment> {

    @Query
    List<Comment> findCommentsByAuthor(long author);

    @Query(nativeQuery = true, value = "select * from comments where author = ? and comment_id = ?")
    Comment getCommentsByAuthorAndId(long author, long commentId);

    @Query(nativeQuery = true, value = "select * from comments where event_id = ?")
    List<Comment> getCommentsByEvent(long event);

    @Query(nativeQuery = true, value = "select count(*) from comments where event_id =?")
    int countCommentById(long eventId);

    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.state = ?1, c.moderation = ?2, c.published = ?4 where c.id =?3")
    int updateCommentStatus(State state, boolean moderation, long commentId, LocalDateTime date);

}