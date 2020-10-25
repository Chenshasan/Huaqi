package com.example.Huaqi.data;

import com.example.Huaqi.po.OptionPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OptionMapperTest {
    @Autowired
    OptionMapper optionMapper;

    @Test
    // 测试根据月份拿期权，主要是like
    public void testGetMonthOption(){
        List<OptionPO> optionPOS = optionMapper.getMonthOption("2020-10");
        for (OptionPO optionPO:optionPOS) {
            System.out.println(optionPO);
        }
    }

    @Test
    public void testGetOptionByETF(){
        List<OptionPO> options = optionMapper.getOptionByETF("510050.SH");
        for (OptionPO optionPO:options) {
            System.out.println(optionPO);
        }
    }

    @Test
    public void testGetOptionByETFError(){
        List<OptionPO> options = optionMapper.getOptionByETF("ERROR_CODE");
        assert (options.size()==0);
    }
}
