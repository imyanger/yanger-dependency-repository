package com.yanger.starter.mongo.reflection.property;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import lombok.Getter;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
    /** Indexed name */
    @Getter
    private final String indexedName;
    /** Children */
    @Getter
    private final String children;
    /** Name */
    @Getter
    private String name;
    /** Index */
    @Getter
    private String index;

    /**
     * Property tokenizer
     *
     * @param fullname fullname
     */
    public PropertyTokenizer(@NotNull String fullname) {
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            this.name = fullname.substring(0, delim);
            this.children = fullname.substring(delim + 1);
        } else {
            this.name = fullname;
            this.children = null;
        }
        this.indexedName = this.name;
        delim = this.name.indexOf('[');
        if (delim > -1) {
            this.index = this.name.substring(delim + 1, this.name.length() - 1);
            this.name = this.name.substring(0, delim);
        }
    }

    /**
     * Has next boolean
     *
     * @return the boolean
     */
    @Override
    public boolean hasNext() {
        return this.children != null;
    }

    /**
     * Next property tokenizer
     *
     * @return the property tokenizer
     */
    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(this.children);
    }

    /**
     * Remove
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
