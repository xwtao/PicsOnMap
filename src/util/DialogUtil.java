
package util;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class DialogUtil{
	private static ProgressDialog mProgressDialog;
	// 定义一个显示消息的对话框
	public static void showDialog(final Context ctx, String msg , boolean closeSelf){
		// 创建一个AlertDialog.Builder对象
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
		if(closeSelf)
		{
			builder.setPositiveButton("确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// 结束当前Activity
					((Activity)ctx).finish();
				}
			});		
		}
		else
		{
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}	
	// 定义一个显示指定组件的对话框
	public static void showDialog(Context ctx , View view)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setView(view).setCancelable(false)
		.setPositiveButton("确定", null);
		builder.create().show();
	}
	
	public static final void showResultDialog(Context context, String msg,
			String title) {
		if(msg == null) return;
		String rmsg = msg.replace(",", "\n");
		Log.d("Util", rmsg);
		new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
				.setNegativeButton("知道?", null).create().show();
	}

	public static final void showProgressDialog(Context context, String title,
			String message) {
		dismissDialog();
		if (TextUtils.isEmpty(title)) {
			title = "请稍候";
		}
		if (TextUtils.isEmpty(message)) {
			message = "正在加载...";
		}
		mProgressDialog = ProgressDialog.show(context, title, message);
	}
	
	public static AlertDialog showConfirmCancelDialog(Context context,
			String title, String message,
			DialogInterface.OnClickListener posListener) {
		AlertDialog dlg = new AlertDialog.Builder(context).setMessage(message)
				.setPositiveButton("确认", posListener)
				.setNegativeButton("取消", null).create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		return dlg;
	}

	public static final void dismissDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
