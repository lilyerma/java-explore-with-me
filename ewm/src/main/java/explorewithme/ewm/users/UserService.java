package explorewithme.ewm.users;

import explorewithme.ewm.users.dto.NewUserRequest;
import explorewithme.ewm.users.dto.UserDto;
import explorewithme.ewm.users.dto.UserShortDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService {
    @Transactional
    UserDto create(NewUserRequest newUserRequest);

    void checkId(long id);

    @Transactional
    UserDto update(UserDto userDto, long id);

    @Transactional
    void delete(long id);

    List<UserDto> getUsers();

    UserDto getUserById(long id);

    List<UserDto> getUsersByids(Long[] userIds);

    Map<Long, UserShortDto> getUsersByIds(List<Long> userIds);
}
