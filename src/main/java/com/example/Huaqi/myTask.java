package com.example.Huaqi;

import com.example.Huaqi.bl.OptionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimerTask;

/**
 * @ClassName myTask
 * @Description TODO
 * @Author 李甘霖
 * @Date 2020/9/2216:33
 **/
public class myTask  extends TimerTask {
    @Autowired
    OptionService optionService;
    @Override
    public void run() {

    }
}
