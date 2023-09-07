package pog.pgp_alpha_v1.constants;

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

    /**
     * 用户名最大长度
     */
    public static final Integer USERNAME_MAX_LENGTH = 4;

    /**
     * 密码最大长度
     */
    public static final Integer PASSWORD_MAX_LENGTH = 8;
    /**
     * 验证码值最大值
     */
    public static final Integer VERIFY_CODE_MAX = 999999;

    /**
     * 验证码值最小值
     */
    public static final Integer VERIFY_CODE_MIN = 100000;

    /**
     * 验证码过期时间
     */
    public static final Integer VERIFY_CODE_EXPIRE_TIME = 10;

    // ---- 权限 ----
    /**
     * 默认权限
     */
    public static final Integer DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    public static final Integer ADMIN_ROLE = 1;

    // ---- 状态码 ----
    /**
     * 成功
     */
    public static final Integer SUCCESS = 20000;

    /**
     * 验证码邮件发送成功
     */
    public static final String  VERIFY_CODE_EMAIL_SEND_SUCCESS = "Verification code email sent successfully!";

    public static final String  LOGOUT_SUCCESS = "Logout successfully!";
    public static final String  UPLOAD_SUCCESS = "Upload successfully!";
    public static final String  SAVE_ERROR = "A file with the same MD5Hash value already exists.";
}
