package com.kob.backend.common.api;

/**
 * @author August
 * @create 2022-08-30 9:37
 */
public enum ResultCode implements IResultCode {
    SUCCESS(200, "操作成功"),
    NOT_LOGIN(201, "未登录"),
    FAIL_LOGIN(210, "鉴权失败"),
    FAIL_USER_PASSWORD(212, "账号或密码错误"),
    FAILURE(400, "业务异常"),
    UN_AUTHORIZED(401, "请求未授权"),
    CLIENT_UN_AUTHORIZED(401, "客户端请求未授权"),
    NOT_FOUND(404, "404 没找到请求"),
    MSG_NOT_READABLE(400, "消息不能读取"),
    METHOD_NOT_SUPPORTED(405, "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "不支持当前媒体类型"),
    REQ_REJECT(403, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    PARAM_MISS(400, "缺少必要的请求参数"),
    PARAM_TYPE_ERROR(400, "请求参数类型错误"),
    PARAM_BIND_ERROR(400, "请求参数绑定错误"),
    PARAM_VALID_ERROR(400, "参数校验失败"),
    FAIL_ThirdOauth(501, "第三方认证失败或已失效"),
    FAIL_NO_THIRD_BIND(502, "未绑定第三方"),
    FAIL_REPEAT_THIRD_BIND(503, "账号重复绑定"),
    FAIL_LOGIN_LIMIT(504, "用户不存在或限制登录");

    final int code;
    final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
