# sql

> 未特殊说明，皆为NOT NULL

### etf

| column      | name       | type        | 备注            |
| ----------- | ---------- | ----------- | --------------- |
| etf_code    | 键         | varchar(32) | key,510050.SH   |
| etf_name    | 期权名词   | varchar(32) | 如华夏上证50ETF |
| rt_last     | 现价       | double      |                 |
| rt_chg      | 涨跌       | double      |                 |
| rt_pcg_chg  | 涨跌幅     | double      |                 |
| rt_open     | 今开       | double      |                 |
| rt_high     | 最高       | double      |                 |
| rt_low      | 最低       | double      |                 |
| rt_vol      | 成交量     | double      |                 |
| rt_amt      | 成交额     | double      |                 |
| option_vol  | 期权成交量 | double      | wss             |
| option_op   | 期权持仓量 | double      | wss             |
| valid       | 有效位     | int         | 1有效，0无效    |
| update_time | 更新时间   | datetime    |                 |

```
data = w.wsq("510050.SH", "rt_last,rt_chg,rt_pct_chg,rt_open,rt_high,rt_low,rt_vol,rt_amt,rt_opt_vs")

w.wss("510050.SH", "optionvolume,optionoi","unit=1;tradeDate=20201016")
```

![image-20201017235808835](https://img2020.cnblogs.com/blog/1958143/202010/1958143-20201018101852692-456219512.png)

### option_list

期权列表

基本是死数据，不用管

| column          | name         | type        | 备注                    |
| --------------- | ------------ | ----------- | ----------------------- |
| option_code     | 期权代码     | varchar(32) | 如10002423.SH，key      |
| option_name     | 期权名       | varchar(32) | 50ETF购9月2.35          |
| option_var      | 期权品种     | varchar(32) | 都是510050OP.SH，不展示 |
| us_code         | 标的物代码   | varchar(32) | 都是510050.SH，不展示   |
| us_name         | 标的物名     | varchar(32) | 华夏上证50ETF，不展示   |
| exe_type        | 期权类型     | varchar(32) |                         |
| strike_price    | 行权价       | double      | ！重要                  |
| strike_month    | 交割月份     | varchar(32) | e.g. 202010             |
| call_put        | 期权类型     | varchar(32) | 认购or认沽              |
| first_tradedate | 起始交易日期 | datetime    |                         |
| last_tradedate  | 最后交易日期 | datetime    |                         |

```
w.wset("optionchain","date=2020-10-17;us_code=510050.SH;option_var=全部;call_put=全部")
```

### 续上表

期权实时数据

每日数据，每天12点爬一次就可以

| column           | name     | type     | 备注                  |
| ---------------- | -------- | -------- | --------------------- |
| change_percent   | 涨跌幅   | double   |                       |
| amount           | 成交额   | int      |                       |
| pre_settle       | 前结算价 | double   |                       |
| open_price       | 开盘价   | double   |                       |
| highest          | 最高价   | double   |                       |
| lowest           | 最低价   | double   |                       |
| close_price      | 收盘价   | double   |                       |
| settlement_price | 结算价   | double   |                       |
| volume           | 成交量   | int      |                       |
| position_vol     | 持仓量   | int      | 总和=etf中的option_op |
| delta            |          | double   |                       |
| in_value         | 内在价值 | double   | from wss              |
| time_value       | 时间价值 | double   | from wss              |
| update_time      | 更新日期 | datetime |                       |
| valid            | 有效位   | int      | valid=1               |

```
w.wset("optiondailyquotationstastics","startdate=2020-10-16;enddate=2020-10-17;exchange=sse;windcode=510050.SH")
w.wss("10002477.SH,10002478.SH,10002479.SH,10002480.SH,10002481.SH,10002482.SH,10002483.SH,10002484.SH,10002485.SH,10002513.SH,10002517.SH,10002571.SH,10002579.SH,10002627.SH,10002643.SH,10002659.SH,10002687.SH,10002688.SH,10002689.SH,10002725.SH,10002726.SH,10002727.SH,10002728.SH,10002729.SH,10002730.SH,10002731.SH,10002732.SH,10002733.SH,10002761.SH,10002767.SH,10002771.SH,10002772.SH,10002773.SH,10002774.SH,10002775.SH,10002776.SH,10002777.SH,10002778.SH,10002779.SH,10002807.SH,10002815.SH,10002816.SH,10002817.SH,10002818.SH,10002819.SH,10002820.SH,10002821.SH,10002822.SH,10002823.SH,10002853.SH", "intrinctvalue,timevalue","tradeDate=20201016")
```

![image-20201017235759039](https://img2020.cnblogs.com/blog/1958143/202010/1958143-20201018101851946-2054498818.png)

### option_

### strategy

买卖 交易策略

| column       | name               | type        | 备注                                                         |
| ------------ | ------------------ | ----------- | ------------------------------------------------------------ |
| id           | 键                 | int         | key                                                          |
| deal_type    | 类型               | varchar(32) | Buy_Put:购买认沽期权<br/>Buy_Call: 购买认购期权<br/>Exec_Option: 行权<br/>Sell_Call: 卖出认购期权<br/>Sell_Put: 卖出认沽期权<br/>Sell_50ETF: 卖出50ETF<br/>Buy_50ETF: 买入50ETF |
| deal_time    | 时间               | datetime    |                                                              |
| status       | 状态               | varchar(32) | Success/Failed                                               |
| volume       | 份数               | double      |                                                              |
| transfer_fee | 手续费             | double      |                                                              |
| price_per    | 每份价             | double      |                                                              |
| price        | 总价               | double      |                                                              |
| trade_id     | 属于哪次交易的买卖 | int         | 外键                                                         |
| item         | 买卖的物品         | varchar(64) | 10002423.SH                                                  |



### trade

一次交易，可能包含了多次买卖/交易策略

| column     | name     | type        | 备注                                                |
| ---------- | -------- | ----------- | --------------------------------------------------- |
| id         | 键       | int         | key                                                 |
| trade_type | 交易类型 | varchar(32) | ????问商业组                                        |
| start_time | 开始时间 | datetime    |                                                     |
| end_time   | 结束时间 | datetime    | 未结束时可为空                                      |
| status     | 状态     | varchar(32) | Pending:正在进行<br/>Success: 成功<br/>Failed: 失败 |
| profit     | 收益     | double      | 未结束时可为空                                      |





