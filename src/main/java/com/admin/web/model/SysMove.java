package com.admin.web.model;

import com.admin.web.model.entity.SysBase;
import com.admin.web.model.enums.Move;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author znn
 */
public record SysMove(boolean move, int index) {

    public void move(Consumer<Integer> consumer) {
        if (this.move()) {
            consumer.accept(this.index());
        }
    }

    public static SysMove of(Move move, Long id, List<? extends SysBase> sysBases) {
        int index = IntStream.range(0, sysBases.size())
                .filter(i -> Objects.equals(id, sysBases.get(i).getId()))
                .findFirst().orElse(-1);
        if (index == -1 || (Objects.equals(Move.UP, move) ? index <= 0 : index >= sysBases.size() - 1)) {
            return new SysMove(false, index);
        }
        return new SysMove(true, Objects.equals(Move.UP, move) ? index - 1 : index + 1);
    }
}
