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
     * 周期性获取数据
     * @param
     * @return
     */
    ResponseVO getListRegularly();

    String postConnection(String url, String jsonString);
    String Connection(String url);

    /**
     * 登录
     * @param
     * @return
     */
    int logon();

    /**
     * 登出
     * @param
     * @return
     */
    void logout(int logonId);
}
