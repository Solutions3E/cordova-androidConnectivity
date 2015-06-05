/**
 * Cordova Android Plugin for Android Connectivity Data.
 */

package com.eeesolutions.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.List;
import org.json.JSONObject;


public class AndroidConnectivity extends CordovaPlugin {
	private String signalDBM;
	TelephonyManager TelManager;
    CallbackContext callbackContext;
    Context context;
	public class GetParams extends PhoneStateListener {
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);

			try {
				signalDBM = signalStrength.getGsmSignalStrength() + "";
			} catch (Exception e) {

			}

			String jsonresult = getResults(signalDBM);
			callbackContext.success(jsonresult);

			TelManager.listen(this, PhoneStateListener.LISTEN_NONE);

		}
	}

	@Override
	public boolean execute(String action, JSONArray data,
			CallbackContext callbackContext) throws JSONException {
		 context = this.cordova.getActivity().getApplicationContext();
		this.callbackContext=callbackContext;
		if (action.equals("getjsonresult")) {

			GetParams listener = new GetParams();
			TelManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			TelManager.listen(listener,
					PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

			return true;
		}

		return false;
	}

	public String getResults(String signalDBM) {

		JSONObject details = new JSONObject();
		try {


			details.put("isconnected", Connectivity.isConnectedWifi(context)+"");
			details.put("isConnectedWifi", Connectivity.isConnectedWifi(context)
					+ "");
			details.put("isConnectedMobile",
					Connectivity.isConnectedMobile(context) + "");
			details.put("WifiLinkSpeed", Connectivity.getWifiLinkSpeed(context)
					+ "");
			details.put("WifiSignalLevel",
					Connectivity.getWifiSignalLevel(context) + "");
			details.put("isConnectedFast", Connectivity.isConnectedFast(context)
					+ "");
			details.put("WifiBSSID", Connectivity.getWifiBSSID(context) + "");
			String ssid = Connectivity.getWifiConnectionDetails(context);
			details.put("wifiSSID", ssid);
			details.put("cellSignalStrength", signalDBM);

			return details + "";

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static class Connectivity {

		public static NetworkInfo getNetworkInfo(Context context) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return cm.getActiveNetworkInfo();
		}

		public static boolean isConnected(Context context) {
			NetworkInfo info = Connectivity.getNetworkInfo(context);
			return (info != null && info.isConnected());
		}

		public static boolean isConnectedWifi(Context context) {
			NetworkInfo info = Connectivity.getNetworkInfo(context);
			return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
		}

		public static boolean isConnectedMobile(Context context) {
			NetworkInfo info = Connectivity.getNetworkInfo(context);
			return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
		}

		public static int getWifiLinkSpeed(Context context) {

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			int linkSpeed = wifiManager.getConnectionInfo().getRssi();
			return linkSpeed;
		}

		public static int getWifiSignalLevel(Context context) {

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			int numberOfLevels = 5;
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),
					numberOfLevels);
			return level;
		}

		public static String getWifiBSSID(Context context) {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifiManager.getConnectionInfo();
			return info.getBSSID();
		}

		public static boolean isConnectedFast(Context context) {
			NetworkInfo info = Connectivity.getNetworkInfo(context);
			return (info != null && info.isConnected() && Connectivity
					.isConnectionFast(info.getType(), info.getSubtype()));
		}

		public static boolean isConnectionFast(int type, int subType) {
			if (type == ConnectivityManager.TYPE_WIFI) {
				return true;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return false; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return false; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return true; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return true; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return false; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return true; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return true; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return true; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return true; // ~ 400-7000 kbps
					/*
					 * Above API level 7, make sure to set
					 * android:targetSdkVersion to appropriate level to use
					 * these
					 */
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					return true; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					return true; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					return true; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					return false; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					return true; // ~ 10+ Mbps
					// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					return false;
				}
			} else {
				return false;
			}
		}

		public static String getWifiConnectionDetails(Context context) {
			String ssid = null;

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			List<ScanResult> results = wifiManager.getScanResults();
			if (results != null) {
				final int size = results.size();
				if (size != 0) {
					for (ScanResult result : results) {

						if (result.BSSID.equals(getWifiBSSID(context))) {

							ssid = result.SSID;
						}

					}

				}
			}
			return ssid;

		}

	}

}
