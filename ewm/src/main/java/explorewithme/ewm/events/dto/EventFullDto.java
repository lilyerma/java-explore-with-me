package explorewithme.ewm.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import explorewithme.ewm.comments.dto.CommentDtoForLists;
import explorewithme.ewm.events.State;
import explorewithme.ewm.events.model.Location;
import explorewithme.ewm.users.dto.UserShortDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Full Dto for the event")
public class EventFullDto {


    @Schema(description = "Annotation")
    String annotation;
    @Schema(description = "Category Dto")
    CategoryDto category;
    @Schema(description = "Number of the confirmed requests")
    int confirmedRequests;
    @Schema(description = "Date when event info was submitted")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @Schema(description = "Description")
    String description;
    @Schema(description = "Date of the event")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @Schema(description = "Event id")
    long id;
    @Schema(description = "User Dto - initiator")
    UserShortDto initiator;
    @Schema(description = "Location as coordinates")
    Location location;
    @Schema(description = "Does event need payment")
    boolean paid;
    @Schema(description = "Limit of the participants, 0 means limitless")
    int participantLimit;
    @Schema(description = "When event was published")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    @Schema(description = "Does event require moderation")
    boolean requestModeration;
    @Schema(description = "State")
    State state;
    @Schema(description = "Title")
    String title;
    @Schema(description = "Number of views")
    int views;
    @Schema(description = "Number of comments")
    int numberOfcomments;
}
