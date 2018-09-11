package bootwildfly.dao;

import bootwildfly.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author houguangqiang
 * @date 2018-09-10
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void save() {
        UserModel userModel = new UserModel(123L, "abc");
        userDao.save(userModel);
        UserModel userModel2 = new UserModel(124L, "bcd");
        userDao.save(userModel2);

        UserModel one = userDao.findOne(123L);
        System.out.println(one);

        List<UserModel> result = userDao.findByEmail("bcd");
        System.out.println(result);
    }
}