package explorewithme.ewm.comments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import explorewithme.ewm.events.State;
import explorewithme.ewm.requests.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
@AllArgsConstructor
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long author;

    @Column
    @NotBlank
    private String text;

    @Column(name = "event_id")
    private long event;

    @Column
    private boolean moderation;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "published_at")
    private LocalDateTime published;

    @Column
    @Enumerated(EnumType.STRING)
    private State state;


    public Comment() {

    }

    public Comment(String text){
        this.text = text;
        this.moderation = true;
        this.created = LocalDateTime.now();
        this.state = State.PENDING;
    }


}
