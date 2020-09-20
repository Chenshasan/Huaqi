package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.CallOptionVO;
import com.example.Huaqi.vo.PutOptionVO;
import com.example.Huaqi.vo.ResponseVO;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Date: 2020-09-10
 */
@Service
public class OptionServiceImpl implements OptionService {
    List<CallOptionVO>Calls=new ArrayList<CallOptionVO>();
    List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
    public double D=0.7;    //暂定阈值

    public OptionServiceImpl() throws FileNotFoundException {
        //参数是一个日期，用来确认需要拿哪一天的期权
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) path = new File("");
        String p=path.getAbsolutePath();
        p=p.substring(0,p.length()-20);
        p=p+"\\src\\main\\java\\com\\example\\Huaqi\\blImpl\\testMock.py";
        System.out.println("path:"+p);
        String[] arguments = new String[] {"python", p,"2020-09-16"};
        String Json="";
        try {
            Process process = Runtime.getRuntime().exec(arguments);//调用python
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            //数据都在in里面
            String line = null;
            while ((line = in.readLine()) != null) {
                Json=Json+line;
            }
            in.close();
            System.out.println(Json);
            //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
            //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
//            int re = process.waitFor();
//            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        System.out.println(Calls.toString());
        System.out.println(Puts.toString());
    }

    public void TestConnection(){
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //2.生成一个get请求
        HttpGet httpget = new HttpGet("http://127.0.0.1:5000/getListMock/date");
        CloseableHttpResponse response = null;

        try {
            //3.执行get请求并返回结果
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;

        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
            System.out.println(result);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResponseVO purchaseCallOption(){
        for(int i=0;i<Calls.size();i++){
            double timePrice=Math.max(Calls.get(i).getETFPrice()-Calls.get(i).getExecPrice(),0);//时间价值
            if(timePrice<0){
                List<PutOptionVO>purchaseList=new ArrayList<>();//认购期权对应的认沽期权List
                for(int j=0;j<Puts.size();j++){
                    if(Calls.get(i).getOptioncode().equals(Puts.get(j).getOptioncode())){
                        purchaseList.add(Puts.get(i));
                        }
                    }
                /*
                对于时间价值为负的认购期权，挑出它对应的认沽期权并按delta的升序排序
                这样就能顺序从List中取出delta值尽可能小的期权购买
                若当前delta值对应的期权数量不够，那么就取第二小的，以此类推
                 */
                Collections.sort(purchaseList);
                //如果挑出了对应的认购期权，而且最小的delta也达到暂定阈值D的要求，那么进行本次交易
                if(purchaseList.size()!=0&&purchaseList.get(0).getDelta()<-0.7){
                    int Call_num=0;    //认购购买的份数
                    int Put_num=0;     //认沽购买的份数
                    double Call_outprice=Calls.get(i).getAvg1_2();//买入挂价
                    myThreads x=new myThreads(Calls.get(i),purchaseList,Call_outprice,Call_num,Put_num);
                    x.start();
                }
                }
            }

        return ResponseVO.buildSuccess();
    }

    class myThreads extends Thread{
        CallOptionVO call;
        List<PutOptionVO> puts;
        double outPrice;
        int Call_num;    //认购购买的份数
        int Put_num;     //认沽购买的份数

        public myThreads(CallOptionVO call,List<PutOptionVO> puts,double outPrice,int Call_num,int Put_num){
            this.call=call;
            this.puts=puts;
            this.outPrice=outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
        }

        public void run(){
            Task task = new Task(call,puts,outPrice,Call_num,Put_num);
            FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
            Thread thread = new Thread(futureTask);
            thread.start();
            System.out.println("threads is running");

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
        double outPrice;
        int Call_num;    //认购购买的份数
        int Put_num;     //认沽购买的份数

        public Task(CallOptionVO call,List<PutOptionVO> puts,double outPrice,int Call_num,int Put_num){
            this.call=call;//要购买的认沽期权
            /*
            这里的认沽期权是一个List是因为delta最小值的期权份数可能不够，就需要用delta第二小的期权来补足这个份数，以此类推
             */
            this.puts=puts;
            this.outPrice=outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
        }

        @Override
        public Integer call() throws Exception {
            //调用买入api，写入买入期权和购买数量
            return 0;
        }
    }


    @Override
    public ResponseVO purchasePutOption(){
        List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
        int m=0;//买入认沽期权的数量
        for(int i=0;i<Puts.size();i++){
            double timePrice=Math.max(Puts.get(i).getExecPrice()-Puts.get(i).getETFPrice(),0);
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
