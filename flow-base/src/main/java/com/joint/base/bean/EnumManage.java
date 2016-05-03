package com.joint.base.bean;

public class EnumManage {

	public enum PayStatusEnum{
		free ("免费用户",1),
		pay("付费用户",2);

		private final String value;
		private final int key;

		private PayStatusEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

	public enum AuthCodeEnum {
		passwordForget ("忘记密码",1),
		register ("注册",2);

		private final String value;
		private final int key;

		private AuthCodeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

    /**
     * 帐号种类
     */
    public enum AccountType{
        Manager("企业管理员",1),
        User("企业普通用户",2);

        private final String value;
        private final int key;

        AccountType(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }

    public enum NotifyKeyEnum{
        pushLog ("推送消息",1),
        sms ("短信消息",2),
        email("邮件消息",3),
        apiRequest("通知接口消息",4),
        wxMpCustomMessage("微信客服消息",5),
		locationEntity("位置信息",6),
        log("请求日志",7),
        resume("业务履历",8),
        downloadRecord("数据导出",9);

        private final String value;
        private final int key;

        private NotifyKeyEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }

    public enum DutyEnum {
        Default ("组员",1),
        Principal ("负责人",2),
        Deputy  ("副职",3);

        private final String value;
        private final int key;

        private DutyEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    };
	/**
	 * 支付状态
	 */
	public static enum PayStateEnum {
		
		/**
		 * 待确认
		 */
		confirming("待确认" , 0),
		
		/**
		 * 待支付
		 */
		paying("待支付" , 1),
		
		/**
		 * 已支付
		 */
		done("已支付" , 2);
		
		private final String value;
		private final int key;
		
		private PayStateEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

	/**
	 * 支付类型
	 */
	public static enum PayTypeEnum {

		/**
		 * 成员账户续费
		 */
		renew("成员账户续费" , 0),

		/**
		 * US电子卷
		 */
		usticket("US电子充值券" , 1),

		/**
		 * 购买产品
		 */
		buy("购买产品" , 2);

		private final String value;
		private final int key;

		private PayTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 续费类型
	 */
	/**
	 * 续费类型
	 */
	public static enum RenewTypeEnum {

		/**
		 * 体验
		 */
		experience("体验" , 0),

		/**
		 * 充值
		 */
		pay("充值" , 1),

		/**
		 * 奖励
		 */
		reward("奖励" , 2),

		/**
		 * 转出
		 */
		out("转出" , 3);

		private final String value;
		private final int key;

		private RenewTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 表单类型
	 */
	public static enum FromType{
		/**
		 * 普通
		 */
		normal("普通", 0),
		/**
		 * 修改历史
		 */
		update("修改历史", 1),
		/**
		 *	还原历史
		 */
		reset("还原历史", 2);
		
		private final String value;
		private final int key;
		
