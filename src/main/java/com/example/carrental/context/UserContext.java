package com.example.carrental.context;

import com.example.carrental.model.auth.LoginUser;

/**
 * 当前请求用户上下文。
 * 基于 ThreadLocal 保存认证后的用户信息，供业务层在不额外传参的情况下获取当前登录人。
 */
public final class UserContext {

    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    /**
     * 写入当前线程的登录用户。
     */
    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    /**
     * 获取当前线程绑定的完整登录用户对象。
     */
    public static LoginUser get() {
        return HOLDER.get();
    }

    /**
     * 获取当前登录用户 ID，常用于创建人、审计字段等场景。
     */
    public static Long getUserId() {
        LoginUser user = HOLDER.get();
        return user == null ? null : user.getUserId();
    }

    /**
     * 清理当前线程中的用户信息。
     */
    public static void clear() {
        HOLDER.remove();
    }
}
