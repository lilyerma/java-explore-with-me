package explorewithme.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import explorewithme.ewm.events.State;
import explorewithme.ewm.events.dto.EventMiniDto;
import explorewithme.ewm.users.dto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullCommentDto {

    private long id;
    private UserShortDto author;
    private String text;
    private EventMiniDto event;
    private boolean moderation;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private LocalDateTime published;
    private State state;


    public FullCommentDto(long id, String text, boolean moderation, LocalDateTime created, LocalDateTime published, State state) {
        this.id = id;
        this.text = text;
        this.moderation = moderation;
        this.created = created;
        this.published = published;
        this.state = state;
    }

}
