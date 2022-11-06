package explorewithme.ewm.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import explorewithme.ewm.requests.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Schema(description = "DTO for the participation request")
public class ParticipationRequestDto {

    @Schema(description = "Date whe request was submitted")
    @JsonSerialize
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @Schema(description = "Id of the event")
    private long event;
    @Schema(description = "Id of the request")
    private long id;
    @Schema(description = "Id of the requester")
    private long requester;
    @Schema(description = "Status of the request")
    private Status status;

}
