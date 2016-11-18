package com.tencent.tws.walletserviceproxy.service;

interface IWalletServiceProxy {

	/**
	* 获取卡列表
	* @param outputParam 用于接收卡列表信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardListQuery(out String[] outputParam);
	
	/**
	* 卡片切换，用于设置默认卡
	*@param 应用aid
	* @return 0:成功；其它:失败
	*/
	int cardSwitch(String instance_id);
	
	/**
	* 获取开卡、充值会话
	* @param inputParam  JSON字符串
	* @param session     用于接收会话字符串
	*
	* @return 0:成功；其它:失败
	*/
	int getSession(String inputParam, out String[] session);
	
	/**
	* Applet管理
	* @param inputParam JSON字符串
	*
	* @return 0:成功；其它:失败
	*/
	int appletManage(String inputParam,out String[] outputParam);
			
	
	/**
	* 卡片个人化
	* @param inputParam JSON字符串
	*
	* @return 0:成功；其它:失败
	*/
	int cardPerso(String inputParam,out String[] outputParam);
	
	/**
	* 卡片充值
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收充值后结果信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardTopup(String inputParam, out String[] outputParam);
	
	/**
	* 卡片查询
	* @param inputParam  JSON字符串
	* @param outputParam JSON字符串  用于接收卡片信息
	*
	* @return 0:成功；其它:失败
	*/
	int cardQuery(String inputParam, out String[] outputParam);
	
	/**
	* 获取SE CPLC
	* @param cplc 用于接收CPLC
	*
	* @return 0:成功；其它:失败
	*/
	int getCplc(out String[] cplc);
	
	/**
	* 检测是否支持
	*
	* @return 0:成功；其它:失败
	*/
	int checkSupportStatus();
	
	/**
	* 检测Instance是否安装
	* @param instance_id appletId
	*
	* @return 0:成功；其它:失败
	*/
	int isInstanceExisted(String instance_id);
	
	/**
	* 从se中查询交易记录
	* @param instance_id appletId
	* @param outputParam JSON字符串  用于接收卡片交易记录信息
	*
	* @return 0:成功；其它:失败
	*/
	int transQuerySe(String instance_id,out String[] outputParam);
	
	/**
	* 设置uid
	* 
	* @return 0:成功；其它:失败
	*/
	int setUid();
	/**
	* 初始化se
	* 
	* @return 0:成功；其它:失败
	*/
	int initSe();

	/**
    	* 合并开卡接口
    	*
    	* @return 0:成功；其它:失败
    */

	int issueCard(String inputParam, out String[] outputParam);

	/**
    	* 获取订单号
    	*
    	* @return 0:成功；其它:失败
    */
	int getPayOrder(String inputParam, out String[] outputParam);
	
		/**
	*Send APDU command to applet
	*@apdu
	*    apdu command to be execute
	*@resultCode
	*	apdu execute result
	*/
	
	byte[] apduExchange(in byte[] apdu,out int[] resultCode);
	
	/** 
	*Send APDU array command to applet
	*@apdulist
	*@resultCode
	*return 0->success, 1->error
	*/
	int apduArrayExchange(in String[] apdulist,out String[] rspApdu);
	/** 
	*selectAid to openchannel
	*@aid
	*	select channel
	*@resultCode
	*return 0->success, 1->error
	*/
	byte[] selectAid(String aid, out int[] resultCode);
	
	
	/**
	*Shut down the se connection
	*
	*@return 0->success, 1->error
	*/

	void shutdown();
	/**
    	* 传送数据
    	*
    	* @return 0:成功；其它:失败
    */
	void passData(String inputParam);

	/**
    	* 获取数据
    	*
    	* @return 0:成功；其它:失败
    */
	String getData();
}