package license.constant;

/**
 * 统计服务常量
 * @author focus
 * @date 2015年11月17日
 * @time 下午2:50:50
 */
public class DBConstant {

	public static final int success = 0;

	/*
	 * 对应的数据库常用的统计字段 
	 */
	public static final String STATISTIC_MSG_TYPE_FIELD = "msgType";
	public static final String STATISTIC_MSG_COUNT_FIELD = "msgCount";
	public static final String STATISTIC_TYPE_ID_FIELD = "statisticTypeId";
	public static final String STATISTIC_TYPE_FIELD = "statisticType";
	public static final String STATISTIC_DATE_FIELD = "statisticDate";
	public static final String USER_ID_FIELD = "userId";
	public static final String TENEMENT_ID_FIELD = "tenementId";
	
	/*
	 *  1 平 台统计，2 企业统计， 3 用户统计
	 */
	public static final int PLATFORM_STATISTIC_TYPE = 1;
	public static final int TENEMENT_STATISTIC_TYPE = 2;
	public static final int USER_STATISTIC_TYPE = 3;
	
	/*
	 *  1 文字， 2 图片 ，3 语音，4 文件 ，5 消息总量
	 */
	public static final int STATISTIC_WORDS = 1;
	public static final int STATISTIC_IMAGES = 2;
	public static final int STATISTIC_VOICES = 3;
	public static final int STATISTIC_FILES = 4;
 
	/*
	 * 数据库统计的地址和数据库名称，数据库密码常量 
	 */
	public static final String DB_USERNAME = "dbUsername";
	public static final String DB_PASSWORD = "dbPassword";
	public static final String DB_URL = "dbUrl";
	public static final String DB_PORT = "dbPort";
	
	/**
	 * 配置信息
	 */
	public static final String CRON = "eim.plugin.statistic.cron";
	public static final String IS_STATISTIC = "eim.plugin.statistic.isStatistic";
	
	public static final String isAddExecuteExtendSql = "eim.plugin.statistic.addExcetute";
	public static final String extendType = "eim.plugin.statistic.extendType";
	/**
	 * 消息统计缓存到数据的名称
	 */
	public static final String CACHE_PLATFORM_MSG_SYNC_CACHE_NAME = "platformStatisticMessageCache";
	public static final String CACHE_TENEMENT_MSG_SYNC_CACHE_NAME = "tenementStatisticMessageCache";
	public static final String CACHE_USER_MSG_SYNC_CACHE_NAME = "userStatisticMessageCache";
	// 初始化消息缓存同步的进程名称
	public static final String INIT_MESSAGE_QUEUE_TASK = "initMessageQueueTsk";
	// 使用日志分析
	public static final String IS_USE_LOGGER_ANALYSIS = "eim.plugin.statistic.isUseLoggerAnalysis";
	
	public static final String UNDEFINED_SYSTEM = "其它";
	public static final String UNDEFINED_CLIENTTYPE = "其它";
			
}
