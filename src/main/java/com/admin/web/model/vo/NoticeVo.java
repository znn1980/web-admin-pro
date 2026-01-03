package com.admin.web.model.vo;

/**
 * @author znn
 */
public class NoticeVo extends PageVo {
    private State state;

    public NoticeVo() {
        this.setState(null);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "NoticeVo{" +
                "state=" + this.getState() +
                '}';
    }

    public enum State {
        /**
         * 未读，已读，我的，全部
         */
        UNREAD, READ, ME, ALL
    }
}