		private FromType(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 洽谈来源
	 */
	public static enum NegotiationFrom{
		/**
		 * 新洽谈
		 */
		business("商务洽谈", 0),
		/**
		 * 履约洽谈
		 */
		contract("履约洽谈", 1);
		
		private final String value;
		private final int key;
		
		private NegotiationFrom(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	/**
	 * 批注状态
	 * */
	public static enum CommentState{
		/**
		 * 回复
		 */
		response("回复", 0),
		/**
		 * 待批，需要回执批复的
		 */
		request("待批", 1),
		/**
		 * 已批复回执的
		 */
		approved("已批复", 2);
		
		private final String value;
		private final int key;
		
		private CommentState(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	/**
	 * 批注类别
	 * */
	public static enum CommentTypeEnum{
		/**
		 * 
		 */
		none("", 0),
		/**
		 * 普通
		 */
		normal("普通", 1),
		/**
		 * 紧急
		 */
		important("紧急", 2),
		/**
		 * 畅聊
		 */
		community("畅聊" , 3),;
		
		private final String value;
		private final int key;
		
		private CommentTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
    /**
     * 批注类型
     * */
    public static enum CommentModelEnum{
        /**
         * 文本
         */
        text("文本", 0),
        /**
         * 图片
         */
        image("图片", 1),
        /**
         * 语音
         */
        voice("语音", 2),
        /**
         * 视频
         */
        video("视频", 3),
        /**
         * 地理位置
         */
        location("地理位置", 4),
        /**
         * 链接
         */
        link("链接", 5),
        /**
         * 其他文件，打开时用文件表示
         */
        file("其他文件", 6);

        private final String value;
        private final int key;

        private CommentModelEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }
	/**
	 * 权限
	 */
	public static enum ActionEnum {
		
		/**
		 * 无
		 */
		none("无权限", 0),
		
		/**
		 * 领导层
		 */
		Boss("领导层", 1),
		
		/**
		 * 负责人
		 */
		Principal("负责人", 2),
		
		/**
		 * 普通员工
		 */
		normal("普通员工", 3);
		
		private final String value;
		private final int key;
		
		private ActionEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	public enum DutyState {
		/***/
		Default {public String getName(){return "组员";}},
        /**  */
		Principal {public String getName(){return "负责人";}},
		/**  */
		Deputy  {public String getName(){return "副职";}};
        public abstract String getName();
	};

	
	/**
	 * 履约阶段
	 */
	public static enum ProceedsPlanEnum {
		
		/**
		 * 未开票
		 */
		Plan("未开票", 1),
		
		/**
		 * 已开票待收款
		 */
		Invoice("已开票", 2),
		
		/**
		 * 部分收款
		 */
		Unfinished("部分收款", 3),
		
		/**
		 * 全额收款
		 */
		Finish("全额收款", 4), ;
		
		private final String value;
		private final int key;
		
		private ProceedsPlanEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

	
	/**
	 * 活动状态
	 */
	public static enum ActivitiStateEnum {
		/**
		 * 活动中
		 */
		Active("活动中", 1),
		
		/**
		 * 离线
		 */
		Offline("离线", 2);
		
		private final String value;
		private final int key;
		
		private ActivitiStateEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	

	/**
	 * 订单发起
	 */
	public static enum CreateTypeEnum {
		/**
		 * APP端
		 */
		app("APP端" , 1),
		
		/**
		 * 网站
		 */
		website("网站", 2),
		
		/**
		 * 微信
		 */
		mp("微信", 3);
		
		private final String value;
		private final int key;
		
		private CreateTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 发票类型
	 */
	public static enum InvoiceTypeEnum {

		/**
		 * 个人
		 */
		personal("个人", 1),
		
		/**
		 * 企业
		 */
		enterprise("企业", 2);
		
//		/**
//		 * 普通
//		 */
//		normal("普通"),
//		
//		/**
//		 * 增值
//		 */
//		added("增值");
		
		private final String value;
		private final int key;
		
		private InvoiceTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 坐标类型
	 * bd09 百度墨卡托坐标系
	 * bd09ll 百度经纬度坐标系（百度地图）
	 * gcj02 中国国家测绘局坐标系
	 */
	public static enum CoordTypeEnum {
		/**
		 * 百度墨卡托坐标系
		 */
		bd09("百度墨卡托坐标系" , 1),
		
		/**
		 * 百度经纬度坐标系（百度地图）
		 */
		bd09ll("百度经纬度坐标系" , 2),
		
		/**
		 * 中国国家测绘局
		 */
		gcj02("中国国家测绘局" , 3),
		/**
		 * 国际坐标系
		 */
		wgs84("国际坐标系" , 4);
		
		private final String value;
		private final int key;
		
		private CoordTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 百度定位结果LocType
	 * 61 ： GPS定位结果
	 * 62 ： 扫描整合定位依据失败。此时定位结果无效。
	 * 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
	 * 65 ： 定位缓存的结果。
	 * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
	 * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
	 * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
	 * 161： 表示网络定位结果
	 * 162~167： 服务端定位失败。 
	 */
	public static enum LocTypeEnum {
		/**
		 * GPS定位结果 
		 */
		GpsLocation("GPS定位结果 " , 61),
		
		/**
		 * 扫描整合定位依据失败。此时定位结果无效
		 */
		bd09ll("扫描整合定位依据失败。此时定位结果无效" , 62),
		
		/**
		 * 网络异常，没有成功向服务器发起请求。此时定位结果无效
		 */
		NetWorkError("网络异常，没有成功向服务器发起请求。此时定位结果无效" , 63),
		
		/**
		 * 定位缓存的结果
		 */
		CacheLocation("定位缓存的结果" , 65),
		
		/**
		 * 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
		 */
		OfflineLocaiton("网络异常，离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果" , 66),
		
		/**
		 * 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
		 */
		OfflineLocaitonError("离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果" , 67),
		
		/**
		 * 网络连接失败时，查找本地离线定位时对应的返回结果
		 */
		NetConnectErrorReturnOfflineLocaiton("网络连接失败时，查找本地离线定位时对应的返回结果" , 68),
		
		/**
		 * 表示网络定位结果 
		 */
		NetWorkLocation("表示网络定位结果 " , 161);
		
		private final String value;
		private final int key;
		
		private LocTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

    /**
     * 自定义消息级别
     */
    public static enum CustomMessageLevelEnum {
        /**
         * 消息
         */
        Normal("普通消息" , 0),

        /**
         * 提醒
         */
        Notice("提醒" , 1),

        /**
         * 代办
         */
        Todo("代办" , 2);

        private final String value;
        private final int key;

        private CustomMessageLevelEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }
	
	/**
	 * 自定义消息类型
	 */
	public static enum CustomMessageTypeEnum {
		/**
		 * 系统消息
		 */
		AdminMsg("系统消息" , 101),
		
		/**
		 * 用户消息
		 */
		UserMsg("用户消息" , 102),
		
		/**
		 * 业务畅聊室
		 */
		Community("业务畅聊室" , 103),
		
		/**
		 * 表单阅读
		 */
		FormRead("表单阅读" , 201),
		
		/**
		 * 网页链接
		 */
		WebView("网页链接" , 301),
		
		/**
		 * 通知更新
		 */
		NewCount("通知更新" , 401);
		
		private final String value;
		private final int key;
		
		private CustomMessageTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 推送目标
	 */
	public static enum PushTargetEnum {
		
		/**
		 * SmartSales
		 */
		SmartSales("SmartSales" , 1);
		
		private final String value;
		private final int key;
		
		private PushTargetEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 推送类别
	 */
	public static enum PushWayEnum {
		/**
		 * 通知
		 */
		Notification("通知" , 1),
		
		/**
		 * 自定义消息
		 */
		CustomMessage("自定义消息" , 2);
		
		private final String value;
		private final int key;
		
		private PushWayEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 推送方式
	 */
	public static enum PushTypeEnum {
		/**
		 * 通过key（发送给所有用户）
		 */
		ApplicationKey("通过key" , 1),
		
		/**
		 * 通过IMEI（已过时）
		 */
		IMEI("通过IMEI（已过时）" , 2),
		
		/**
		 * 通过标识
		 */
		Alias("通过标识", 3),
		
		/**
		 * 通过特征（or关系）
		 */
		Tag("通过特征（or关系）" , 4),
		
		/**
		 * 通过特征（and关系）
		 */
		Tag_And("通过特征（and关系）", 5),
		
		/**
		 * 注册ID
		 */
		Registration_Id("注册ID", 6);
		
		private final String value;
		private final int key;
		
		private PushTypeEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}
	
	/**
	 * 推送错误code
	 */
	public static enum ErrorCodeEnum {
		/**
		 * 没有错误，发送成功
		 */
		NOERROR(0,"没有错误，发送成功"),
		
		/**
		 * 系统内部错误
		 */
		SystemError(10,"系统内部错误"),
		
		/**
		 * 不支持GET请求
		 */
		NotSupportGetMethod(1001,"不支持GET请求"),
		
		/**
		 * 缺少必须参数
		 */
		MissingRequiredParameters(1002,"缺少必须参数"),
		
		/**
		 * 参数值不合法
		 */
		InvalidParameter(1003,"参数值不合法"),
		
		/**
		 * 验证失败
		 */
		ValidateFailed(1004,"验证失败"),
		
		/**
		 * 消息体太大
		 */
		DataTooBig(1005,"消息体太大"),
		
		/**
		 * IMEI不合法
		 */
		InvalidIMEI(1007,"IMEI不合法"),
		
		/**
		 * appkey不合法
		 */
		InvalidAppKey(1008,"appkey不合法"),
		
		/**
		 * msg_content不合法
		 */
		InvalidMsgContent(1010,"msg_content不合法"),
		
		/**
		 * 没有满足条件的推送目标
		 */
		InvalidPush(1011,"没有满足条件的推送目标"),
		
		/**
		 * IOS不支持自定义消息
		 */
		CustomMessgaeNotSupportIOS(1012,"IOS不支持自定义消息");
		
		private final int key;
		private final String value;
		
		private ErrorCodeEnum(final int key,final String value) {
			this.key = key;
			this.value = value;
		}
		public int key() {
			return this.key;
		}
		public String value() {
			return this.value;
		}
	}
	
	/**
	 * 个人账户类型，用以每一笔流水或者个人账户
	 */
	public static enum AccountLogStateEnum {
		/**
		 * 
		 */
		normal("正常" , 1),
		
		/**
		 * 
		 */
		freeze("待结算" , 2);
		
		private final String value;
		private final int key;
		
		private AccountLogStateEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}

    /**
     * 销售员考评统计周期
     * */
    public static enum EvaluateEnum {
        /**
         *
         */
        month("月度" , 1),

        /**
         *
         */
        season("季度" , 3),
        /**
         *
         */
        halfyear("半年度" , 6),
        /**
         *
         */
        year("年度" , 12);

        private final String value;
        private final int key;

        private EvaluateEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }

    /**
     * 销售员考评统计方式
     * */
    public static enum EvaluateCreateTypeEnum {
        /**
         *
         */
        snapshot("快照" , 0),
        /**
         *
         */
        instant("实时" , 1);

        private final String value;
        private final int key;

        private EvaluateCreateTypeEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }
    }


	/**
	 * 支付方式
	 */
	public static enum PayMethodEnum {

		/**
		 * 支付宝
		 */
		alipay("支付宝" , 0),

		/**
		 * US电子卷
		 */
		usticket("US电子充值劵" , 1);

		private final String value;
		private final int key;

		private PayMethodEnum(final String value, final int key) {
			this.value = value;
			this.key = key;
		}
		public String value() {
			return this.value;
		}
		public int key() {
			return this.key;
		}
	}



}
