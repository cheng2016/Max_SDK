package com.huyu.sdk.data.config;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.SharedPreferencesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * @author chengzj
 * @time 2020/7/20 16:01
 * Description: XML、
 */
public class PhoneInfoHelper {
    /**
     * 设备id
     */
    public static String deviceId = "";
    /**
     * 设备型号
     */
    public static String model = "";
    /**
     * Sims号
     */
    public static String simsNum = "";
    /**
     * 手机号码
     */
    public static String phoneNum = "";
    /**
     * IMEI号
     */
    public static String imei = "";
    /**
     * IMSI
     */
    public static String imsi = "";
    /**
     * mac地址
     */
    public static String macAddress = "";
    /**
     * 运营商
     */
    public static String spType = "未知";
    /**
     * SDK版本
     */
    public static String sdk_version = "";
    /**
     * 设备的系统版本
     */
    public static String release_version = "";
    /**
     * 纬度
     */
    public static double longitude;
    /**
     * 纬度
     */
    public static double latitude;

    private static PhoneInfoHelper instance;

    public static PhoneInfoHelper getInstance() {
        if (instance == null)
            instance = new PhoneInfoHelper();
        return instance;
    }

    public void init(Context context) {
        getPhoneInfo(context);
        getU9PhoneInfo(context);
    }


    public void getPhoneInfo(Context context) {
        String imeiFile, imeiStorage;
        try {
            imeiFile = getFile("imei" + ".txt");
            if (TextUtils.isEmpty(imeiFile)) {
                imeiFile = getFile("imei" + "_" + Constant.APPID + ".txt");
            }
        } catch (Exception e) {
            e.printStackTrace();
            imeiFile = "";
            Logger.e("hy_imei文件读取失败:" + e.toString());
        }
        try {
            imeiStorage = SharedPreferencesUtils.getData(context, "imei", "");
        } catch (Exception e) {
            e.printStackTrace();
            imeiStorage = "";
            Logger.e("hy_imei存储获取失败:" + e.toString());
        }
        if (!TextUtils.isEmpty(imeiFile)) {
            this.imei = imeiFile;
            Logger.d("hy_imei文件读取不为空:" + imeiFile);
        } else if (!TextUtils.isEmpty(imeiStorage)) {
            this.imei = imeiStorage;
            Logger.d("hy_imei存储读取不为空:" + imeiStorage);
        }
    }

    void getU9PhoneInfo(Context context) {
        String imeiFile = getFile("u9imei" + ".txt");
        String imeiSp = SharedPreferencesUtils.getData(context, "u9imei", "");
        if (!TextUtils.isEmpty(imeiFile)) {
            this.imei = imeiFile;
            Logger.d("imei sdcard文件读取不为空:" + imeiFile);
        } else if (!TextUtils.isEmpty(imeiSp)) {
            this.imei = imeiSp;
            Logger.d("imei sp文件读取不为空:" + imeiSp);
        }
    }

    /**
     * 获取自定义目录下txt文件的内容
     *
     * @param fileName 文件名字 格式*.txt
     * @return
     */
    public static String getFile(String fileName) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD卡目录的*.txt
            File sdcardDir = Environment.getExternalStorageDirectory();
            filePath = sdcardDir.getPath() + File.separator + "hy_game_sdk"
                    + File.separator + fileName;
        } else { // 系统下载缓存目录的*.txt
            File sdcardDir = Environment.getDownloadCacheDirectory();
            filePath = sdcardDir.getPath() + File.separator + "hy_game_sdk"
                    + File.separator + fileName;
        }
        try {
            // 创建FIle对象
            File file = new File(filePath);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            // 获取文件中的内容
            String content = br.readLine();
            // 关闭流对象
            fis.close();
            br.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(fileName + " 文件读取异常:" + e.toString());
        }
        return "";
    }

    /**
     * sdcard自定义目录保存string数据
     *
     * @param fileName
     *            文件名字 格式*.txt
     * @param str
     *            存入的字符串
     */
    public static void saveFile(String fileName, String str) {
        String filePath = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (hasSDCard) { // SD卡自定义目录的*.txt
            File sdcardDir = Environment.getExternalStorageDirectory();
            filePath = sdcardDir.getPath() + File.separator + "hy_game_sdk"
                    + File.separator + fileName;
        } else { // 系统下载缓存自定义目录的*.txt
            File sdcardDir = Environment.getDownloadCacheDirectory();
            filePath = sdcardDir.getPath() + File.separator + "hy_game_sdk"
                    + File.separator + fileName;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(fileName + " 文件写入异常:" + e.toString());
        }
    }
}
