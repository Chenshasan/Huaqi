package com.example.Huaqi;

import com.example.Huaqi.bl.OptionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OptionServiceTest extends HuaqiApplicationTests{
    @Autowired
    private OptionService optionService;

    @Test
    public void testGetEntFileById(){
        optionService.purchaseCallOption();
    }
}
