package bootwildfly.service;

import bootwildfly.dao.HeaderDao;
import bootwildfly.dao.UserDao;
import bootwildfly.model.HeaderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author houguangqiang
 * @date 2018-09-11
 * @since 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private HeaderDao headerDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public int updateMsgAndMsgTimeByUid(Long uid, String msg, Long msgTime) {
        return userDao.updateMsgAndMsgTimeByUid(uid, msg, msgTime);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void deleteByUid(Long uid) {
        userDao.delete(uid);
        headerDao.deleteByUid(uid);
    }

    public Iterable<HeaderModel> getAllHeaders() {
        return headerDao.findAll();
    }
}
