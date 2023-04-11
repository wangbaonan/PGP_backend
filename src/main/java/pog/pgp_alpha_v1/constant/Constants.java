package pog.pgp_alpha_v1.constant;

public class Constants {

    /**
     * 检查邮件格式正则表达式
     */
    public static final String EMAIL_REGEX = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
    /**
     * 账户格式正则表达式
     */
    public static final String ACCOUNT_REGEX = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s+]";
    /**
     * 用户登录状态
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    // ---- 权限 ----
    /**
     * 默认权限
     */
    public static final Integer DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    public static final Integer ADMIN_ROLE = 1;
}
