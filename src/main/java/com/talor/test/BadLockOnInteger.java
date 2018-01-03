package com.talor.test;

import lombok.Builder;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/12/21 15:30
 */
public class BadLockOnInteger implements Runnable {
    public static Integer i=0;

    static BadLockOnInteger instance=new BadLockOnInteger();

    @Override
    public void run(){
        for (int j = 0; j < 1000000; j++) {
            synchronized (i){
                i++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(instance);
        Thread t2=new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
