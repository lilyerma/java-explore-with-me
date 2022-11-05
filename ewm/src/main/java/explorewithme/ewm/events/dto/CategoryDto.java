package explorewithme.ewm.events.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for the category")
public class CategoryDto {

    @NotNull
    @Schema(description = "Category id")
    private long id;
    @NotBlank
    @NotEmpty
    @Schema(description = "Category name")
    private String name;

}
