package com.admin.web.model.vo;

import com.admin.web.model.enums.Move;

import java.io.Serializable;

/**
 * @author znn
 */
public class MoveVo implements Serializable {
    private Long id;
    private Move move;

    public MoveVo() {
        this.setId(null);
        this.setMove(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id=" + this.getId() +
                ", move=" + this.getMove() +
                '}';
    }

}
