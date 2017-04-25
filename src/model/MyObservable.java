package model;

import java.util.ArrayList;

public interface MyObservable {

	void tellAll(Object arg);

	void addObserver(MyObserver o);

	void removeObserver(MyObserver o);

	ArrayList<MyObserver> getObservers();
}
