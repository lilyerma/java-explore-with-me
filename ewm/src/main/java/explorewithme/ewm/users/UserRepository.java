package explorewithme.ewm.users;

import explorewithme.ewm.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query
    List<User> findByIdIn(List<Long> ids);

}
