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
        int res = optionService.logon();
        System.out.println(res);
        optionService.logout(res);
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


}
