package multiValuedNominalAlpha.model;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by rc305 on 01/05/15.
 * It represents a set of labels - ie multiple observations
 * Immutable
 */
final public class Labels {
    private final Set<String> labels;

    public Labels(String[] a) {
        this.labels = new TreeSet<String>(Arrays.asList(a));
    }

    public Set toSet() {
        return labels;
    }
}
