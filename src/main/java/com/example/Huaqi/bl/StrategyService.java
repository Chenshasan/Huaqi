package com.example.Huaqi.bl;

import com.example.Huaqi.vo.ResponseVO;


public interface StrategyService {
    /**
     * 根据时间区间(月份)获取累计收益率和大盘的对比数据
     * @param startMonth
     * @param endMonth
     * @return
     */
    ResponseVO cumulativeRate(String startMonth, String endMonth);

    /**
     * 根据时间区间(月份)获取策略信息
     * @param startMonth
     * @param endMonth
     * @return
     */
    ResponseVO strategyInfo(String startMonth, String endMonth);

    /**
     * 获取累计收益率的方差与均值（与其他理财产品做比较）
     * @return
     */
    ResponseVO varianceAndMeanOfTotalRate();

    /**
     * 获取策略整体信息
     * @return
     */
    ResponseVO strategyOverallInfo();

    /**
     * 获取数据的月份范围
     * @return
     */
    ResponseVO monthRank();
}
