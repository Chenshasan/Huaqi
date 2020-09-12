package com.example.Huaqi.blImpl;

import com.example.Huaqi.bl.OptionService;
import com.example.Huaqi.vo.CallOption;
import com.example.Huaqi.vo.PutOption;
import com.example.Huaqi.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OptionServiceImpl implements OptionService {

    @Override
    public ResponseVO purchaseCallOption(){
        List<CallOption>Calls=new ArrayList<CallOption>();
        List<PutOption>Puts=new ArrayList<PutOption>();
        int delta=0;

        List<CallOption>PurchaseCalls=new ArrayList<CallOption>();//
        for(int i=0;i<Calls.size();i++){
            int timePrice=Math.max(Calls.get(i).getETFPrice()-Calls.get(i).getExecPrice(),0);
            if(timePrice<0){
                
            }

        }



        return ResponseVO.buildSuccess();

    }

    @Override
    public ResponseVO purchasePutOption(){



        return ResponseVO.buildSuccess();
    }




}
