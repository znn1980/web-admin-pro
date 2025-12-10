package com.admin.web.utils;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author znn
 */
public class Specifications<T> {
    private Logic logic;
    private Specification<T> specification;

    private Specifications() {
        this.logic = Logic.AND;
        this.specification = Specification.unrestricted();
    }

    public static <T> Specifications<T> where() {
        return new Specifications<>();
    }

    public Specification<T> build() {
        return this.specification;
    }

    public Specifications<T> and() {
        this.logic = Logic.AND;
        return this;
    }

    public void logic(Specification<T> specification) {
        if (this.logic == Logic.AND) {
            this.specification = this.specification.and(specification);
        }
        if (this.logic == Logic.OR) {
            this.specification = this.specification.or(specification);
        }
        if (this.logic == Logic.NOT) {
            this.specification = Specification.not(specification);
        }
    }

    /**
     * 等于
     */
    public Specifications<T> equal(String field, Object value) {
        this.logic((root, query, builder) -> value == null
                ? builder.conjunction() : builder.equal(root.get(field), value));
        return this;
    }

    /**
     * 范围查询
     */
    public <Y extends Comparable<? super Y>> Specifications<T> between(String field, Y a, Y b) {
        this.logic((root, query, builder) -> {
            if (a == null && b == null) {
                return builder.conjunction();
            } else if (a == null) {
                return builder.lessThanOrEqualTo(root.get(field), b);
            } else if (b == null) {
                return builder.greaterThanOrEqualTo(root.get(field), a);
            } else {
                return builder.between(root.get(field), a, b);
            }
        });
        return this;
    }

    /**
     * 排序
     */
    public Specifications<T> orderBy(String field, boolean asc) {
        this.logic((root, query, builder) -> {
            if (asc) {
                query.orderBy(builder.asc(root.get(field)));
            } else {
                query.orderBy(builder.desc(root.get(field)));
            }
            return builder.conjunction();
        });
        return this;
    }

    enum Logic {
        /**
         *
         */
        AND, OR, NOT
    }
}
