package explorewithme.ewm.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import explorewithme.ewm.users.dto.UserDto;
import explorewithme.ewm.users.dto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoForLists {

    UserShortDto userDto;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime published;
    String text;

}
