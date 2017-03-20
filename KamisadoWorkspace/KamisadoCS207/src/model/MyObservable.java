/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author sid
 */
public interface MyObservable {
    ArrayList<MyObserver> observers = new ArrayList<>();

    default void tellAll(Object arg){
        for(MyObserver obs : observers){
            obs.update(this, arg);
        }
    }
    
    default void addObserver(MyObserver o){
        observers.add(o);
    }
    
    default void removeObserver(MyObserver o){
    	if(observers.contains(o)){
    		observers.remove(o);
    	}
    }
}
