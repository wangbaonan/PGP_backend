package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pog.pgp_alpha_v1.mapper.UserMapper;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.UserService;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wangbaonan
 * 针对表【user】的数据库操作Service实现
 * 2023-04-10 22:21:40
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    final String emailRegex = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
    final String accountRegex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s+]";

    @Resource
    private UserMapper userMapper;

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
            if(!Pattern.matches(emailRegex, email)){
                return -1;
            }
        }
        // 账户不能包含重复字符 正则表达式
        Matcher matcher = Pattern.compile(accountRegex).matcher(userAccount);
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


}




