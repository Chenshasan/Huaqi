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

    /**
     * 根据标的物ETF_code 检索对应期权列表
     * @param code
     * @return
     */
    ResponseVO getOptionByETFCode(String code,String type);

    /**
     * 计算Delta曲线
     * @param code
     * @return
     */
    ResponseVO getDeltaCurve(String code);

    /**
     * 计算时间价值曲线
     * @param code
     * @return
     */
    ResponseVO getTimeValueCurve(String code);

    /**
     * 获得期权的交割时间
     * @param optionCode
     * @return
     */
    ResponseVO getOptionTradeDate(String etfcode);
}
