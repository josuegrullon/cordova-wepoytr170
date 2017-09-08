/**
 */
package com.example;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.app.Activity;

import java.util.Date;

public class WepoyPrint extends CordovaPlugin {
  private static final String TAG = "WepoyPrint";

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

      Intent intent = new Intent();
      intent.setPackage("woyou.aidlservice.jiuiv5");
      intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
      webView.getContext().startService(intent);//启动打印服务
      webView.getContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    Log.d(TAG, "Initializing WepoyPrint");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if(action.equals("echo")) {
      String phrase = args.getString(0);
      // Echo back the first argument
        try {
            // woyouService.printerInit(callback);
            woyouService.lineWrap(1, callback);
            woyouService.printText(phrase, callback);
            woyouService.lineWrap(1, callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
      Log.d(TAG, phrase);
    } else if(action.equals("getDate")) {
      // An example of returning data back to the web layer
      final PluginResult result = new PluginResult(PluginResult.Status.OK, (new Date()).toString());
      callbackContext.sendPluginResult(result);
    }
    return true;
  }
    private IWoyouService woyouService;

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };


    ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) throws RemoteException {
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException {
        }
    };

}