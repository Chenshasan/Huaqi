package com.example.Huaqi.data;

import com.example.Huaqi.po.ETFPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ETFMapperTest {
    @Autowired
    ETFMapper etfMapper;

    @Test
    public void testETFMapper(){
        ETFPO etfpo = etfMapper.getAllETF().get(0);
        System.out.println(etfpo);
        etfpo.setOption_vol_pcf(1234.5);
        etfMapper.updateETF(etfpo);
        etfpo = etfMapper.getAllETF().get(0);
        System.out.println(etfpo);

    }
}
