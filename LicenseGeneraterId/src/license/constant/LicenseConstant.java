package license.constant;

public class LicenseConstant {

	/*
	 * <li> 701          签名验证失败
	 * <li> 702   license创建失败
	 * <li> 0     License验证成功
	 * <li> 711         解析密文失败
	 * <li> 712   License过期
	 * <li> 713         无效的日期
	 * <li> 714   License快到期了
	 * <li> 715   License快Nonce 错误
	 * <li> 716        无license
	 * <li> 722        用户超出
	 * <li> 723        可登陆账号超出
	 */
	public static final int CREATE_LICENSE_ERROR = 702;
	public static final int SIGN_FAIL = 701;
	public static final int SUCCESS = 0;
	public static final int DECODE_FAIL = 711;
	public static final int LISENCE_EXPIRE = 712;
	public static final int INVALID_DATE = 713;
	public static final int NEAR_EXPIREDATE = 714;
	public static final int NONCE_ERR = 715;
	public static final int NO_LICENSE = 716;
	public static final int USER_OUT = 722;
	public static final int USER_LOGIN_ENABLED_OUT = 723;
	
	public static final String TENEMENT_LIMIT_MAX_USER_COUNT = "731";
	/**
	 * license 限制错误
	 */
	public static final String LICENSE_LIMIT_ERROR = "732";
	// 定时自动检查license
	public static final String PROP_LICENSE_AUTO_CHECK_CORN = "license.auto.check.interval.corn";
	// license 失效后定时发送系统通知给用户 
	public static final String PROP_NOTIFY_ALL_USER_CORN = "license.invalid.usernotify.corn";
	// 系统邮件提醒
	public static final String PROP_SYSTEM_EMAIL_NOTIFY_CORN = "license.system.email.notify.corn";
	// license 失效时系统邮件提醒的时间间隔
	public static final String PROP_SYSTEM_EMAIL_NOTIFY_INTERVAL = "license.system.email.notify.interval.day";
	// 延迟限制
	public static final String PROP_LIMIT_DELAY_INTERVAL = "license.limit.delay.day";
	
	public static final String LICENSE_TASK = "license_task";
	public static final String LICENSE_INIT_NONCE = "License_init_nonce";
	public static final String LICENSE_CHECK_TIME = "eim.plugin.license.task.checkTime.millis";
	
	public static final String LICENSE_TYPE = "licenseType";
	public static final String LICENSE_LOCK = "licenseLock";
	
	public static final String TRIAL_LICENSE = "1";
	public static final String IS_NEW_MAC_ADDRESS = "plugin.eim.license.new.macAddress";
	/**
	 * license 的过期时间
	 */
	public static final String LICENSE_EXPIRE_DATE = "expireDate";
	/**
	 * 最大通讯录数量
	 */
	public static final String LICENSE_MAX_USER_COUNT = "maxUserCount";
	/**
	 * 最大的可登录的用户的数量
	 */
	public static final String LICENSE_MAX_LOGIN_ENABLE_USER_COUNT = "maxLoginEnableUserCount";
	
	/**
	 * license 控制的其他扩展属性
	 */
	public static final String LICENSE_EXT_FIELDS = "extFields";
	/*
	 * 	String record = extFieldJson.optString("concurrentCountRecord");
			String siptrunk = extFieldJson.optString("concurrentCountRelay");
			String aa =  extFieldJson.optString("concurrentCountOperator");
	 */
	public static final String CON_CURRENT_COUNT_RECORD = "concurrentCountRecord";
	public static final String CON_CURRENT_COUNT_RELAY= "concurrentCountRelay";
	public static final String CON_CURRENT_COUNT_OPERATOR = "concurrentCountOperator";
	
	public static final String APP_COUNT = "appCount";
	public static final String CONFERENCE_JOINED_COUNT = "conferenceJoinedCount";
	public static final String MAX_CLIENT_COUNT = "maxClientCount";
	
	
	public static final String ALL_TENEMENTID_FREE = "all_tenement_free";
	
	public static final String APP_CLICK_USER = "app_click_user";
	public static final String IS_LICENSE_ENABLED = "plugin.eim.license.enabled"; // 是否使用license 的开关
	
	public static final String CALLED_ON_OFF_URL = "bss/policy/tenants/call.calledonoff";
	
	public static final String LICENSE_CONTROL_VERSION_SIGN_SUFFIX = "_gzb";
	public static final String LICENSE_CONTROL_VERSION = "eim_5.4";
	
	// 表示所有的license 都已经过期， 但当重新导入license时 这个值会进行更新
	public static final String LICENSE_ALL_EXIPRE = "plugins.eim.license.all.expire";
	// 默认的mac地址
	public static final String DEFAULT_MAC_ADDRESS = "ABCDEFGHIJ";
}
