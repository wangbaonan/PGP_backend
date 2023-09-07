package pog.pgp_alpha_v1.common;

/**
 * 错误码
 */
public enum ErrorCode {

    /*
    SUCCESS(0, "OK", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    //用户未激活
    USER_NOT_ACTIVATED(40002, "用户未激活", ""),
    USER_LOGIN_ERROR(40003, "用户名或密码错误", ""),
    //文件已存在
    FILE_EXIST(40004, "文件已存在", ""),
    //文件上传失败
    FILE_UPLOAD_ERROR(40005, "文件上传失败", ""),
    EMAIL_REGISTERED(40006, "邮箱已注册", "");
    */
    SUCCESS(20000, "OK", ""),
    PARAMS_ERROR(40000, "Request parameters error", ""),
    NULL_ERROR(40001, "Request data is null", ""),
    NOT_LOGIN(40100, "Not logged in", ""),
    NO_AUTH(40101, "No authorization", ""),
    SYSTEM_ERROR(50000, "System internal exception", ""),
    // User not activated
    USER_NOT_ACTIVATED(40002, "User not activated", ""),
    USER_LOGIN_ERROR(40003, "Username or password is incorrect", ""),
    // File already exists
    FILE_EXIST(40004, "File already exists", ""),
    // File upload failed
    FILE_UPLOAD_ERROR(40005, "File upload failed", ""),
    EMAIL_REGISTERED(40006, "Email has been registered", ""),
    ACCOUNT_LENGTH_ERROR(40007, "The account name cannot be less than 4 characters", ""),
    PASSWORD_LENGTH_ERROR(40008, "The password cannot be less than 8 characters", ""),
    EMAIL_FORMAT_ERROR(40009, "Please enter the correct email format", ""),
    EMAIL_REPEAT_ERROR(40010, "The email has been registered", ""),
    ACCOUNT_REGEX_ERROR(40011, "The account name cannot contain specific characters", ""),
    PASSWORD_CHECK_ERROR(40012, "The password and check password are inconsistent", ""),
    ACCOUNT_REPEAT_ERROR(40013, "The account has been registered", ""),
    REGISTER_ERROR(40014, "Registration failed", ""),
    INPUT_BLANK(40015, "Please ensure that the account, password, and check password are not empty", ""),
    CAPTCHA_CODE_ERROR(40016, "Captcha verification code error", "");



    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}

