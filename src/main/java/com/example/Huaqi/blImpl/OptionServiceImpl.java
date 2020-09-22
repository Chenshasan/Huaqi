package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.CallOptionVO;
import com.example.Huaqi.vo.PutOptionVO;
import com.example.Huaqi.vo.ResponseVO;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
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

    public String Connection(String url){
        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //2.生成一个get请求
        HttpGet httpget = new HttpGet(url);
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
        JSONObject startObj=new JSONObject(result);
        JSONObject res=startObj.getJSONObject("data");
        System.out.println(res.toString());
        return res.toString();
    }

    public String postConnection(String url, String jsonString){
        //1.获得一个httpclient对象
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();//创建CloseableHttpClient
        HttpPost httpPost = new HttpPost(url);//实现HttpPost
        httpPost.addHeader("Content-Type", "application/json");//设置httpPost的请求头中的MIME类型为json
        StringEntity requestEntity = new StringEntity(jsonString, "utf-8");
        httpPost.setEntity(requestEntity);//设置请求体

        try {
            //3.执行get请求并返回结果
            response = httpClient.execute(httpPost, new BasicHttpContext());//执行请求返回结果
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
        return result;
    }

    @Override
    public ResponseVO purchaseCallOption(){
        for(int i=0;i<Calls.size();i++){
            double timePrice=Math.max(Calls.get(i).getETFPrice()-Calls.get(i).getExecPrice(),0);//时间价值
            if(timePrice<0){
                List<PutOptionVO>purchaseList=new ArrayList<>();//认购期权对应的认沽期权List
                //认购期权对应的认沽期权，且这些期权是满足条件-1<delta<阈值的认沽期权
                for(int j=0;j<Puts.size();j++) {
                    if ((-1 < Puts.get(j).getDelta() && Puts.get(j).getDelta() < D)) {
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
                    int Call_num = m;    //认购购买的份数
                    int count = 0;//purchaseList里面认沽期权的总份数
                    int put_count = (int) Math.round(-1 * m / purchaseList.get(0).getDelta());//购买认沽期权的份数
                    for (int p = 0; p < purchaseList.size(); p++) {
                        count = count + purchaseList.get(p).getNum();
                    }
                    //判断如果总份数都不能满足需要购买的量，满足了才继续
                    if (count > put_count&&Calls.get(i).getNum()>=Call_num) {
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
            String param1="{\n" +
                    "\"securityCode\": \""+Call.getOptioncode()+"\",\n" +
                    "\"tradeSide\": \"Buy\",\n" +
                    "\"orderPrice\": \""+Call.getAvg1_2()+"\",\n" +
                    "\"orderVolume\": \""+Call_num+"\",\n" +
                    "\n" +
                    "\"options\": {\n" +
                    "\"OrderType\": \"LMT\",\n" +
                    "\"HedgeType\": \"SPEC\"\n" +
                    "}\n" +
                    "}";

            postConnection("http://127.0.0.1:5000/trade/torder",param1);
            //postConnection("http://114.212.242.163:5000/trade/torder",param1);

            //TODO 如果十秒之后交易没有成功（查询交易状态），则进行撤销委托的API调用
            try{
                Thread.currentThread().sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
            int logonId = logon();
            String param2 = "{\n"+
                    "\"queryType\":\""+"Order\",\n" +
                    "\"options\":{\n" +
                    "\"LogonID\":\"" + logonId + "\"" +
                    "}\n" +
                    "}";
            String res2 = postConnection("http://114.212.242.163:5000/trade/tquery",param2);
            JSONObject jsonObject0 = new JSONObject(res2);
            JSONArray jsonArray = jsonObject0.getJSONArray("data");
            JSONObject jsonObject = new JSONObject((String) jsonArray.get(0));
            String orderStatus = jsonObject.getString("OrderStatus");
            System.out.println(orderStatus);
            int orderNum = jsonObject.getInt("OrderNumber");
            System.out.println(orderNum);
            if(orderStatus.equals("Invalid")){
                String param3 = "\"{\n"+
                        "\"OrderNumber\":\"" + orderNum + "\"\n" +
                        "}";
                postConnection("http://127.0.0.1:5000/trade/tcancel",param3);
            }
            logout(logonId);

            for(int i=0;i<Put.size();i++) {
                int every_num = Put_num.get(i);
                PutOptionVO p = Put.get(i);//期权

                String param="{\n" +
                        "\"securityCode\": \""+p.getOptioncode()+"\",\n" +
                        "\"tradeSide\": \"Buy\",\n" +
                        "\"orderPrice\": \""+p.getAvg1_2()+"\",\n" +
                        "\"orderVolume\": \""+every_num+"\",\n" +
                        "\n" +
                        "\"options\": {\n" +
                        "\"OrderType\": \"LMT\",\n" +
                        "\"HedgeType\": \"SPEC\"\n" +
                        "}\n" +
                        "}";

                postConnection("http://127.0.0.1:5000/trade/torder",param);
            }
            return 0;
        }
    }


    @Override
    public ResponseVO purchasePutOption(){
        List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
        int m=1;//买入认沽期权的数量
        for(int i=0;i<Puts.size();i++){
            double timePrice=Math.max(Puts.get(i).getExecPrice()-Puts.get(i).getETFPrice(),0);
            if(timePrice<0){
                int n=m*10000;//对应应该买入50ETF的数量
                String param="{\n" +
                        "\"securityCode\": \""+Puts.get(i).getOptioncode()+"\",\n" +
                        "\"tradeSide\": \"Buy\",\n" +
                        "\"orderPrice\": \""+Puts.get(i).getAvg1_2()+"\",\n" +
                        "\"orderVolume\": \""+n+"\",\n" +
                        "\n" +
                        "\"options\": {\n" +
                        "\"OrderType\": \"LMT\",\n" +
                        "\"HedgeType\": \"SPEC\"\n" +
                        "}\n" +
                        "}";

                postConnection("http://127.0.0.1:5000/trade/torder",param);
            }
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO login() {
            Connection("http://114.212.242.163:5000/getList/510050.SH/2020-09-22");
//
//        String param="{\n" +
//                "    \"brokerId\": \"0000\",\n" +
//                "    \"departmentId\": \"0\",\n" +
//                "    \"logonAccount\": \"W5814909233703\",\n" +
//                "    \"password\": \"000\",\n" +
//                "    \"accountType\": \"SHO\"\n" +
//                "}\n";
//        postConnection("http://127.0.0.1:5000/trade/tlogon",param);
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO getListRegularly() {
        Calls=new ArrayList<>();
        Puts=new ArrayList<>();
        String result1=Connection("http://127.0.0.1:5000/getList/510050.SH/20200923");

        //解析返回的query_str\query_info\query_list
        JSONObject startObj=new JSONObject(result1);
        String query_str=startObj.getString("query_str");
        JSONArray array=startObj.getJSONArray("query_info");

        String result2=Connection("http://127.0.0.1:5000/getList/"+query_str);
        JSONObject startObj1=new JSONObject(result2);
        JSONObject array1=startObj1.getJSONObject("status_res");

        for(int i=0;i<array.length();i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String option_code = obj.getString("option_code");//唯一标识符
                double strike_price = obj.getDouble("strike_price");//行权价

                JSONObject obj1=array1.getJSONObject(option_code);
                JSONObject inObject = obj1.getJSONObject("curr_status");
                double RT_BID1 = inObject.getDouble("RT_BID1");
                double RT_BID2=inObject.getDouble("RT_BID2");
                double price=inObject.getDouble("RT_LAST");//期权价格
                double ETF50price=inObject.getDouble("RT_USTOCK_PRICE");//50ETF价格
                double avg1_2=(RT_BID1+RT_BID2)/2.0;//买一买二平均值
                double thedelta=inObject.getDouble("RT_DELTA");//delta值

                if(obj.getString("call_put").equals("认购")){
                    CallOptionVO callOptionVO=new CallOptionVO();
                    callOptionVO.setOptioncode(option_code);
                    callOptionVO.setExecPrice(strike_price);
                    callOptionVO.setPrice(price);
                    callOptionVO.setETFPrice(ETF50price);
                    callOptionVO.setDelta(thedelta);
                    callOptionVO.setAvg1_2(avg1_2);
                    Calls.add(callOptionVO);
                }
                if(obj.getString("call_put").equals("认沽")){
                    PutOptionVO putOptionVO=new PutOptionVO();
                    putOptionVO.setOptioncode(option_code);
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
        return ResponseVO.buildSuccess();
    }

    public int logon(){
        String param = "{\n" +
                "\"brokerId\": \"0000\",\n" +
                "\"departmentId\": \"0\",\n" +
                "\"logonAccount\": \"W5814909233703\",\n" +
                "\"password\": \"000\",\n" +
                "\"accountType\": \"SHO\"\n" +
                "}";
        String res = postConnection("http://114.212.242.163:5000/trade/tlogon",param);
        JSONObject jsonObject = new JSONObject(res);
        String list = jsonObject.getString("data");
        int logonId = Integer.parseInt(list.substring(1,list.length()-1));
        return logonId;
    }

    public void logout(int logonId){
        String param = "{\n" +
                "\"logonId\": \"" + logonId + "\"\n" +
                "}";
        String res = postConnection("http://114.212.242.163:5000/trade/tlogout",param);
        System.out.println(res);
    }

}
