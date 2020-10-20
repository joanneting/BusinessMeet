package tw.com.businessmeet.function;

@FunctionalInterface
public interface Supplier<R> {
    R get();
}
