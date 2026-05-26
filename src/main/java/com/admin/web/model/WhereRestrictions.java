package com.admin.web.model;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author znn
 */
public class WhereRestrictions {
    private final List<Predicate> restrictions;

    private WhereRestrictions() {
        this.restrictions = new ArrayList<>();
    }

    public WhereRestrictions add(boolean b, Supplier<Predicate> supplier) {
        return b ? this.add(supplier) : this;
    }

    public WhereRestrictions add(Supplier<Predicate> supplier) {
        if (Objects.nonNull(supplier.get())) {
            this.restrictions.add(supplier.get());
        }
        return this;
    }

    public Predicate[] build() {
        return this.restrictions.toArray(new Predicate[0]);
    }

    public static WhereRestrictions builder() {
        return new WhereRestrictions();
    }
}
