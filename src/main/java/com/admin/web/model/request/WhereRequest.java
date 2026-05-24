package com.admin.web.model.request;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author znn
 */
public class WhereRequest {

    private WhereRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<Predicate> restrictions;

        private Builder() {
            this.restrictions = new ArrayList<>();
        }

        public Builder add(boolean b, Supplier<Predicate> supplier) {
            return b ? this.add(supplier) : this;
        }

        public Builder add(Supplier<Predicate> supplier) {
            if (Objects.nonNull(supplier.get())) {
                this.restrictions.add(supplier.get());
            }
            return this;
        }

        public Predicate[] build() {
            return this.restrictions.toArray(new Predicate[0]);
        }
    }
}
