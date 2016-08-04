package cn.ucai.fulicenter.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.applib.controller.HXSDKHelper;
import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.domain.User;
import com.squareup.picasso.Picasso;

public class UserUtils {
    private static final String TAG = UserUtils.class.getSimpleName();


    public static void setCurrentUserAvatar(Context context, ImageView imageView) {
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if(user != null && user.getAvatar() != null) {
            Picasso.with(context).load(user.getAvatar()).placeholder(2130837620).into(imageView);
        } else {
            Picasso.with(context).load(2130837620).into(imageView);
        }

    }

    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */

    public static User getUserInfo(String username){
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){

            user = new User(username);
        }

        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }

    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    

    /**
     * 设置当前用户头像
     */
    public static void setAppCurrentUserAvatar(Context context, ImageView imageView) {
        User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if (user != null && user.getAvatar() != null) {
            Log.e(TAG,"setCrrentUserAvatar.url="+user.getAvatar());
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    /**
     * 设置当前用户头像
     */
    public static void setAppCurrentUserAvatar(ImageView imageView) {
        String userName = FuliCenterApplication.getInstance().getUserName();
        if (userName != null && imageView != null) {
            setAppUserAvatar(null,userName,imageView);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
       User user = getUserInfo(username);
        if(user!=null){
            textView.setText(user.getNick());
        }else {
            textView.setText(username);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setAppUserNick(UserAvatar user,TextView textView){
        if(user != null){
            if(user.getMUserNick()!=null){
                textView.setText(user.getMUserNick());
            }else{
                textView.setText(user.getMUserName());

        }
        }
    }
    public static void setAppUserNick(String username, TextView nameTextview) {
        UserAvatar user = getAppUserInfo(username);
        setAppUserNick(user,nameTextview);
    }
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	User user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
    
    public static void setAppCurrentUserNick(TextView textView){
        UserAvatar user = FuliCenterApplication.getInstance().getUser();
        if (textView != null && user!=null) {
            textView.setText(user.getMUserNick());

        } else {
           textView.setText(FuliCenterApplication.currentUserNick);
        }
    }

    /**
     * 保存或更新某个用户
     */
	public static void saveUserInfo(User newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}

    public static void setAppUserAvatar(Context context, String username, ImageView avatar) {
        String path = "";
        if(path != null && username != null){
            path = getUserAvatarPath(username);
            Log.e(TAG, "path=" + path);
            Picasso.with(context).load(path).placeholder(R.drawable.default_avatar).into(avatar);
        }else{
            Picasso.with(context).load(R.drawable.default_avatar).into(avatar);
        }
    }

    public static String getUserAvatarPath(String username) {
        StringBuilder path = new StringBuilder(I.SERVER_ROOT);
        path.append(I.QUESTION).append(I.KEY_REQUEST)
                .append(I.EQU).append(I.REQUEST_DOWNLOAD_AVATAR)
                .append(I.AND)
                .append(I.NAME_OR_HXID).append(I.EQU).append(username)
                .append(I.AND)
                .append(I.AVATAR_TYPE).append(I.EQU).append(I.AVATAR_TYPE_USER_PATH);
        return path.toString();

    }




    private static UserAvatar getAppUserInfo(String username) {
        UserAvatar user = FuliCenterApplication.getInstance().getUserMap().get(username);
        if(user == null){
            user = new UserAvatar(username);
        }
        return user;
    }
}
