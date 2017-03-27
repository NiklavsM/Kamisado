/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author ben
 */
public interface MyObservable {

	void tellAll(Object arg);

	void addObserver(MyObserver o);

	void removeObserver(MyObserver o);

	ArrayList<MyObserver> getObservers();
}
