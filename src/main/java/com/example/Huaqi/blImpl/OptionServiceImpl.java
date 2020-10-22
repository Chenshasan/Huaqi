package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.data.OptionMapper;
import com.example.Huaqi.po.OptionPO;
import com.example.Huaqi.vo.*;
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

    @Autowired
    OptionMapper optionMapper;

    List<CallOptionVO>Calls=new ArrayList<CallOptionVO>();
    List<PutOptionVO>Puts=new ArrayList<PutOptionVO>();
    ExecutorService service =Executors.newCachedThreadPool();
    public double D=-0.7;    //暂定阈值
    public int logonId=4;

    public double RemainingFund=10000000;  //剩余的资金

    @Override
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
            //System.out.println(result);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     //   System.out.println("aaa"+result);
        JSONObject startObj=new JSONObject(result);
        JSONObject res=startObj.getJSONObject("data");
     //   System.out.println(res.toString());
        return res.toString();
    }

    @Override
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
        Collections.sort(Calls);//将时间价值按升序排列，以便购买的时候从时间价值最小的期权开始
        //System.out.println("运行到此处");
        for(int i=0;i<Calls.size();i++){
           // 时间价值小于0准备购买
            //System.out.println("剩余的资金"+RemainingFund);
           // System.out.println(Calls.get(i).toString());
            if(Calls.get(i).getTimeprice()<0){
                List<PutOptionVO>purchaseList=new ArrayList<>();//认购期权对应的认沽期权List
                //认购期权对应的认沽期权，且这些期权是满足条件-1<delta<阈值的认沽期权
                for(int j=0;j<Puts.size();j++) {
                    //System.out.println("认沽期权"+Puts.get(j).toString());
                    if ((Puts.get(j).getDelta()<-0.7)&&(Puts.get(j).getDelta()<=-1)) {
                        purchaseList.add(Puts.get(j));
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

                    double Sum_money;//当前购买认购以及行权和认沽期权需要的总资金数；

                    int index = 0;//挑选出的认沽期权在purchaseList中的index
                    int m = 1;//合适的m值
                    //选出合适的m
                    while (true) {
                        //将m从1开始++1，如果有m能够满足-1*m/delta接近整数并且误差小于0.1时，则选择该m
                        double judge = -1 * m / purchaseList.get(0).getDelta();
                        //System.out.println(purchaseList.get(0).getDelta());
                        if (judge - Math.floor(judge) < 0.1 || Math.ceil(judge) - judge < 0.1) {
                            break;
                        }
                        m++;
                    }
                    int Call_num = m;    //认购购买的份数
                    int count = 0;//purchaseList里面认沽期权的总份数
                    int put_count = (int) Math.round(-1 * m / purchaseList.get(0).getDelta());//购买认沽期权的份数

                   // System.out.println("购买期权的份数"+put_count);
                    for (int p = 0; p < purchaseList.size(); p++) {
                        count = count + purchaseList.get(p).getNum();
                    }
                    //判断如果总份数都不能满足需要购买的量，满足了才继续
                    if (count > put_count&&Calls.get(i).getNum()>=Call_num) {
                        int the_count = 0;
                        int the_index = 0;
                        double put_money=0;//购买所有认沽期权需要的资金
                        //这里填充要买的认沽期权的true_purchaseList和对应每个认沽期权购买份数的List
                        while (true) {
                            true_purchaseList.add(purchaseList.get(the_index));
                            the_count = the_count + purchaseList.get(the_index).getNum();
                            if (the_count < put_count) {
                                Put_num.add(purchaseList.get(the_index).getNum());
                                //认沽总价要加上当前认沽的价格
                                put_money=put_money+purchaseList.get(the_index).getPrice()*purchaseList.get(the_index).getNum();
                                the_index++;
                            } else {
                                Put_num.add(purchaseList.get(the_index).getNum() - (the_count - put_count));
                                //认沽总价要加上当前认沽的价格
                                put_money=put_money+purchaseList.get(the_index).getPrice()*(purchaseList.get(the_index).getNum() - (the_count - put_count));
                                break;
                            }
                        }
                        //如果当前剩余资金足够那么就购买
                        //需要的资金是认沽期权的价格加上认购期权的价格以及购买50ETF的价格
                        Sum_money=put_money+Calls.get(i).getPrice()*Call_num+Calls.get(i).getETFNum()*Calls.get(i).getExecPrice();
                        if(RemainingFund>=Sum_money){
                        myThreads x = new myThreads(Calls.get(i), true_purchaseList, Call_num, Put_num,Sum_money);
                        x.start();
                        }
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
        double Sum_money;//这一次交易所需要的全部资金，以便成功交易减去资金或者撤销的时候我们账户的剩余资金能恢复

        public myThreads(CallOptionVO call,List<PutOptionVO>put,int Call_num,List<Integer>Put_num,double Sum_money){
            this.Call=call;
            this.Put=put;
            //this.Call_outprice=Call_outprice;
            //this.Put_outPrice=Put_outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
            this.Sum_money=Sum_money;
        }

        public void run(){
            Task task = new Task(Call,Put,Call_num,Put_num,Sum_money);
            FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
            Thread thread = new Thread(futureTask);
            thread.start();
            //System.out.println("_____________________________threads is running");

            try {
                futureTask.get();
            } catch (InterruptedException | ExecutionException e) {
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
        double Sum_money;//这一次交易所需要的全部资金，以便成功交易减去资金或者撤销的时候我们账户的剩余资金能恢复

        public Task(CallOptionVO call,List<PutOptionVO>put,int Call_num,List<Integer>Put_num,double Sum_money){
            this.Call=call;//要购买的认沽期权
            this.Put=put;
            //this.Call_outprice=Call_outprice;
            //this.Put_outPrice=Put_outPrice;
            this.Call_num=Call_num;
            this.Put_num=Put_num;
            this.Sum_money=Sum_money;
        }

        @Override
        public Integer call() throws Exception {
            //调用具体的购买API
            System.out.println("调用购买API");
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
            System.out.println("购买"+postConnection("http://114.212.242.163:5000/trade/torder",param1));

            //postConnection("http://114.212.242.163:5000/trade/torder",param1);

            //如果十秒之后交易没有成功（查询交易状态），则进行撤销委托的API调用
//            try{
//                Thread.sleep(10000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            String param2 = "{\n"+
                    "\"queryType\":\""+"Order\",\n" +
                    "\"options\":{\n" +
                    "\"LogonID\":\"" + logonId + "\"" +
                    "}\n" +
                    "}";
            String res2 = postConnection("http://114.212.242.163:5000/trade/tquery",param2);
            System.out.println(res2);
            JSONObject jsonObject0 = new JSONObject(res2);
            JSONArray jsonArray = jsonObject0.getJSONArray("data");


            JSONObject jsonObject = new JSONObject((String) jsonArray.get(0));
            String orderStatus = jsonObject.getString("OrderStatus");
            System.out.println("状态");
            System.out.println(orderStatus);
            int orderNum = jsonObject.getInt("OrderNumber");
           // System.out.println(orderNum);

            if(!orderStatus.equals("Normal")){
                String param3 = "\"{\n"+
                        "\"OrderNumber\":\"" + orderNum + "\"\n" +
                        "}";
                postConnection("http://114.212.242.163:5000/trade/tcancel",param3);
                //撤销以后我们剩余的资金要进行恢复
                RemainingFund=RemainingFund+Sum_money;
            }
            else{
                System.out.println("买入认沽期权");
                for(int i=0;i<Put.size();i++) {
                    int every_num = Put_num.get(i);
                    PutOptionVO p = Put.get(i);//期权

                    String param = "{\n" +
                            "\"securityCode\": \"" + p.getOptioncode() + "\",\n" +
                            "\"tradeSide\": \"Buy\",\n" +
                            "\"orderPrice\": \"" + p.getAvg1_2() + "\",\n" +
                            "\"orderVolume\": \"" + every_num + "\",\n" +
                            "\n" +
                            "\"options\": {\n" +
                            "\"OrderType\": \"LMT\",\n" +
                            "\"HedgeType\": \"SPEC\"\n" +
                            "}\n" +
                            "}";
                  System.out.println(postConnection("http://114.212.242.163:5000/trade/torder", param));
                    //交易后我们的剩余资金要减去这次交易所需要的总的资金
                    RemainingFund=RemainingFund-Sum_money;

                }
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
                postConnection("http://114.212.242.163:5000/trade/torder",param);
            }
        }
        return ResponseVO.buildSuccess();
    }

    @Override
    public ResponseVO getListRegularly() {
        Calls=new ArrayList<>();
        Puts=new ArrayList<>();
        String result1=Connection("http://114.212.242.163:5000/getList/510050.SH/20200923");

        //解析返回的query_str\query_info\query_list
        JSONObject startObj=new JSONObject(result1);
        String query_str=startObj.getString("query_str");
        JSONArray array=startObj.getJSONArray("query_info");

        String result2=Connection("http://114.212.242.163:5000/getOptions/"+query_str);
        JSONObject startObj1=new JSONObject(result2);
        JSONObject array1=startObj1.getJSONObject("status_res");

        for(int i=0;i<array.length();i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String option_code = obj.getString("option_code");//唯一标识符
                double strike_price = obj.getDouble("strike_price");//行权价
                int multiplier=obj.getInt("multiplier");//50ETF数量

                JSONObject inObject=array1.getJSONObject(option_code);
                double RT_ASK1 = inObject.getDouble("RT_ASK1");
                double RT_ASK2=inObject.getDouble("RT_ASK2");
                double price=inObject.getDouble("RT_LAST");//期权价格
                double ETF50price=inObject.getDouble("RT_USTOCK_PRICE");//50ETF价格
                double avg1_2=(RT_ASK1+RT_ASK2)/2.0;//买一买二平均值
                double thedelta=inObject.getDouble("RT_DELTA");//delta值
                int RT_LAST_AMT=inObject.getInt("RT_LAST_AMT");//50ETF的行权量
                int RT_LAST_VOL=inObject.getInt("RT_LAST_VOL");

                if(obj.getString("call_put").equals("认购")){
                    CallOptionVO callOptionVO=new CallOptionVO();
                    callOptionVO.setOptioncode(option_code);
                    callOptionVO.setExecPrice(strike_price);
                    callOptionVO.setPrice(price);
                    callOptionVO.setETFPrice(ETF50price);
                    callOptionVO.setDelta(thedelta);
                    callOptionVO.setAvg1_2(avg1_2);
                    callOptionVO.setNum(RT_LAST_AMT);
                    callOptionVO.setETFNum(RT_LAST_VOL);

                    double timePrice=callOptionVO.getPrice()-Math.max(callOptionVO.getETFPrice()-callOptionVO.getExecPrice(),0);//时间价值
                    callOptionVO.setTimeprice(timePrice);
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
                    putOptionVO.setNum(RT_LAST_AMT);
                    putOptionVO.setETFNum(RT_LAST_VOL);

                    double timePrice=putOptionVO.getPrice()-Math.max(putOptionVO.getExecPrice()-putOptionVO.getETFPrice(),0);//时间价值
                    putOptionVO.setTimeprice(timePrice);

                    Puts.add(putOptionVO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println(Calls.toString());
        //
        //
        // System.out.println(Puts.toString());
        return ResponseVO.buildSuccess();
    }

    @Override
    public int logon(){
        String param = "{\n" +
                "\"brokerId\": \"0000\",\n" +
                "\"departmentId\": \"0\",\n" +
                "\"logonAccount\": \"W631190900503\",\n" +
                "\"password\": \"000\",\n" +
                "\"accountType\": \"SHO\"\n" +
                "}";
        String res = postConnection("http://114.212.242.163:5000/trade/tlogon",param);
        JSONObject jsonObject = new JSONObject(res);
        String list = jsonObject.getString("data");
        int logonId = Integer.parseInt(list.substring(1,list.length()-1));
        return logonId;

//        String result1=Connection("http://114.212.242.163:5000/getList/510050.SH/2020-09-22");
//        System.out.println(result1);
//        //解析返回的query_str\query_info\query_list
//        JSONObject startObj=new JSONObject(result1);
//        String query_str=startObj.getString("query_str");
//        JSONArray array=startObj.getJSONArray("query_info");
//
//        String result2=Connection("http://127.0.0.1:5000/getList/"+query_str);
//        System.out.println(result2);
//        return 0;
    }

    @Override
    public void logout(int logonId){
        String param = "{\n" +
                "\"logonId\": \"" + logonId + "\"\n" +
                "}";
        String res = postConnection("http://114.212.242.163:5000/trade/tlogout",param);
        //System.out.println(res);
    }

    @Override
    public ResponseVO getOptionByETFCode(String code,String type) {
        List<OptionPO> options;
        if("call".equals(type)){
            options = optionMapper.getCallOptionByETF(code);
        }else if("put".equals(type)){
            options = optionMapper.getPutOptionByETF(code);
        }else {
            return ResponseVO.buildFailure("TypeError");
        }

        if(options.size()==0) {
            return ResponseVO.buildFailure("No Option Found");
        }else {
            return ResponseVO.buildSuccess(options);
        }
    }

    @Override
    public ResponseVO getDeltaCurve(String code) {
        List<OptionPO> options = optionMapper.getOptionByETF(code);
        if(options.size()==0) {
            return ResponseVO.buildFailure("No Option Found");
        }else {
            // create curve
            HashMap<String,Object> res = new HashMap<>();
            // 认购
            PolylinesVO call_data = new PolylinesVO();
            PolylinesVO put_data = new PolylinesVO();

            HashMap<String,HashMap<Double,Double>> call = new HashMap<>();
            HashMap<String,HashMap<Double,Double>> put = new HashMap<>();
            // key:month value:<price,delta>
            // load to 3-d map
            for(OptionPO option:options){
                String month = option.getLast_tradedate().substring(0,10).replace("-","");
                HashMap<String,HashMap<Double,Double>> target;
                HashMap<Double,Double> map;
                if(option.getCall_put().equals("认购"))
                    target = call;
                else
                    target = put;
                if(target.containsKey(month))
                    map = target.get(month);
                else
                    map = new HashMap<>();
                map.put(option.getStrike_price(),option.getDelta());
                target.put(month,map);
            }
            ArrayList<String> date = new ArrayList<>(Arrays.asList(put.keySet().toArray(new String[0])));
            ArrayList<Double> prices = null;
            // get price array
            for(String d:date){
                HashMap<Double,Double> call_tmp = call.get(d);
                ArrayList<Double> tmp = new ArrayList<>(Arrays.asList(call_tmp.keySet().toArray(new Double[0])));
                if(prices==null){
                    prices = tmp;
                }else {
                    prices.retainAll(tmp);
                }
            }
            Collections.sort(prices);
            ArrayList<ArrayList<Double>> call_ys = new ArrayList<>();
            ArrayList<ArrayList<Double>> put_ys = new ArrayList<>();
            for(String d:date){
                HashMap<Double,Double> call_tmp = call.get(d);
                HashMap<Double,Double> put_tmp = call.get(d);
                ArrayList<Double> call_y = new ArrayList<>();
                ArrayList<Double> put_y = new ArrayList<>();
                for(Double price:prices){
                    call_y.add(call_tmp.get(price));
                    put_y.add(put_tmp.get(price));
                }
                call_ys.add(call_y);
                put_ys.add(put_y);
            }
            call_data.setY_axis(call_ys);
            call_data.setX_axis(prices);
            call_data.setDate(date);
            put_data.setY_axis(put_ys);
            put_data.setX_axis(prices);
            put_data.setDate(date);
            res.put("call",call_data);
            res.put("put",put_data);
            return ResponseVO.buildSuccess(res);
        }
    }

    @Override
    public ResponseVO getTimeValueCurve(String code) {
        List<OptionPO> options = optionMapper.getOptionByETF(code);
        if(options.size()==0) {
            return ResponseVO.buildFailure("No Option Found");
        }else {
            // create curve
            HashMap<String,Object> res = new HashMap<>();
            // 认购
            PolylinesVO call_data = new PolylinesVO();
            PolylinesVO put_data = new PolylinesVO();

            HashMap<String,HashMap<Double,Double>> call = new HashMap<>();
            HashMap<String,HashMap<Double,Double>> put = new HashMap<>();
            // key:month value:<price,delta>
            // load to 3-d map
            for(OptionPO option:options){
                String month = option.getLast_tradedate().substring(0,10).replace("-","");
                HashMap<String,HashMap<Double,Double>> target;
                HashMap<Double,Double> map;
                if(option.getCall_put().equals("认购"))
                    target = call;
                else
                    target = put;
                if(target.containsKey(month))
                    map = target.get(month);
                else
                    map = new HashMap<>();
                map.put(option.getStrike_price(),option.getTime_value());
                target.put(month,map);
            }
            ArrayList<String> date = new ArrayList<>(Arrays.asList(put.keySet().toArray(new String[0])));
            ArrayList<Double> prices = null;
            // get price array
            for(String d:date){
                HashMap<Double,Double> call_tmp = call.get(d);
                ArrayList<Double> tmp = new ArrayList<>(Arrays.asList(call_tmp.keySet().toArray(new Double[0])));
                if(prices==null){
                    prices = tmp;
                }else {
                    prices.retainAll(tmp);
                }
            }
            Collections.sort(prices);
            ArrayList<ArrayList<Double>> call_ys = new ArrayList<>();
            ArrayList<ArrayList<Double>> put_ys = new ArrayList<>();
            for(String d:date){
                HashMap<Double,Double> call_tmp = call.get(d);
                HashMap<Double,Double> put_tmp = call.get(d);
                ArrayList<Double> call_y = new ArrayList<>();
                ArrayList<Double> put_y = new ArrayList<>();
                for(Double price:prices){
                    call_y.add(call_tmp.get(price));
                    put_y.add(put_tmp.get(price));
                }
                call_ys.add(call_y);
                put_ys.add(put_y);
            }
            call_data.setY_axis(call_ys);
            call_data.setX_axis(prices);
            call_data.setDate(date);
            put_data.setY_axis(put_ys);
            put_data.setX_axis(prices);
            put_data.setDate(date);
            res.put("call",call_data);
            res.put("put",put_data);
            return ResponseVO.buildSuccess(res);
        }
    }

    @Override
    public ResponseVO getOptionTradeDate(String etfcode) {
        List<String> tradeDate = optionMapper.getTradeDate(etfcode);
        if(tradeDate.size()==0) {
            return ResponseVO.buildFailure("No Option Found");
        }else {
            List<String> res = new ArrayList<>();
            tradeDate.forEach(i->res.add(i.substring(0,10).replace("-","")));
            return ResponseVO.buildSuccess(res);
        }
    }
}
