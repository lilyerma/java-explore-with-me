package explorewithme.ewm.events.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class NewCategoryDto {

    @NotEmpty
    @NotBlank
    String name;
}
