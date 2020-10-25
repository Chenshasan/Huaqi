package com.example.Huaqi.serviece;

import com.example.Huaqi.HuaqiApplication;
import com.example.Huaqi.bl.OptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HuaqiApplication.class)
@Transactional
public class OptionServiceTest {

    @Autowired
    OptionService optionService;

    @Test
    public void testDeltaCurve(){
        optionService.getDeltaCurve("510050.SH");
    }
}
