package com.github.sigrarr.lunisolarcalc.util;

import java.util.HashSet;
import java.util.Set;

public class Sets {

    @SuppressWarnings("unchecked")
    public static <T> Set<T> of(T... elements) {
        Set<T> set = new HashSet<>();
        for(T elem : elements) set.add(elem);
        return set;
    }

    public static <T> Set<T> empty() {
        return new HashSet<>();
    }
}
