package com.example.Huaqi;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.blImpl.OptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class HuaqiApplication {

//	private static Timer timer = new Timer();//创建一个Timer类的实例

	public static void main(String[] args) {
		SpringApplication.run(HuaqiApplication.class, args);
//		myTask task = new myTask();
//		timer.schedule(task, 0, 20000);//以当前基准时间延迟0秒后执行一次，以后按指定间隔时间1秒无限次数的执行。
	}
}
