package com.huyu.sdk.data.bean;

import android.content.Context;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimResolve {
	/** 设备id */
	public static String deviceId = "";
	/** 设备型号 */
	public static String model = "";
	/** Sims号 */
	public static String simsNum = "";
	/** 手机号码 */
	public static String phoneNum = "";
	/** IMEI号 */
	public static String imei = "";
	/** IMSI */
	public static String imsi = "";
	/** mac地址 */
	public static String macAddress = "";
	/** 运营商 */
	public static String spType = "未知";
	/** SDK版本 */
	public static String sdk_version = "";
	/** 设备的系统版本 */
	public static String release_version = "";
	/** 纬度 */
	public static double longitude;
	/** 纬度 */
	public static double latitude;
	public static Context mContext;
	//private static TelephonyManager telMgr;
	/**
	 * @param
	 */
	public static void getDeviceInfo(Context context) {
		Log.d("","------->u9获取设备信息<--------");
		mContext = context;
		/*try {
			model = getPhoneModel();
		} catch (Exception e) {
			HY_Log.e("getPhoneModel 获取异常:" + e.toString());
		}
		try {
			sdk_version = getVersionSDK();
		} catch (Exception e) {
			HY_Log.e("getVersionSDK 获取异常:" + e.toString());
		}
		try {
			release_version = getVersionRelease();
		} catch (Exception e) {
			HY_Log.e("getVersionRelease 获取异常:" + e.toString());
		}
//		getLocation();
		// 获取手机型号
		if (!TextUtils.isEmpty(imei)) {
			return;
		}
		
		try {
			telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			simsNum = telMgr.getSimSerialNumber();
			imsi = telMgr.getSubscriberId();
		} catch (Exception e) {
			HY_Log.e("simsNum 获取异常:" + e.toString());
		}
		try {

			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				spType = "中国移动";
			} else if (imsi.startsWith("46001")) {
				spType = "中国联通";
			} else if (imsi.startsWith("46003")) {
				spType = "中国电信";
			}
		} catch (Exception e) {
			HY_Log.e("imsi 获取异常:" + e.toString());
		}

		try {
			imei = telMgr.getDeviceId();
		} catch (Exception e) {
			HY_Log.e("imei 获取异常:" + e.toString());
			e.printStackTrace();// imei获取不到
		} finally {
			if (imei == null || "".equals(imei)) {
				imei = "";
			}
		}

		// 获取手机号吗
		try {
			phoneNum = telMgr.getLine1Number();
			if (phoneNum != null && phoneNum.contains("+")) {
				phoneNum = phoneNum.substring(3, phoneNum.length());
			}
		} catch (Exception e) {
			HY_Log.e("手机号 获取异常:" + e.toString());
			e.printStackTrace();// imei获取不到
		}
		// 获取mac地址
		// 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0

		try {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				macAddress = info.getMacAddress();
			}
			if (!TextUtils.isEmpty(macAddress)) {
				macAddress = macAddress.replaceAll(":", "");
				macAddress = macAddress.replaceAll("-", "");
			}
		} catch (Exception e) {
			e.printStackTrace(); // mac获取不到
		} finally {
			if (macAddress == null || "".equals(macAddress)) {
				macAddress = "no_macAddress";
			}
		}*/
		// HY_Log.d(TAG, "imei:" + imei + "," + "mac" + macAddress);
		showLog();
	}

	/**
	 * 获取手机型号
	 **/
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机型号
	 **/
	@SuppressWarnings("deprecation")
	public static String getVersionSDK() {
		return android.os.Build.VERSION.SDK;
	}

	/**
	 * 获取手机型号
	 **/
	public static String getVersionRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/*public static void getLocation() {
		try {
			LocationManager locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Location location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
			} else {
				LocationListener locationListener = new LocationListener() {

					// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {

					}

					// Provider被enable时触发此函数，比如GPS被打开
					@Override
					public void onProviderEnabled(String provider) {

					}

					// Provider被disable时触发此函数，比如GPS被关闭
					@Override
					public void onProviderDisabled(String provider) {

					}

					// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
					@Override
					public void onLocationChanged(Location location) {
						if (location != null) {
							HY_Log.e("Map", "Location changed : Lat: "
									+ location.getLatitude() + " Lng: "
									+ location.getLongitude());
						}
					}
				};
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 1000, 0,
						locationListener);
				Location location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude(); // 经度
					longitude = location.getLongitude(); // 纬度
				}
			}
		} catch (Exception e) {
			HY_Log.e("GPS获取异常:" + e.toString());
		}
	}*/

	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		System.out.println(m.matches() + "---");

		return m.matches();

	}

	public static void showLog() {
		Log.d("","model:" + model + "#simsNum:" + simsNum + "#phoneNum:"
				+ phoneNum + "#imei:" + imei + "#imsi:" + imsi + "#macAddress:"
				+ macAddress + "#spType:" + spType + "#sdk_version:"
				+ sdk_version + "#release_version:" + release_version
				+ "#latitude:" + latitude + "#longitude" + longitude);
	}

}
