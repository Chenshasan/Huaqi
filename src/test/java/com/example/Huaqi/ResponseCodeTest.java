package com.example.Huaqi;

import com.example.Huaqi.enums.ResponseCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest

@WebAppConfiguration
public class ResponseCodeTest {
    @Test
    public void test1(){
        ResponseCode code = ResponseCode.Success;
        System.out.println(code.getCode());
    }
}
