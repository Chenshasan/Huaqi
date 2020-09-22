package com.example.Huaqi.bl;
import com.example.Huaqi.vo.ResponseVO;

public interface OptionService {
    /**
     * 实现自动购买认购期权
     * @param
     * @return
     */
    ResponseVO purchaseCallOption();

    /**
     * 实现自动购买认沽期权
     * @param
     * @return
     */
    ResponseVO purchasePutOption();

    /**
     * 登录进入wind账户
     * @param
     * @return
     */
    ResponseVO login();

    /**
     * 周期性获取数据
     * @param
     * @return
     */
    ResponseVO getListRegularly();

    String postConnection(String url, String jsonString);

    int logon();

    void logout(int logonId);
}
