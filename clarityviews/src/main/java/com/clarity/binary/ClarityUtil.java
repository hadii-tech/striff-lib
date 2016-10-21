package com.clarity.binary;

import java.util.Iterator;
import java.util.Map;

/**
 * General utility related to the Clarity Web Service.
 *
 * @author Muntazir Fadhel
 */
public final class ClarityUtil {

    private ClarityUtil() {

    }

    public enum InvocationSiteProperty {

        FIELD, LOCAL, NONE, METHOD_PARAMETER, CONSTRUCTOR_PARAMETER;
    }

    public static Object getObjectFromStringObjectKeyValueMap(String value, Map<?, ?> hashMap) {
        final Iterator<?> it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<?, ?> pair = (Map.Entry<?, ?>) it.next();
            if (pair.getValue().equals(value)) {
                return pair.getKey();
            }
        }
        return null;
    }
}
