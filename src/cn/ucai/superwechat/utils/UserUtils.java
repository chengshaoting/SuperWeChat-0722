package cn.ucai.superwechat.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.applib.controller.HXSDKHelper;
import cn.ucai.superwechat.DemoHXSDKHelper;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.bean.MemberUserAvatar;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.domain.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.default_avatar).into(imageView);
        } else {
            Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
        }
    }
    /**
     * 设置当前用户头像
     */
    public static void setAppCurrentUserAvatar(ImageView imageView) {
        String userName = SuperWeChatApplication.getInstance().getUserName();
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
        UserAvatar user = SuperWeChatApplication.getInstance().getUser();
        if (textView != null && user!=null) {
            textView.setText(user.getMUserNick());

        } else {
            textView.setText(user.getMUserName());
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
        UserAvatar user = SuperWeChatApplication.getInstance().getUserMap().get(username);
        if(user == null){
            user = new UserAvatar(username);
        }
        return user;
    }
    public static MemberUserAvatar getAppMemberInfo(String hxid,String username){
        MemberUserAvatar member=null;
        HashMap<String, MemberUserAvatar> members =
                SuperWeChatApplication.getInstance().getMemberMap().get(hxid);
        if (members==null||members.size()<0){
            return null;
        }else {
            member = members.get(username);
        }
        return member;
    }

    public static void setAppGroupAvatar(Context context, String username, ImageView avatar) {
        String path = "";
        if(path != null && username != null){
            path = getGroupAvatarPath(username);
            Log.e(TAG, "path=" + path);
            Picasso.with(context).load(path).placeholder(R.drawable.group_icon).into(avatar);
        }else{
            Picasso.with(context).load(R.drawable.group_icon).into(avatar);
        }
    }

    private static String getGroupAvatarPath(String username) {

            StringBuilder path = new StringBuilder(I.SERVER_ROOT);
            path.append(I.QUESTION).append(I.KEY_REQUEST)
                    .append(I.EQU).append(I.REQUEST_DOWNLOAD_AVATAR)
                    .append(I.AND)
                    .append(I.NAME_OR_HXID).append(I.EQU).append(username)
                    .append(I.AND)
                    .append(I.AVATAR_TYPE).append(I.EQU).append(I.AVATAR_TYPE_GROUP_PATH);
            return path.toString();

    }

    public static void setAppMemberNick(String hxid, String username, TextView textView) {
        MemberUserAvatar member = getAppMemberInfo(hxid, username);
        Log.e(TAG,"member="+member);
        if(member!=null&&member.getMUserNick()!=null){
            textView.setText(member.getMUserNick());
        }else {
            textView.setText(username);
        }
    }
}
