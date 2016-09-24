package com.clarity.rest.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Chops a list into non-view sublists of length L.
 *
 */
public class ChoppedList {

    private List<?> list;
    private int l;

    public <T> ChoppedList(final List<T> list, final int l) {
        this.list = list;
        this.l = l;
    }

    @SuppressWarnings("unchecked")
    public <T> List<List<T>> chopped() {
        final ArrayList<List<T>> parts = new ArrayList<List<T>>();
        final int n = list.size();
        for (int i = 0; i < n; i += l) {
            parts.add(new ArrayList<T>((Collection<? extends T>) list.subList(i, Math.min(n, i + l))));
        }
        return parts;
    }
}
