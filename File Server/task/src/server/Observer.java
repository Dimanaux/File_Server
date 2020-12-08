package server;

public interface Observer<T> {
    void onNext(T e);
}
