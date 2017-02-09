package com.lawrence.core.lib.utils.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;


/**
 * 获取手机的基本参数
 * <p/>
 * Created by lawrence on 2015/11/25.
 */
public class PhoneUtil {
//    private static Context context;

    private static TelephonyManager telephonyManager;
    private static WifiManager wifiManager;

    public static PhoneUtil getInstance(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return SingletonHolder.getInstance();
    }

    private static class SingletonHolder {
        private static PhoneUtil instance = new PhoneUtil();

        public static PhoneUtil getInstance() {
            return instance;
        }
    }

    /**
     * 获取设备IMEI
     *
     * @return deviceid 设备IMEI号
     */
    public String getDeviceId() {
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取设备唯一标识
     *
     * @return subscriberId 设备唯一标识
     */
    public String getSubscriberId() {
        return telephonyManager.getSubscriberId();
    }

    /**
     * 获取设备软件版本
     *
     * @return DeviceSoftwareVersion 设备软件版本
     */
    public String getDeviceSoftwareVersion() {
        return telephonyManager.getDeviceSoftwareVersion();
    }

    /**
     * 获取运营商
     *
     * @return provider 服务运营商
     */
    public String getProvider() {
        String provider = "未知";
        try {
            String IMSI = telephonyManager.getSubscriberId();
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * 获取当前设备的 Android 版本信息
     *
     * @return
     */
    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 获取服务商名称
     *
     * @return
     */
    public String getSimOperator() {
        return telephonyManager.getSimOperator();
    }


    /**
     * 获取SIM卡的序列号
     *
     * @return
     */
    public String getSimSeriaNumber() {
        return telephonyManager.getSimSerialNumber();
    }


    /**
     * 获取手机型号
     *
     * @return phone model
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取android版本
     *
     * @return sdk version
     */
    public String getAPILevel() {
        return Build.VERSION.SDK;
    }


    /**
     * 获取设备的网卡地址
     *
     * @return
     * @hide 不可用
     */
    public String getMac() {
        String macAddress = "";
        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public String getNetworkType() {
        String networkType;
        int type = telephonyManager.getNetworkType();
        switch (type) {
            case 1:
                networkType = "GPRS";
                break;
            case 2:
                networkType = "EDGE";
                break;
            case 3:
                networkType = "UMTS";
                break;
            case 8:
                networkType = "HSDPA";
                break;
            case 9:
                networkType = "HSUPA";
                break;
            case 10:
                networkType = "HSPA";
                break;
            case 4:
                networkType = "CDDMA";
                break;
            case 5:
                networkType = "EVDO_0";
                break;
            case 6:
                networkType = "EVDO_A";
                break;
            case 7:
                networkType = "1xRTT";
                break;
            default:
                networkType = "";
                break;
        }
        return networkType;
    }

    public String getSDPath() {
        String sdDir = null;
        if (SDCardUtil.isAvailable()) {
            sdDir = SDCardUtil.getSDCardPath();
        }
        return sdDir;
    }


    public boolean sdcardIsAvailable() {
        return SDCardUtil.isAvailable();
    }


}

