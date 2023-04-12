package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.UserMapper;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pog.pgp_alpha_v1.constant.Constants.*;

/**
 * @author Wangbaonan
 * 针对表【user】的数据库操作Service实现
 * 2023-04-10 22:21:40
 * 用户服务实现类
 */

@Service
@Slf4j // log
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    private UserService userService;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String email, String username) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        // 校验账户名长度
        if (userAccount.length() < 4) {
            return -1;
        }
        // 校验密码长度
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        // 校验邮箱格式
        if ((email != null) && (!email.isEmpty())) {
            if (!Pattern.matches(EMAIL_REGEX, email)) {
                return -1;
            }
        }
        // 账户不能包含重复字符 正则表达式
        Matcher matcher = Pattern.compile(ACCOUNT_REGEX).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询次数
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 2.加密
        // BCryptPasswordEncoder工具底层封装了盐值加密,并且无需在数据库中维持salt字段
        // 采用SHA-256+随机盐+密钥对密码进行加密, SHA系列是Hash算法
        // SHA-256碰撞几率更小
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(userPassword);

        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setMail(email);
        user.setUsername(username);
        boolean saveFlag = this.save(user);
        if (!saveFlag) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // 校验账户名长度
        if (userAccount.length() < 4) {
            return null;
        }
        // 校验密码长度
        if (userPassword.length() < 8) {
            return null;
        }
        // 账户不能包含重复字符 正则表达式
        Matcher matcher = Pattern.compile(ACCOUNT_REGEX).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        // 利用Mapper查询需要使用QueryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在
        if (user == null) {
            log.info("User Login failed, user account : " + userAccount + " can't found!");
            return null;
        }

        // 获取数据库中的 SHA-256 + 随机盐值的密码 利用passwordEncoder方法匹配用户输入的密码与数据库中加密的密码
        String encodedPassword = user.getUserPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userPassword, encodedPassword);
        if (matches){
            // 用户脱敏
            User safetyUser = new User();
            safetyUser.setId(user.getId());
            safetyUser.setUserAccount(user.getUserAccount());
            safetyUser.setMail(user.getMail());
            safetyUser.setUserStatus(user.getUserStatus());
            safetyUser.setUsername(user.getUsername());
            safetyUser.setCreateTime(user.getCreateTime());
            safetyUser.setUserRole(user.getUserRole());

            // 记录用户的登录态 Session
            // 单个服务器登录 如果是分布式登录改成Redis
            request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
            return safetyUser;
        }
        return null;
    }

}




