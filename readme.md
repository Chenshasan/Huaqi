# 更新日志

## 2020/10/25

* 给出逻辑实现需要的数据层接口

	* 当前ETF数据 `ETFMapper.currentETF`

	* 当前某个期权数据`OptionMapper.currentOption(optionCode)`

	* 某个月到期的期权

		```java
		interface OptionMapper{
			/**
		     * 获取某个月到期的期权List
		     * @param strike_month 到期月份，格式类似"2020-10"
		     * @return
		     */
		    List<OptionPO> getMonthOption(@Param("month") String strike_month);
		}
		```

	* 买卖记录

		* `StrategyMapper.addStrategy`
		* `TradeMapper.addTrade`
		
	* 获取当月时间价值最小的期权
	
		* 还没写，利用getMonthOption接口在Service层实现下吧

> 开发环境中，记得先注意运行.sql运行
>
> 大多数表的字段都是NOT NULL的，所有添加的时候要set全属性

