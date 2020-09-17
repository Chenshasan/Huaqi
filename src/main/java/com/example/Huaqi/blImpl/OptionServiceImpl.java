package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.CallOptionVO;
import com.example.Huaqi.vo.PutOptionVO;
import com.example.Huaqi.vo.ResponseVO;
import org.json.JSONArray;
import org.json.JSONObject;
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
 * @Date: 2020-09-10
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Override
    public ResponseVO purchaseCallOption(){
        List<CallOptionVO>Calls=new ArrayList<CallOptionVO>();
        List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
        String Json="";
        JSONArray array=new JSONArray(Json);
        for(int i=0;i<array.length();i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String us_code = obj.getString("us_code");//唯一标识符
                double strike_price = obj.getDouble("strike_price");//行权价

                JSONObject inObject = obj.getJSONObject("curr_status");
                double RT_BID1 = inObject.getDouble("RT_BID1");
                double RT_BID2=inObject.getDouble("RT_BID2");
                double price=inObject.getDouble("RT_LAST");//期权价格
                double ETF50price=inObject.getDouble("RT_USTOCK_PRICE");//50ETF价格
                double avg1_2=(RT_BID1+RT_BID2)/2.0;//买一买二平均值
                double thedelta=inObject.getDouble("RT_DELTA");//delta值
                if(obj.getString("call_put").equals("认购")){
                    CallOptionVO callOptionVO=new CallOptionVO();
                    callOptionVO.setOptioncode(us_code);
                    callOptionVO.setExecPrice(strike_price);
                    callOptionVO.setPrice(price);
                    callOptionVO.setETFPrice(ETF50price);
                    callOptionVO.setDelta(thedelta);
                    callOptionVO.setAvg1_2(avg1_2);
                    Calls.add(callOptionVO);
                }
                if(obj.getString("call_put").equals("认沽")){
                    PutOptionVO putOptionVO=new PutOptionVO();
                    putOptionVO.setOptioncode(us_code);
                    putOptionVO.setExecPrice(strike_price);
                    putOptionVO.setPrice(price);
                    putOptionVO.setETFPrice(ETF50price);
                    putOptionVO.setDelta(thedelta);
                    putOptionVO.setAvg1_2(avg1_2);
                    Puts.add(putOptionVO);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<Calls.size();i++){
            double timePrice=Math.max(Calls.get(i).getETFPrice()-Calls.get(i).getExecPrice(),0);
            if(timePrice<0){
                int selectIndex=-1;
                for(int j=0;j<Puts.size();j++){
                    if(Calls.get(i).getOptioncode().equals(Puts.get(j).getOptioncode())){
                        if(selectIndex==-1){
                            selectIndex=j;
                        }
                        else{
                            if(Puts.get(j).getDelta()<=Puts.get(selectIndex).getDelta()){
                                selectIndex=j;
                            }
                        }
                    }
                }

                if(Purchase.size()>=Calls.get(i).getDelta()){
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
