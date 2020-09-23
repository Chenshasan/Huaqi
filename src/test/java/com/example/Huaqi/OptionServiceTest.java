package com.example.Huaqi;

import com.example.Huaqi.bl.OptionService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OptionServiceTest extends HuaqiApplicationTests{
    @Autowired
    private OptionService optionService;

    @Test
    public void testLogon(){
        optionService.logon();
    }

    /**
     * 因为没看整个的调用逻辑，所以直接把我写的部分复制了一遍来测
     * @author syc
     * @throws JSONException
     */
    @Test
    public void testCall() throws JSONException {
        int logonId = optionService.logon();
        String param2 = "{\n"+
                "\"queryType\":\""+"Order\",\n" +
                "\"options\":{\n" +
                "\"LogonID\":\"" + logonId + "\"" +
                "}\n" +
                "}";
        String res2 = optionService.postConnection("http://114.212.242.163:5000/trade/tquery",param2);
        JSONObject jsonObject0 = new JSONObject(res2);
        JSONArray jsonArray = jsonObject0.getJSONArray("data");
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        String orderStatus = jsonObject.getString("OrderStatus");
        System.out.println(orderStatus);
        int orderNum = jsonObject.getInt("OrderNumber");
        System.out.println(orderNum);
        //optionService.logout(logonId);
        if(orderStatus.equals("Invalid")){
            //logonId = optionService.logon();
            String param3 = "\"{\n"+
                    "\"OrderNumber\":\"" + orderNum + "\"\n" +
                    "}";
            optionService.postConnection("http://127.0.0.1:5000/trade/tcancel",param3);
            //optionService.logout(logonId);
        }
        optionService.logout(logonId);
    }

    @Test
    public void testConnection() throws JSONException {
        optionService.getListRegularly();
    }

    @Test
    public void allTest() throws JSONException {
        optionService.getListRegularly();
        optionService.purchasePutOption();
        optionService.purchaseCallOption();
    }

    @Test
    public void testLogOut() throws JSONException {
        optionService.logout(3);
    }

    @Test
    public void testOrder() throws JSONException {
        String param="{\n" +
                "\"securityCode\": \""+"10002736.SH"+"\",\n" +
                "\"tradeSide\": \"Buy\",\n" +
                "\"orderPrice\": \""+0.57+"\",\n" +
                "\"orderVolume\": \""+1+"\",\n" +
                "\n" +
                "\"options\": {\n" +
                "\"OrderType\": \"LMT\",\n" +
                "\"HedgeType\": \"SPEC\"\n" +
                "}\n" +
                "}";
        optionService.postConnection("http://114.212.242.163:5000/trade/torder",param);
    }

    @Test
    public void testQuery() throws JSONException {
        String param2 = "{\n"+
                "\"queryType\":\""+"Order\",\n" +
                "\"options\":{\n" +
                "\"LogonID\":\"" + 1 + "\"" +
                "}\n" +
                "}";
        String res2 = optionService.postConnection("http://114.212.242.163:5000/trade/tquery",param2);
        JSONObject jsonObject0 = new JSONObject(res2);
        JSONArray jsonArray = jsonObject0.getJSONArray("data");
        System.out.println(jsonArray);
    }

    @Test
    public void testCancel() throws JSONException {
        String param3 = "{\n" +
                "    \"OrderNumber\": \"5\",\n" +
                "    \"options\": {\n" +
                "        \"LogonId\": \"1\"\n" +
                "    }\n" +
                "}";
        optionService.postConnection("http://114.212.242.163:5000/trade/tcancel",param3);
    }
}
