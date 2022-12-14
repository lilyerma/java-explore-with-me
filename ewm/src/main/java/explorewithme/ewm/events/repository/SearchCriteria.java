package explorewithme.ewm.events.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.JavaType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private String type;

}

