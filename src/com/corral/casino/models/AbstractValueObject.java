package com.corral.casino.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractValueObject {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
