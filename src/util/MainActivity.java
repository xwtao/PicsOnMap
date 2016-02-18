package util;
import android.app.Activity;
import android.os.Bundle;
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		try {	
			//DialogUtil.showDialog(MainActivity.this,Affair.registerUser("zx","123456","18070516851","1.jpg"),true);//注册用户
			//DialogUtil.showDialog(MainActivity.this,Affair.alterUser("1","ZX","", "", "","","",""),true);//修改用户资料
			//DialogUtil.showDialog(MainActivity.this,Affair.findUserById("1"),true);//根据id找用户
			//DialogUtil.showDialog(MainActivity.this,Affair.loginUser("18070516851","123456"),true);//登录用户
			//DialogUtil.showDialog(MainActivity.this,Affair.registerUser("zx2","123456","12321124","3.jpg"),true);//注册用户
			//DialogUtil.showDialog(MainActivity.this,Affair.deleteUser("2"),true);//删除用户
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadUserGPS("1","127;20","江西南昌"),true);//上传用户定位
			//DialogUtil.showDialog(MainActivity.this,Affair.registerDriver("周星","赣D","123456","1807051685","1.jpg"),true);//注册司机
			//DialogUtil.showDialog(MainActivity.this,Affair.alterDriver("1","","","2.jpg","","",""),true);//修改司机           信息
			//DialogUtil.showDialog(MainActivity.this,Affair.findDriverById("2"),true);//根据id找司机
			//DialogUtil.showDialog(MainActivity.this,Affair.loginDriver("1807051685","123456"),true);//登录司机
			//DialogUtil.showDialog(MainActivity.this,Affair.registerDriver("周星","赣D","123456","18070516851","2.jpg"),true);//注册司机
			//DialogUtil.showDialog(MainActivity.this,Affair.deleteDriver("3"),true);//删除司机
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadDriverGPS("2","127;20","江西南昌"),true);//上传司机定位
			//DialogUtil.showDialog(MainActivity.this,Affair.addComment("2","1","5","还可以咯"),true);//增加评论
			//DialogUtil.showDialog(MainActivity.this,Affair.findCommentById("1"),true);//根据id找评论	
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByDriver("2",""),true);//pageNum="",查找司机所有的被评价过的
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByDriver("2","1"),true);//pageNum="1"查找司机1-10页被评价过的
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByDriver("2","2"),true);//pageNum="2"查找司机11-20页被评价过的
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByUser("1",""),true);//查找用户发表过的所有评论
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByUser("1","2"),true);//查找用户发表过的1-10页评论
			//DialogUtil.showDialog(MainActivity.this,Affair.getCommentByUser("1","1"),true);//查找用户发表过的11-20页评论
			//DialogUtil.showDialog(MainActivity.this,Affair.addComment("2","1","4","有点贵。。"),true);//增加评论
			//DialogUtil.showDialog(MainActivity.this,Affair.deleteComment("3"),true);//减少评论	
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadDriverGPS("4","126;21","江西南昌"),true);//上传司机定位
			//DialogUtil.showDialog(MainActivity.this,Affair.registerDriver("李四","赣A","123456","18070516853","4.jpg"),true);//注册司机
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadDriverGPS("5","128;20","江西南昌"),true);//上传司机定位
			//DialogUtil.showDialog(MainActivity.this,Affair.nearByDriver("127;21","江西南昌","1"),true);//查看某位置附近的司机
			//DialogUtil.showDialog(MainActivity.this,Affair.registerUser("zx2","123456","18070516852","2.jpg"),true);//注册用户
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadUserGPS("5","126;22","江西南昌"),true);//上传用户定位
			//DialogUtil.showDialog(MainActivity.this,Affair.registerUser("zx3","123456","18070516853","3.jpg"),true);//注册用户
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadUserGPS("3","127.1;21.1","江西南昌"),true);//上传用户定位
			//DialogUtil.showDialog(MainActivity.this,Affair.registerUser("zx4","123456","18070516854","4.jpg"),true);//注册用户
			//DialogUtil.showDialog(MainActivity.this,Affair.uploadUserGPS("4","127;20","湖北武汉"),true);//上传用户定位
			//DialogUtil.showDialog(MainActivity.this,Affair.nearByUser("127;21","江西南昌","1"),true);//查看附近的用户
			//DialogUtil.showDialog(MainActivity.this,Affair.addOrderform("2","1","江西财经大学麦庐园","江西财经大学蛟桥园","100"),true);//用户发出订单
			//DialogUtil.showDialog(MainActivity.this,Affair.addOrderform("2","1","江西财经大学麦庐园","江西财经大学蛟桥园","100","127;21"),true);//用户发出订单
			//DialogUtil.showDialog(MainActivity.this,Affair.addOrderform("4","1","江西财经大学麦庐园","江西财经大学蛟桥园","100"),true);//用户发出订单
			//DialogUtil.showDialog(MainActivity.this,Affair.findOrderformById("1"),true);//根据id查看订单
			//DialogUtil.showDialog(MainActivity.this,Affair.getOrderformByUser("1",""),true);//查看用户的订单记录
			//DialogUtil.showDialog(MainActivity.this,Affair.getOrderformByDriver("2",""),true);//查看司机的订单记录
			//DialogUtil.showDialog(MainActivity.this,Affair.setDriverState("2","1"),true);
			//DialogUtil.showDialog(MainActivity.this,Affair.addOrderform("2","1","江西财经大学麦庐园","江西财经大学蛟桥园","127;21"),true);
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("1","江西财经大学麦庐园","江西财经大学蛟桥园","127;21","2015-4-1","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("3","江西财经大学麦庐园","江西财经大学蛟桥园","127;21","2015-4-1","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.deleteRequest("2"),true);//删除预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.findRequestById("1"),true);//根据id找预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.getRequestByUser("1","1"),true);
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("3","江西财经大学麦庐园","江西财经大学蛟桥园","127.1;20.9","2015-4-1","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("4","江西财经大学麦庐园","江西财经大学蛟桥园","127.1;21.05","2015-4-1","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.nearByRequest("127;21","江西南昌",""),true);//附近预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.alterRequest("1","","","2014-4-2","",""),true);//修改预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.completeRequest("1","2","1"),true);//完成预约单交易
			//DialogUtil.showDialog(MainActivity.this,Manage.addComment("还不错","1","1","评论"),true);//评论id为1的评论
			//DialogUtil.showDialog(MainActivity.this,Manage.addComment("还行","1","2","评论"),true);//评论id为1的评论
			//DialogUtil.showDialog(MainActivity.this,Manage.ViewCommentByActivity("1","","评论"),true);//id为1评论的评论
			//DialogUtil.showDialog(MainActivity.this,Manage.addComment("还不错","1","1","活动"),true);//评论活动			
			//DialogUtil.showDialog(MainActivity.this,Manage.ViewCommentByActivity("1","","广播"),true);//id为1广播的评论
			//DialogUtil.showDialog(MainActivity.this,Affair.driverRequest("1","2","1","我对那地方熟,能很快带你去"),true);//司机拉客
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("3","江西财经大学麦庐园","江西财经大学蛟桥园","127.1;20.9","2015-4-7 06:30:00","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("3","江西财经大学麦庐园","江西财经大学蛟桥园","127.1;20.9","2015-4-7 22:30:00","面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.addRequest("3","江西财经大学麦庐园","江西财经大学蛟桥园","127.1;20.9","2015-4-8 22:30:00" ,"面议","江西南昌"),true);//添加预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.completeRequest(id, driverId),true);//预约单交易完成
			//DialogUtil.showDialog(MainActivity.this,Affair.completeRequest("6","2","3"),true);//预约单交易完成
			//DialogUtil.showDialog(MainActivity.this,Affair.completeRequest("7","2","3"),true);//预约单交易完成
			//DialogUtil.showDialog(MainActivity.this,Affair.presentRequest("2"),true);//以后将执行的预约单
			//DialogUtil.showDialog(MainActivity.this,Affair.registerDriver("张三","赣D","123456","18070516854","3.jpg","362421199412300044"),true);//注册司机
			//DialogUtil.showDialog(MainActivity.this,Affair.driverResponse("1","2","接受"),true);//司机接受请求单
			//DialogUtil.showDialog(MainActivity.this,Affair.driverResponse("2","2","拒绝"),true);//司机拒绝请求单
			//DialogUtil.showDialog(MainActivity.this,Affair.driverTodayOrderform("2"),true);//查看今天请求单
			DialogUtil.showDialog(MainActivity.this,Affair.driverTodayRequest("2"),true);//查看今天预约单
		} catch (Exception e) {			
			DialogUtil.showDialog(MainActivity.this,"连接失败："+e.toString(), true);
		}
	}
}