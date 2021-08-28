package com.example.mysql.util;

import sun.security.jca.GetInstance;

/**
 * @author: liufeixiang
 * @date: 8/26/21 6:48 PM
 */
public class Singleton {

//    private static Singleton instance = new Singleton();
//
//    private Singleton(){
//    }
//
//    public static Singleton getInstance(){
//        return instance;
//    }

    private static volatile Singleton singleton ;


    public static Singleton getInstance(){
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    return new Singleton();
                }
            }
        }
        return singleton;
    }
}
