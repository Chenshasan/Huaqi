package com.example.Huaqi.blImpl;

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
public class OptionServiceImpl implements OrderService {
    private final static String RESERVE_ERROR = "预订失败";
    private final static String ROOMNUM_LACK = "预订房间数量剩余不足";
    private final static String ORDERSTATE_ERROR = "订单状态异常";
    private final static String ANNUL_ERROR = "订单删除失败";
    private final static String EXEC_ERROR = "入住失败";
    private final static String CHECKOUT_ERROR = "退房失败";
    private final static String NO_CREDIT="信用值不足";
    private final static String REPEAT_COMMENT="此订单已评论";
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CreditRecordMapper creditRecordMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public ResponseVO addOrder(OrderVO orderVO) {
        int reserveRoomNum = orderVO.getRoomNum();
        int curNum = hotelService.getRoomCurNum(orderVO.getHotelId(),orderVO.getRoomType());
        Double credit=accountService.getUserInfo(orderVO.getUserId()).getCredit();
        Integer intCredit=credit.intValue();
        if (intCredit<0){
            return ResponseVO.buildFailure(NO_CREDIT);
        }
        if(reserveRoomNum>curNum){
            return ResponseVO.buildFailure(ROOMNUM_LACK);
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState("已预订");
            UserVO user = accountService.getUserInfo(orderVO.getUserId());
            orderVO.setClientName(user.getUserName());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            orderMapper.addOrder(order);
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(RESERVE_ERROR);
        }
       return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getUserOrders(int userid) {

        List<Order> orders= orderMapper.getUserOrders(userid);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempTime=df.format(new Date());
        for(int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            String lastTime=order.getCheckInDate();
            int a=lastTime.compareTo(tempTime);
            if((order.getOrderState().equals("已预订"))&&(a<=0)) {
                orderMapper.overTime(order.getId());
                order.setOrderState("异常");
                //System.out.print("here");
                CreditRecord creditRecord=new CreditRecord();
                creditRecord.setAction(CreditActionType.abnormal);
                creditRecord.setCredit(-order.getPrice().intValue());
                creditRecord.setOrder_id(order.getId());
                creditRecord.setUser_id(order.getUserId());
                creditRecord.setTime(tempTime);
                creditRecordMapper.addRecord(creditRecord);
                accountMapper.updateCredit(order.getUserId(),-order.getPrice().intValue());
                hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),-order.getRoomNum());
            }
        }
        return orders;
    }

    @Override
    public ResponseVO annulOrder(int orderid) {
        Order order=orderMapper.getOrderById(orderid);
        try{
            if(!order.getOrderState().equals("已预订")){
                return ResponseVO.buildFailure(ORDERSTATE_ERROR);
            }
            OrderVO orderVO=new OrderVO();
            BeanUtils.copyProperties(order,orderVO);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tempTime=df.format(new Date());
            orderMapper.annulOrder(orderid);
            CreditRecord creditRecord=new CreditRecord();
            creditRecord.setAction(CreditActionType.annul);
            creditRecord.setCredit(-(order.getPrice().intValue()/2));
            creditRecord.setOrder_id(order.getId());
            creditRecord.setUser_id(order.getUserId());
            creditRecord.setTime(tempTime);
            creditRecordMapper.addRecord(creditRecord);
            accountMapper.updateCredit(order.getUserId(),-(order.getPrice().intValue()/2));
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),-orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ANNUL_ERROR);
    }
        return ResponseVO.buildSuccess(true);
    }

    /**
     * @param hotelId
     * @return
     */
    @Override
    public List<Order> getHotelOrders(Integer hotelId) {
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().filter(order -> order.getHotelId().equals(hotelId)).collect(Collectors.toList());
    }

    @Override
    public List<HotelVO> getUserHotels(int userid) {
        List<Order> temp=orderMapper.getUserHotels(userid);
        List<Integer> para = new ArrayList<>();
        for (int i=0;i<temp.size();i++){
            para.add(temp.get(i).getHotelId());
        }
        if (para.size()==0) return new ArrayList<HotelVO>();
        return hotelMapper.getUserHotels(para);
    }

    @Override
    public List<Order> getUserOrdersInOneHotel(int userid, int hotelid) {
        return orderMapper.getUserOrdersInOneHotel(userid,hotelid);
    }

    @Override
    public ResponseVO executeOrder(int orderid) {
        Order order=orderMapper.getOrderById(orderid);
        try{
            if(!order.getOrderState().equals("已预订")){
                return ResponseVO.buildFailure(ORDERSTATE_ERROR);
            }
            OrderVO orderVO=new OrderVO();
            BeanUtils.copyProperties(order,orderVO);
            orderMapper.executeOrder(orderid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(EXEC_ERROR);
        }
        CreditRecord creditRecord=new CreditRecord();
        creditRecord.setUser_id(order.getUserId());
        creditRecord.setOrder_id(orderid);
        creditRecord.setCredit(order.getPrice().intValue());
        creditRecord.setAction(CreditActionType.execute);
                //valueOf("订单执行"));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        creditRecord.setTime(time);
        creditRecordMapper.addRecord(creditRecord);
        accountMapper.updateCredit(order.getUserId(),order.getPrice().intValue());
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO checkOut(int orderid) {
        Order order=orderMapper.getOrderById(orderid);
        try{
            if(!order.getOrderState().equals("已入住")){
                return ResponseVO.buildFailure(ORDERSTATE_ERROR);
            }
            OrderVO orderVO=new OrderVO();
            BeanUtils.copyProperties(order,orderVO);
            orderMapper.checkOut(orderid);
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),-orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(CHECKOUT_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public Comment getOrderComment(Integer orderid){
        return commentMapper.selectByOrder(orderid);
    }

    @Override
    public ResponseVO addComment(CommentVO commentVO){
        Integer order_id=commentVO.getOrder_id();
        Integer user_id=commentVO.getUser_id();
        List<Comment> comments=commentMapper.selectByUser(user_id);

        int n=comments.size();
        for(int i=0;i<n;i++){
            if(comments.get(i).getOrder_id()==order_id) return ResponseVO.buildFailure(REPEAT_COMMENT);
        }
        Comment comment=new Comment();
        Integer hotelId=commentVO.getHotel_id();
        double rate=commentVO.getRate();
        HotelVO hotel=hotelMapper.getHotelById(hotelId);
        double oldRate=hotel.getRate();
        Integer rateNum=commentMapper.selectByHotel(hotelId).size();
        double newRate=(rate+rateNum*oldRate)/(rateNum+1.0);
        hotel.setRate(newRate);
        hotelService.updateHotelInfo(hotel);
        BeanUtils.copyProperties(commentVO,comment);
        commentMapper.addComment(comment);
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAbnormalOrderList() {
        List<Order> orders = orderService.getAllOrders();
        return orders.stream().filter(order -> order.getOrderState().equals("异常")).collect(Collectors.toList());
    }

    @Override
    public Order getOrderById(int orderid) {
        return orderMapper.getOrderById(orderid);
    }

    @Override
    public List<Order> getAllHotelOrders(Integer userId){
        Integer hotelId=hotelMapper.selectManagerHotel(userId).getId();
        List<Order> orders=orderMapper.getAllOrders();
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getHotelId()!=hotelId) orders.remove(i--);
        }
        return orders;
    }

}
