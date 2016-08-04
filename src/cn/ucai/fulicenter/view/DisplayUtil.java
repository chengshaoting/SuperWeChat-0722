package cn.ucai.fulicenter.view;

import android.app.Activity;

import cn.ucai.fulicenter.R;
import android.view.View;

/**
 * Created by sks on 2016/8/3.
 */
public class DisplayUtil {
    public static void initBack(final Activity activity){
        activity.findViewById(R.id.backClickArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
    public static void Back(final Activity activity){
        activity.findViewById(R.id.backClickArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

}
