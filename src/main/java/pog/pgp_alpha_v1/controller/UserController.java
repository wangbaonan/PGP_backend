package pog.pgp_alpha_v1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.model.request.UserLoginRequest;
import pog.pgp_alpha_v1.model.request.UserRegisterRequest;
import pog.pgp_alpha_v1.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static pog.pgp_alpha_v1.constant.Constants.ADMIN_ROLE;
import static pog.pgp_alpha_v1.constant.Constants.USER_LOGIN_STATE;

/**
 * 用户接口
 * @author Wangbaonan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return null;
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String username = userRegisterRequest.getUsername();
        String mail = userRegisterRequest.getMail();
        // 本处校验不对业务逻辑进行校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, username, mail)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword, mail, username);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        // 从前端传来的请求体中获取参数
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 本处校验不对业务逻辑进行校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){
        if(isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username); //模糊查询
        }
        return userService.list();
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        if(!isAdmin(request)){
            return false;
        }
        if(id <= 0){
            return false;
        }
        // 逻辑删除
        return userService.removeById(id);
    }

    /**
     * 鉴权 是否为管理员
     * @param request HttpServletRequest
     * @return T-管理员 F-非管理员
     */
    private boolean isAdmin(HttpServletRequest request){
        // 鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
