package server;

import java.util.ArrayList;

public class Pipe<T> {
    private final ArrayList<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    public void send(T t) {
        synchronized (observers) {
            observers.forEach(o -> o.onNext(t));
        }
    }

    public void destroy() {
        synchronized (observers) {
            observers.clear();
        }
    }
}
