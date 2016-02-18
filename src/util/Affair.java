package util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Affair {
	static String BASE_URL = "http://120.26.77.102:8080/Service/";

	public static String registerUser(String username, String password, String telephone, String headSculpture) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);// 昵称
		map.put("password", password);// 密码
		map.put("telephone", telephone);// 号码
		map.put("headSculpture", headSculpture);// 用户名
		String url = BASE_URL + "user/register.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String deleteUser(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "user/deleteUser.action";
		return HttpUtil.postRequest(url, map);
	}

	public static String findUserById(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "user/findUserById.action";
		String body = HttpUtil.postRequest(url, map);
		JSONObject obj = new JSONObject(body);
		StringBuffer result = new StringBuffer();
		result.append(obj.toString()).append("\r\n");
		return result.toString();
	}
	public static String alterUser(String id, String username, String telephone, String headSculpture, String locate, String GPS, String QQ, String WeiChat) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		map.put("username", username);
		map.put("telephone", telephone);
		map.put("headSculpture", headSculpture);
		map.put("locate", locate);
		map.put("GPS", GPS);
		map.put("QQ", QQ);
		map.put("WeiChat", WeiChat);
		String url = BASE_URL + "user/alterUser.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String uploadUserGPS(String id, String GPS, String locate) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		map.put("GPS", GPS);
		map.put("locate", locate);
		String url = BASE_URL + "user/uploadUserGPS.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String loginUser(String telephone, String password) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("telephone", telephone);// 电话
		map.put("password", password);// 密码
		String url = BASE_URL + "user/login.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String uploadDriverGPS(String id, String GPS, String locate) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);//时间id
		map.put("GPS", GPS);//由经度;纬度构成
		map.put("locate", locate);
		String url = BASE_URL + "driver/uploadDriverGPS.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String loginDriver(String telephone, String password) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("telephone", telephone);// 电话
		map.put("password", password);// 密码
		String url = BASE_URL + "driver/login.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String deleteDriver(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "driver/deleteUser.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String findDriverById(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "driver/findUserById.action";
		String body = HttpUtil.postRequest(url, map);
		JSONObject obj = new JSONObject(body);
		StringBuffer result = new StringBuffer();
		result.append(obj.toString()).append("\r\n");
		return result.toString();
	}
	public static String alterDriver(String id, String realName, String license, String headSculpture, String GPS, 
		String locate, String telephone) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		map.put("realName",realName);
		map.put("license",license);
		map.put("headSculpture", headSculpture);
		map.put("GPS", GPS);
		map.put("locate",locate);
		String url = BASE_URL + "driver/alterUser.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String addComment(String driverId,String userId,String score,String information) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId",driverId);// 司机id
		map.put("userId",userId);// 乘客id
		map.put("score",score);//打分
		map.put("information",information);//评论内容
		String url = BASE_URL + "comment/addComment.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String findCommentById(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "comment/findCommentById.action";
		String body = HttpUtil.postRequest(url, map);
		JSONObject obj = new JSONObject(body);
		StringBuffer result = new StringBuffer();
		result.append(obj.toString()).append("\r\n");
		return result.toString();
	}
	public static String deleteComment(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);//评论内容
		String url = BASE_URL + "comment/deleteComment.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String getCommentByUser(String userId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("pageNum", pageNum);
		String url = BASE_URL + "comment/getCommentByUser.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该用户尚未给过评价"))
			result.append("该用户尚未给过评价");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String getCommentByDriver(String driverId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId", driverId);
		map.put("pageNum", pageNum);
		String url = BASE_URL + "comment/getCommentByDriver.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该司机尚未获得评价"))
			result.append("该司机尚未获得评价");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String nearByDriver(String GPS,String locate,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("GPS",GPS);
		map.put("locate",locate);
		map.put("pageNum",pageNum);//页数，pageNum=""返回所有，pageNum="1"返回1-10
		String url = BASE_URL + "user/nearByDriver.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("查看附近的人需先定位"))
			result.append("查看附近的人需先定位");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{/*
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		*/result.append(body);
			}
		return result.toString();
	}
	public static String nearByUser(String GPS,String locate,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("GPS",GPS);
		map.put("locate",locate);
		map.put("pageNum",pageNum);//页数，pageNum=""返回所有，pageNum="1"返回1-10
		String url = BASE_URL + "driver/nearByUser.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("查看附近的人需先定位"))
			result.append("查看附近的人需先定位");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String findOrderformById(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "orderform/findOrderformById.action";
		String body = HttpUtil.postRequest(url, map);
		JSONObject obj = new JSONObject(body);
		StringBuffer result = new StringBuffer();
		result.append(obj.toString()).append("\r\n");
		return result.toString();
	}
	public static String getOrderformByUser(String userId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("pageNum", pageNum);
		String url = BASE_URL + "orderform/getOrderformByUser.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该用户尚未发过订单"))
			result.append("该用户尚未发过订单");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String getOrderformByDriver(String driverId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId", driverId);
		map.put("pageNum", pageNum);
		String url = BASE_URL + "orderform/getOrderformByDriver.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该司机尚未接过订单"))
			result.append("该司机尚未接过订单");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String setDriverState(String id,String workState) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);// 司机id
		map.put("workState",workState);// 司机工作状态
		String url = BASE_URL + "driver/setDriverState.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String setOrderFormRunState(String id,String runState) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);// 司机id
		map.put("runState",runState);// 司机工作状态
		String url = BASE_URL + "orderform/setRunState.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String addRequest(String userId,String start,String end,String GPS,String timeInterval
	,String recompense,String locate) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId",userId);//用户id
		map.put("start",start);//出发点
		map.put("end",end);//终点
		map.put("GPS",GPS);//GPS
		map.put("timeInterval",timeInterval);//时间
		map.put("recompense",recompense);//报酬
		map.put("locate",locate);//地区
		String url = BASE_URL + "request/addRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String deleteRequest(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);//request
		String url = BASE_URL + "request/deleteRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String findRequestById(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);// 用户id
		String url = BASE_URL + "request/findRequestById.action";
		String body = HttpUtil.postRequest(url, map);
		JSONObject obj = new JSONObject(body);
		StringBuffer result = new StringBuffer();
		result.append(obj.toString()).append("\r\n");
		return result.toString();
	}
	public static String getRequestByUser(String userId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId",userId);// 用户id
		map.put("pageNum",pageNum);
		String url = BASE_URL + "request/getRequestByUser.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该用户尚未发过预约单"))
			result.append("该用户尚未发过预约单");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String getRequestByDriver(String driverId,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId",driverId);// 用户id
		map.put("pageNum",pageNum);
		String url = BASE_URL + "request/getRequestByDriver.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该司机尚未接过预约单"))
			result.append("该司机尚未接过预约单");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String nearByRequest(String GPS,String locate,String pageNum) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("GPS",GPS);
		map.put("locate",locate);
		map.put("pageNum",pageNum);//页数，pageNum=""返回所有，pageNum="1"返回1-10
		String url = BASE_URL + "request/nearByRequest.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("查看附近的预约单需先定位"))
			result.append("查看附近的预约单需先定位");
		else if (body.equals("f")) 
			result.append("超出页数");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String alterRequest(String id,String start,String end,String timeInterval,String recompense,String GPS) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);
		map.put("start",start);// 起始点
		map.put("end",end);// 终点
		map.put("start",start);// 起始点
		map.put("timeInterval",timeInterval);// 起始点
		map.put("recompense",recompense);// 起始点
		map.put("GPS",GPS);// 起始点
		String url = BASE_URL + "request/alterRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String completeRequest(String id,String driverId) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);// 预约单id
		map.put("driverId",driverId);//司机id
		//map.put("userId",userId);
		String url = BASE_URL + "request/completeRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String addOrderform(String driverId,String userId,String start,String end,String GPS) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId",driverId);// 司机id
		map.put("userId",userId);// 乘客id
		map.put("start",start);// 起点
		map.put("end",end);// 终点
		map.put("GPS",GPS);// 距离，注该属性在服务器为double型，所以发送时只发数值不发单位默认以米为单位
		String url = BASE_URL + "orderform/addOrderform.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String driverRequest(String id,String driverId,String userId,String information) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id);// 预约单id
	    map.put("driverId",driverId);
	    map.put("userId",userId);
	    map.put("information",information);
		String url = BASE_URL + "request/driverRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String presentRequest(String driverId) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
	    map.put("driverId",driverId);
		String url = BASE_URL + "request/presentRequest.action";
		return HttpUtil.postRequest(url, map);
	}
	public static String registerDriver(String realName,String license, String password, String telephone, 
			String headSculpture,String IdNo) throws Exception {
			Map<String, String> map = new HashMap<String, String>();
			map.put("realName", realName);// 真实姓名
			map.put("license",license);// 车牌号
			map.put("password", password);// 密码
			map.put("telephone", telephone);// 号码
			map.put("headSculpture", headSculpture);// 头像
			map.put("IdNo",IdNo);//身份证号
			String url = BASE_URL + "driver/register.action";
			return HttpUtil.postRequest(url, map);
		}
	public static String driverResponse(String id,String driverId, String rsp) throws Exception {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id",id);//订单id
			map.put("driverId",driverId);//司机id
			map.put("rsp",rsp);// 司机回复
			String url = BASE_URL + "orderform/driverResponse.action";
			return HttpUtil.postRequest(url, map);
		}
	public static String driverTodayOrderform(String driverId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId",driverId);
		String url = BASE_URL + "orderform/driverTodayOrderform.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该用户今天无预约单"))
			result.append("该用户今天无预约单");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
	public static String driverTodayRequest(String driverId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("driverId",driverId);
		String url = BASE_URL + "request/driverTodayRequest.action";
		String body = HttpUtil.postRequest(url, map);
		StringBuffer result = new StringBuffer();
		if(body.equals("该用户今天无请求单"))
			result.append("该用户今天无请求单");
		else{
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				result.append(obj.toString()).append("\r\n");
			}
		}
		return result.toString();
	}
}
