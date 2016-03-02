package com.example.zhl.weatherdemo.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/3/1.
 * 截图工具类
 */
public class ScreenShotUtils {

    public static Bitmap takeScreenShot(Activity activity){
        Bitmap bitmap = null;
        View view = activity.getWindow().getDecorView();

        //设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        //如果绘图无法缓存，强制构建绘图缓存
        view.buildDrawingCache();
        //返回缓存视图
        bitmap = view.getDrawingCache();

        //获取状态栏的高度
        Rect frame = new Rect();
        //测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        Log.d("状态栏高度","状态栏的高度是："+statusHeight);

        int width=activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        //根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap,0,statusHeight,width,height-statusHeight);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /*
    *保存图片到sd卡中
     */
    public static boolean savePic(Bitmap bitmap,String strName){
        FileOutputStream fos = null;
        try {
            File dir = new File(strName);
            dir.mkdirs();
            File file = new File(dir,"temp.png");
            fos = new FileOutputStream(file);
            if (null != fos){
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
