package com.admin.web.model.vo;

import com.admin.web.model.SysUser;

/**
 * @author znn
 */
public class NoticeVo extends PageVo {
    private State state;
    private SysUser user;

    public NoticeVo() {
        this.setState(null);
        this.setUser(null);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "NoticeVo{" +
                "state=" + this.getState() +
                ", user=" + this.getUser() +
                '}';
    }

    public enum State {
        /**
         * 未读，已读，我的，全部
         */
        UNREAD, READ, ME, ALL
    }
}
