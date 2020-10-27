package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.StrategyService;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl implements StrategyService {
    @Override
    public ResponseVO cumulativeRate(String startMonth, String endMonth) {
        return null;
    }

    @Override
    public ResponseVO strategyInfo(String startMonth, String endMonth) {
        return null;
    }

    @Override
    public ResponseVO varianceAndMeanOfTotalRate() {
        return null;
    }

    @Override
    public ResponseVO strategyOverallInfo() {
        return null;
    }

    @Override
    public ResponseVO monthRank() {
        return null;
    }
}
