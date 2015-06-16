package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.*;

import java.util.concurrent.Callable;

public final class TimeFunction0<T> implements Function0<T> {
    private static final String FORMAT = "Elapsed time: %s msecs";
    public static final Block<Number> DEFAULT_REPORTER = Runnables.printLine(FORMAT);
    private final Callable<? extends T> callable;
    private final Function1<? super Number, ?> reporter;

    private TimeFunction0(Callable<? extends T> callable, Function1<? super Number, ?> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    public final T call() throws Exception {
        long start = System.nanoTime();
        T result = callable.call();
        reporter.call(calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static double calculateMilliseconds(long start, long end) {
        return calculateNanoseconds(start, end) / 1000000.0;
    }

    public static long calculateNanoseconds(long start, long end) {
        return (end - start);
    }

    public static <T> TimeFunction0<T> time(Callable<? extends T> callable){
        return time(callable, DEFAULT_REPORTER);
    }

    public static <T> TimeFunction0<T> time(Callable<? extends T> callable, Function1<? super Number, ?> reporter){
        return new TimeFunction0<T>(callable, reporter);
    }

    public static <T,R> TimeFunction0<R> time(Function1<? super T,? extends R> callable, T value){
        return time(callable, value, DEFAULT_REPORTER);
    }

    public static <T,R> TimeFunction0<R> time(Function1<? super T,? extends R> callable, T value, Function1<? super Number, ?> reporter){
        return new TimeFunction0<R>(Callables.deferApply(callable, value), reporter);
    }

    public static <T> TimeFunction0<Sequence<T>> time(Sequence<T> sequence){
        return time(Callables.<T>realise(), sequence, DEFAULT_REPORTER);
    }

    public static <T> TimeFunction0<Sequence<T>> time(Sequence<T> sequence, Function1<? super Number, ?> reporter){
        return time(Callables.<T>realise(), sequence, reporter);
    }
}
