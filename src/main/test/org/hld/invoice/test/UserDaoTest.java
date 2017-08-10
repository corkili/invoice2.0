package org.hld.invoice.test;

import junit.framework.TestCase;
import lombok.extern.log4j.Log4j;
import org.hld.invoice.dao.UserDao;
import org.hld.invoice.entity.Authority;
import org.hld.invoice.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * Created by 李浩然 On 2017/8/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/applicationContext.xml", "classpath:/META-INF/spring-mvc.xml"})
@Log4j
public class UserDaoTest extends TestCase {

    @Autowired
    private UserDao userDao;

    @Test
    public void testAll() {
        User user = new User();
        user.setEmail("15528235793@163.com");
        user.setPassword("lhr6412145");
        user.setName("李浩然");
        user.setJobId("2015141463087");
        user.setPhone("15528235793");
        user.setIsManager(false);
        user.setEnabled(false);
        user.setVerificationCode("ASDF5169861");
        user.setCreateTime(new Date());
        user.setVerifyTime(new Date());
        user.setImage(null);

        Authority authority = new Authority();
        authority.setAddInvoice(false);
        authority.setManageUser(false);
        authority.setModifyInvoice(false);
        authority.setQueryInvoice(false);
        authority.setQueryReport(false);
        authority.setRemoveInvoice(false);
        user.setAuthority(authority);
        log.info("init user:" + user);

        Integer id = userDao.save(user);
        log.info("saved user: " + user);
        user.setEnabled(true);
        log.info("modify enabled: " + user);
        userDao.merge(user);
        log.info("modify enabled done: " + user);
        user.getAuthority().setAddInvoice(true);
        log.info("modify auth: " + user);
        userDao.saveOrUpdate(user);
        log.info("modify auth done: " + user);

        User testUserId = userDao.get(id);
        log.info("get user by id: "+ testUserId);
        testUserId.setIsManager(true);
        testUserId.getAuthority().setManageUser(true);
        log.info("modify manager and auth: " + testUserId);
        userDao.update(testUserId);
        log.info("modify manager and auth done: " + testUserId);

        List<User> userList = userDao.findAllUser();
        for (User u : userList) {
            log.info("find all user: " + u);
        }

        log.info("user size: " + userDao.getTotalCount());

        log.info("find user by email: " + userDao.findUserByEmail("15528235793@163.com"));
    }
}
