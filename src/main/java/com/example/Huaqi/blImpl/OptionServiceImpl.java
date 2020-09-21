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
                //认购期权对应的认沽期权，且这些期权是满足条件-1<delta<阈值的认沽期权
                for(int j=0;j<Puts.size();j++) {
                    if ((Calls.get(i).getOptioncode().equals(Puts.get(j).getOptioncode())) && (-1 < Puts.get(j).getDelta() && Puts.get(j).getDelta() < D)) {
                        purchaseList.add(Puts.get(i));
                    }
                }
                /*
                对于时间价值为负的认购期权，挑出它对应的认沽期权并按delta的升序排序
                这样就能顺序从List中取出delta值尽可能小的期权购买
                若当前delta值对应的期权数量不够，那么就取第二小的，以此类推
                 */
                Collections.sort(purchaseList);
                //如果有满足条件的认沽期权
                if(purchaseList.size()!=0) {
                    List<PutOptionVO> true_purchaseList = new ArrayList<>();//要购买的认沽期权List
                    List<Integer> Put_num = new ArrayList<>();//要购买的认沽期权份数对应的List,这里的数和对应的认沽期权List一一对应
                    List<Double> Put_outPrice = new ArrayList<>();//认沽期权买入的挂价List,同上
                    int Call_num = 0;    //认购购买的份数
                    double Call_outprice = Calls.get(i).getAvg1_2();//认购期权买入挂价

                    int index = 0;//挑选出的认沽期权在purchaseList中的index
                    int m = 1;//合适的m值
                    //选出合适的m
                    while (true) {
                        //将m从1开始++1，如果有m能够满足-1*m/delta接近整数并且误差小于0.1时，则选择该m
                        double judge = -1 * m / purchaseList.get(0).getDelta();
                        if (judge - Math.floor(judge) < 0.1 || Math.ceil(judge) - judge < 0.1) {
                            break;
                        }
                        m++;
                    }
                    int count = 0;//purchaseList里面认沽期权的总份数
                    int put_count = (int) Math.round(-1 * m / purchaseList.get(0).getDelta());//购买认沽期权的份数
                    for (int p = 0; p < purchaseList.size(); p++) {
                        count = count + purchaseList.get(p).getNum();
                    }
                    //判断如果总份数都不能满足需要购买的量，满足了才继续
                    if (count > put_count) {
                        int the_count = 0;
                        int the_index = 0;
                        //这里填充要买的认沽期权的true_purchaseList和对应每个认沽期权购买份数的List
                        while (true) {
                            true_purchaseList.add(purchaseList.get(the_index));
                            the_count = the_count + purchaseList.get(the_index).getNum();
                            if (the_count <= put_count) {
                                Put_num.add(purchaseList.get(the_index).getNum());
                                the_index++;
                            } else {
                                Put_num.add(purchaseList.get(the_index).getNum() - (the_count - put_count));
                                break;
                            }
                        }
                        myThreads x = new myThreads(Calls.get(i), true_purchaseList, Call_num, Put_num);
                        x.start();
                    }
                }
            }
        }
        return ResponseVO.buildSuccess();
    }

    class myThreads extends Thread{
        CallOptionVO Call;
        //double Call_outprice;
        int Call_num;    //认购购买的份数
        List<PutOptionVO>Put;
        //List<Double>Put_outPrice;
        List<Integer>Put_num;

        public myThreads(CallOptionVO call,List<PutOptionVO>put,int Call_num,List<Integer>Put_num){
            this.Call=call;
            this.Put=put;
            //this.Call_outprice=Call_outprice;
            //this.Put_outPrice=Put_outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
        }

        public void run(){
            Task task = new Task(Call,Put,Call_num,Put_num);
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
        CallOptionVO Call;
        //double Call_outprice;
        int Call_num;    //认购购买的份数
        List<PutOptionVO>Put;
        //List<Double>Put_outPrice;
        List<Integer>Put_num;

        public Task(CallOptionVO call,List<PutOptionVO>put,int Call_num,List<Integer>Put_num){
            this.Call=call;//要购买的认沽期权
            this.Put=put;
            //this.Call_outprice=Call_outprice;
            //this.Put_outPrice=Put_outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
        }

        @Override
        public Integer call() throws Exception {
            //调用具体的购买API
            /*
            这里的认沽期权及其购买的份数都是List
            调用API的时候用for循环分别购买List的每一个
            for(int i=0;i<Put.size();i++){
                int every_num=Put_num.get(i);
                PutOptionVO p=Put.get(i);//期权
            }
             */

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
