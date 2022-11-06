package explorewithme.ewm.events.repository;

import explorewithme.ewm.events.model.Category;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Category c set c.name = ?1 where c.id = ?2")
    int updateCategory(String name, long id);

    @Query
    List<Category> findCategoriesByIdIn(List<Long> ids);

}
