package pog.pgp_alpha_v1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pog.pgp_alpha_v1.model.User;
import pog.pgp_alpha_v1.service.UserService;
import pog.pgp_alpha_v1.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 86183
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-10 22:21:40
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




