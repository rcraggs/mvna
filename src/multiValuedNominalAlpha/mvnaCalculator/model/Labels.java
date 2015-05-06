package multiValuedNominalAlpha.mvnaCalculator.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by rc305 on 01/05/15.
 * It represents a set of labels - ie multiple observations
 * Immutable
 */
final public class Labels implements Iterable, Comparable {
    private final Set<String> labels;

    public Labels(String[] a) {
        this.labels = new TreeSet<>(Arrays.asList(a));
    }

    public Labels(Set s) {
        this.labels = new TreeSet<>(s);
    }

    public Set toSet() {
        return labels;
    }

    public int size() {
        return labels.size();
    }

    @Override
    public Iterator iterator() {
        return labels.iterator();
    }

    @Override
    public int compareTo(Object o) {

        if (this.size() < ((Labels) o).size())
            return -1;
        if (this.size() == ((Labels) o).size() && isLowerAlphabetically(this, (Labels) o))
            return -1;
        else
            return 1;
    }

    private boolean isLowerAlphabetically(Labels a, Labels b) {

        Iterator ai = a.labels.iterator();
        Iterator bi = b.labels.iterator();

        for (int i = 0; i < a.size(); i++) {
            String sa = (String) ai.next();
            String sb = (String) bi.next();

            if (sa.compareToIgnoreCase(sb) < 0) {
                return true;
            }
            if (sa.compareToIgnoreCase(sb) > 0) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Labels labels1 = (Labels) o;

        if (labels != null ? !labels.equals(labels1.labels) : labels1.labels != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return labels != null ? labels.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "{" + String.join(", ", this) + "}";
    }
}