package com.clarity.rest.core.component;

import java.util.List;

/**
 * A group of components.
 *
 * @author Muntazir Fadhel
 */
public interface ComponentGrouping {

    /**
     * Returns a list consisting of the names of all the components in the
     * group.
     */
    List<String> componentGroup();
}
