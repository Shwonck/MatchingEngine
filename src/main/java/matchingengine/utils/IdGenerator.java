package matchingengine.utils;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private static final AtomicLong counter = new AtomicLong();

    // I did an utility class so I can generate global Id's independent if it is for Limit or Market Orders

    public static long generateId(){
        return counter.incrementAndGet();
    }
}
