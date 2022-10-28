package explorewithme.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import explorewithme.ewm.users.dto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    long id;
    @NotBlank
    @NotNull
    String text;

}
