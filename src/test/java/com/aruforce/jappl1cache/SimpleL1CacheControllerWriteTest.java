package com.aruforce.jappl1cache;

import com.aruforce.jappl1cache.exceptions.CacheNotExistsException;
import com.aruforce.jappl1cache.inter.L1CacheController;
import org.junit.Test;

public class SimpleL1CacheControllerWriteTest {
    @Test
    public void getTest() throws CacheNotExistsException {
        L1CacheController l1CacheController = SimpleL1CacheController.getL1CacheController();
        l1CacheController.setCache("123","HelloWolrd");
    }
}
