package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    private Comparator<T> defaultComparator;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.defaultComparator = c;
    }
    public T max() {
        return max(this.defaultComparator);
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxElement = get(0);
        for (T currentElement : this) {
            if (c.compare(currentElement, maxElement) > 0) {
                maxElement = currentElement;
            }
        }
        return maxElement;
    }
}
