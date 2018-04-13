package license.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 一些基本的app 使用工具类提取
 * @author focus ;liaofei
 * @date 2016年2月22日
 * @time 上午11:16:05
 */
public class AppUtil {

	public static final String OBJECTCLASS = "objectClass";
//	private static Logger LOG = LoggerFactory.getLogger(AppUtil.class);
	/**
	 * 判断对象是否为空，对象适用于：集合，字符序列，Map，一维数组，通用对象
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:06:19
	 */
	public static boolean isEmpty(Object obj){
		if(obj == null) return true;
		if(obj instanceof Collection){
			return ((Collection<?>) obj).isEmpty();
		}else if(obj instanceof CharSequence){
			return ((CharSequence) obj).toString().trim().length() == 0 || obj.toString().trim().equals("null")||"".equals(obj.toString().trim());
		}else if(obj instanceof Map){
			return ((Map<?,?>) obj).isEmpty();
		}else if(obj instanceof Object[]){
			return ((Object []) obj).length == 0;
		}
		return false;	
	}
	
	/**
	 * 判断对象是否为空
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:25
	 */
	public static boolean isEmpty(Object ...objs){
		for(Object obj : objs){
			if(isEmpty(obj) == false)
				return false;
		}
		return true;
	}
	
	/**
	 * 判断对象是否不为空
	 * @param obj
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:34
	 */
	public static boolean isNotEmpty(Object obj){
		return isEmpty(obj) == false;
	}
	
	/**
	 * 判断对象是否不为空，必须所有元素都不为空
	 * @param objs
	 * @return
	 * @author focus
	 * @date 2015年9月2日
	 * @time 上午11:07:41
	 */
	public static boolean isNotEmpty(Object ...objs){
		for(Object obj : objs){
			if(isEmpty(obj))
				return false;
		}
		return true;
	}
	
	
	public static List<Field> getAllFields(Class<?> clzz){
		
		List<Field> fieldList = new ArrayList<Field>();
		// 父类属性
		if(clzz.getSuperclass() != Object.class){
			fieldList.addAll(getAllFields(clzz.getSuperclass()));
		}
		
		// 当前类属性
		Field[] fields = clzz.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			fieldList.add(field);
		}
		
		return fieldList;
	}
	public static Object getFieldValue(String fieldName ,Object obj) throws Exception{
		Class<?> clzz = obj.getClass();
		return getFieldValue(clzz.getDeclaredField(fieldName), obj);
	}
	
	public static Object getFieldValue(Field field,Object obj) throws Exception{
		field.setAccessible(true);
		return field.get(obj);
	}
	
	public static boolean isString(Class<?> clzz){
		if(clzz == null) return false;
		String clzzName = clzz.getName();
		if(clzzName.equals(char.class.getName()) || clzzName.equals(Character.class.getName())){
			return true;
		}else if(clzzName.equals(String.class.getName())){
			return true;
		}
		return false;
	}
	/**
	 * 是否是数值类型
	 * @author focus
	 * @date 2015年12月9日
	 * @time 下午1:20:44
	 */
	public static boolean isNumber(Class<?> clzz){

		if(clzz == null) return false;
		
		String clzzName = clzz.getName();
		if(clzzName.equals(int.class.getName()) || clzzName.equals(Integer.class.getName())){
			return true;
		}else if(clzzName.equals(long.class.getName()) || clzzName.equals(Long.class.getName())){
			return true;
		}else if(clzzName.equals(short.class.getName()) || clzzName.equals(Short.class.getName())){
			return true;
		}else if(clzzName.equals(byte.class.getName()) || clzzName.equals(Byte.class.getName())){
			return true;
		}else if(clzzName.equals(double.class.getName()) || clzzName.equals(Double.class.getName())){
			return true;
		}else if(clzzName.equals(float.class.getName()) || clzzName.equals(Float.class.getName())){
			return true;
		}
		return false;
	}
	
	/**
	 * 内存分页
	 * @author focus
	 * @date 2016年2月29日
	 * @time 上午10:34:45
	 */
	public static <T>List<T> page(List<T>list,int offset,int limit){
		
		if(isEmpty(list)) return null;
		
		int size = list.size();
		int fromIndex = offset;
		int toIndex = offset + limit;
		
		if(size < fromIndex){
			return null;
		}
		
		if(size < toIndex){
			toIndex = size;
		}
		
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * 从俩用户Id组装成一个chatId，用户A_用户B
	 * @param loginUserId 用户A
	 * @param chatId 用户B
	 * @return
	 */
	public static String makeChatIdFromUserIds(String loginUserId, String chatId){
		if(0<loginUserId.compareTo(chatId)){
			return loginUserId+'_'+chatId;
		}else{
			return chatId+'_'+loginUserId;
		}
	}

	/**
	 * 根据chatId，和其中一个用户Id，解析出另一个用户Id
	 * @param loginUserId 其中一个用户Id
	 * @param chatId 用户A_用户B
	 * @return
	 */
	public static String parseOneOfUserId(String loginUserId, String chatId){
//		String strArr[] = null;
//		strArr = chatId.split("_");
//		if(loginUserId.equals(strArr[0])){
//			return strArr[1];
//		}else{
//			return strArr[0];
//		}
		int loginUserIdPosition = chatId.indexOf(loginUserId);
		//找不到需要匹配的用户
		if(loginUserIdPosition == -1){
			return loginUserId;
		}
		//若当前用户在前面，目标用户则取后面
		if(loginUserIdPosition == 0){
			return chatId.substring(loginUserId.length() + 1);
		}else{//若当前用户在后面，目标用户则取前面
			return chatId.substring(0, (chatId.length() - loginUserId.length() - 1));
		}
	}

	/**
	 * 根据JID String获取username/charRoomId部分
	 * @param jid
	 * @return
	 */
	public static String getNodeFromJID(String jid){
		if(jid == null) return null;
		int index = jid.indexOf("@");
		if(index != -1){
			return jid.substring(0, index);
		}else{
			return jid;
		}
	}

	public static void main(String[] args) {
		System.out.println(parseOneOfUserId("u12a", "u12a_u34b"));
		System.out.println(parseOneOfUserId("u34b", "u12a_u34b"));
		System.out.println(parseOneOfUserId("u12_ab", "u12_ab_u34"));
		System.out.println(parseOneOfUserId("u12_ab", "u12_ab_u34_cd"));
		System.out.println(parseOneOfUserId("u34_cd", "u12_ab_u34_cd"));
		System.out.println(parseOneOfUserId("u34_cd", "u12ab_u34_cd"));
	}
}
