package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.CallOptionVO;
import com.example.Huaqi.vo.PutOptionVO;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Override
    public ResponseVO purchaseCallOption(){
        List<CallOptionVO>Calls=new ArrayList<CallOptionVO>();
        List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
        int delta=0;

        for(int i=0;i<Calls.size();i++){
            int timePrice=Math.max(Calls.get(i).getETFPrice()-Calls.get(i).getExecPrice(),0);
            if(timePrice<0){
                List<PutOptionVO>Purchase=new ArrayList<PutOptionVO>();
                for(int j=0;j<Puts.size();j++){
                    if(Calls.get(i).getName().equals(Puts.get(j).getName())){
                        Purchase.add(Puts.get(j));
                    }
                }

                if(Purchase.size()>=delta){
                    //调用认购期权与相应认沽期权的购买API
                    int outPrice=0;
                    myThreads x=new myThreads(Calls.get(i),Purchase,outPrice);
                    x.start();
                }
            }
        }
        return ResponseVO.buildSuccess();
    }

    class myThreads extends Thread{
        CallOptionVO call;
        List<PutOptionVO> puts;
        int outPrice;

        public myThreads(CallOptionVO call,List<PutOptionVO> puts,int outPrice){
            this.call=call;
            this.puts=puts;
            this.outPrice=outPrice;
        }

        public void run(){
            Task task = new Task(call,puts,outPrice);
            FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
            Thread thread = new Thread(futureTask);
            thread.start();

            try {
                futureTask.get(10000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    class Task implements Callable<Integer> {
        CallOptionVO call;
        List<PutOptionVO> puts;
        int outPrice;

        public Task(CallOptionVO call,List<PutOptionVO> puts,int outPrice){
            this.call=call;
            this.puts=puts;
            this.outPrice=outPrice;
        }

        @Override
        public Integer call() throws Exception {
            //调用写入api
            return 0;
        }
    }

    @Override
    public ResponseVO purchasePutOption(){
        List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
        int delta=0;
        int m=0;//买入认沽期权的数量
        for(int i=0;i<Puts.size();i++){
            int timePrice=Math.max(Puts.get(i).getExecPrice()-Puts.get(i).getETFPrice(),0);
            if(timePrice<0){
                int n=m*10000;//对应应该买入50ETF的数量
                int outprice=0;//挂价
                /*
                TODO 调用买入认沽期权和50ETF的API
                 */
            }
        }
        return ResponseVO.buildSuccess();
    }

}
