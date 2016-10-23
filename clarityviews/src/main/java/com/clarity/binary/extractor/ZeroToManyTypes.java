package com.clarity.binary.extractor;

import java.util.ArrayList;
import java.util.List;

public class ZeroToManyTypes {

    private static List<String> types = new ArrayList<String>();

    static {
        types.add("java.util.ArrayList");
        types.add("java.util.Set");
        types.add("java.util.Map");
        types.add("java.util.SortedSet");
        types.add("java.util.Collection");
        types.add("java.util.AbstractCollection");
        types.add("java.util.AbstarctList");
        types.add("java.util.AbstractQueue");
        types.add("java.util.AbstractSequentialList");
        types.add("java.util.AbstractSet");
        types.add("java.util.ArrayDeque");
        types.add("java.util.AttributeList");
        types.add("java.util.BeanContextServiceSupport");
        types.add("java.util.BeanContextServicesSupport");
        types.add("java.util.BeanContextSupport");
        types.add("java.util.ConcurrentLinkedDeque");
        types.add("java.util.ConcurrentLinkedQueue");
        types.add("java.util.ConcurrentSkipListSet");
        types.add("java.util.CopyOnWriteArrayList");
        types.add("java.util.CopyOnWriteArraySet");
        types.add("java.util.DelayQueue");
        types.add("java.util.EnumSet");
        types.add("java.util.HashSet");
        types.add("java.util.JobStateReasons");
        types.add("java.util.LinkedBlockingDeque");
        types.add("java.util.LinkedBlockingQueue");
        types.add("java.util.LinkedHashSet");
        types.add("java.util.LinkedList");
        types.add("java.util.LinkedTransferQueue");
        types.add("java.util.PriorityBlockingQueue");
        types.add("java.util.PriorityQueue");
        types.add("java.util.RoleList");
        types.add("java.util.RoleUnresolvedList");
        types.add("java.util.Stack");
        types.add("java.util.SynchronousQueue");
        types.add("java.util.TreeSet");
        types.add("java.util.Vector");
        types.add("java.util.RoleList");
        types.add("java.util.AbstractMap");
        types.add("java.util.Attributes");
        types.add("java.util.AuthProvider");
        types.add("java.util.ConcurrentHashMap");
        types.add("java.util.ConcurrentSkipListMap");
        types.add("java.util.EnumMap");
        types.add("java.util.HashMap");
        types.add("java.util.Hashtable");
        types.add("java.util.IdentityHashMap");
        types.add("java.util.LinkedHashMap");
        types.add("java.util.PrinterStateReasons");
        types.add("java.util.Properties");
        types.add("java.util.Provider");
        types.add("java.util.RenderingHints");
        types.add("java.util.SimpleBindings");
        types.add("java.util.TabularDataSupport");
        types.add("java.util.TreeMap");
        types.add("java.util.UIDefaults");
        types.add("java.util.WeakHashMap");
    }

    public static boolean isZeroToManyType(String str) {
        return types.contains(str);
    }
}
