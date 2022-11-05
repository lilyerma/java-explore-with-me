package explorewithme.ewm.users;


import explorewithme.ewm.users.dto.NewUserRequest;
import explorewithme.ewm.users.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Tag(name = "Users controller for admins", description = "User management for admins: CRUD")
public class UserController {

    private final UserService userService;

    // Метод, который добавляет нового пользователя
    @Operation(summary = "Create new user")
    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) throws RuntimeException {
        return userService.create(newUserRequest);
    }

    // Метод удаляющий пользователя
    @Operation(summary = "Delete user")
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) throws RuntimeException {
        userService.delete(userId);
    }

    // Метод по получению всех пользователей
    @Operation(summary = "Get list of users")
    @GetMapping
    public List<UserDto> getUsers() throws RuntimeException {
        return userService.getUsers();
    }

}
