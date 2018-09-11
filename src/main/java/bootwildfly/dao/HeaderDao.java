package bootwildfly.dao;

import bootwildfly.model.HeaderModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
@Repository
public interface HeaderDao extends CrudRepository<HeaderModel, Long> {

    @Modifying
    @Query("delete from HeaderModel where uid = ?1")
    int deleteByUid(Long uid);
}
