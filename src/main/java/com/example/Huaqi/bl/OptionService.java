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
}
