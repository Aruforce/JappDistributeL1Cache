package com.yf.japplacache.api;

import com.yf.jappl1cache.api.SimpleL1CacheController;
import com.yf.jappl1cache.api.exceptions.CacheNotExistsException;
import com.yf.jappl1cache.api.inter.L1CacheController;
import org.junit.Test;

/**
 * @author Aruforce
 */
public class SimpleL1CacheControllerTest {
    // 测试单线程下本机读写
    @Test
    public void getTest() {
        L1CacheController l1CacheController = SimpleL1CacheController.getL1CacheController();

        int count = 0;
        boolean flag = true;
        while (flag) {
            String cache = null;
            try {
                cache = l1CacheController.getCache("helloworld");
            } catch (CacheNotExistsException e) {

            }
            if (null !=cache){
                flag = false;
                System.out.println(cache);
                if (cache.equals("10000")){
                    break;
                }
            }
            System.out.println("第"+(++count)+"获取");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


