package bootwildfly.dao;

import bootwildfly.model.UserModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
@Repository
public interface UserDao extends CrudRepository<UserModel, Long> {

    List<UserModel> findByEmail(@Param("email") String email);

    @Modifying
    @Query("update UserModel as u set u.msg = ?2, u.msgTime = ?3 where u.uid = ?1")
    int updateMsgAndMsgTimeByUid(@Param("uid")Long uid, @Param("msg") String msg, @Param("msgTime") long msgTime);
}
