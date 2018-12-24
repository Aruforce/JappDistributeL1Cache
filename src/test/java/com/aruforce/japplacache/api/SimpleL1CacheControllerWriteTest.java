package com.yf.japplacache.api;

import com.yf.jappl1cache.api.SimpleL1CacheController;
import com.yf.jappl1cache.api.exceptions.CacheNotExistsException;
import com.yf.jappl1cache.api.inter.L1CacheController;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class SimpleL1CacheControllerWriteTest {
    @Test
    public void getTest() {
        L1CacheController l1CacheController = SimpleL1CacheController.getL1CacheController();
        CountDownLatch latch  = new CountDownLatch(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    String helloworld = null;
                    try {
                        helloworld = l1CacheController.getCache("helloworld");
                    } catch (CacheNotExistsException e) {
                        l1CacheController.setCache("helloworld", "0");
                        continue;
                    }
                    int j = Integer.valueOf(helloworld).intValue();
                    l1CacheController.setCache("helloworld", (j + 1) + "");
                }
                latch.countDown();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    String helloworld = null;
                    try {
                        helloworld = l1CacheController.getCache("helloworld");
                    } catch (CacheNotExistsException e) {
                        l1CacheController.setCache("helloworld", "0");
                        continue;
                    }
                    int j = Integer.valueOf(helloworld).intValue();
                    l1CacheController.setCache("helloworld", (j + 1) + "");
                }
                latch.countDown();
            }
        });
        t1.start();
        t2.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(l1CacheController.getCache("helloworld"));
        } catch (CacheNotExistsException e) {
            e.printStackTrace();
        }
    }


}
