
package com.rigoiot;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.os.Looper;
import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.wellcom.verify.GfpInterface;

import java.util.HashMap;
import java.util.Map;

enum FingerprintState {
  IDLE,
  INIT,
  BT_OPENING,
  BT_OPENED,
  BT_CONNECTING,
  BT_CONNECTED,
  BT_DISCONNECTED,
  READY
}

public class RNRigoFingerprintModule extends ReactContextBaseJavaModule {

  private static final String TAG = "RNRigoFingerprintModule";

  private final ReactApplicationContext reactContext;

  private GfpInterface  mCGfpInterface = null;
  private FingerprintState mState = FingerprintState.IDLE;
  private String mVer = "";
  private int mError = 0;

  public RNRigoFingerprintModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNRigoFingerprint";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("state", mState);
    constants.put("version", mVer);
    constants.put("isEmulator", isEmulator());
    return constants;
  }
  /**
   * Utility methods related to physical devies and emulators.
   */
  private boolean isEmulator() {
      return Build.FINGERPRINT.startsWith("generic")
              || Build.FINGERPRINT.startsWith("unknown")
              || Build.MODEL.contains("google_sdk")
              || Build.MODEL.contains("Emulator")
              || Build.MODEL.contains("Android SDK built for x86")
              || Build.MANUFACTURER.contains("Genymotion")
              || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
              || "google_sdk".equals(Build.PRODUCT);
  }

  private  void setState(FingerprintState state) {
    Log.v(TAG, "State: (" + mState.toString() + ") -> (" + state.toString() + ")");
    mState = state;
  }

  @ReactMethod
  public void init() {
    Log.v(TAG, "init()");
    if (!isEmulator()) {
      mCGfpInterface = new GfpInterface(reactContext, mFpHandler);
      setState(FingerprintState.IDLE);
    }
  }

  @ReactMethod
  public void destroy() {
    Log.v(TAG, "init()");
    setState(FingerprintState.IDLE);
    if (mCGfpInterface != null) {
      mCGfpInterface.sysExit();
      mCGfpInterface = null;
    }
  }

  @ReactMethod
  public void sysOnResume() {
    Log.v(TAG, "sysOnResume()");
    setState(FingerprintState.IDLE);
    if (mCGfpInterface != null) {
      mCGfpInterface.sysOnResume();
    }
  }

  @ReactMethod
  public void fpiOpenBT(Callback cb) {
    Log.v(TAG, "fpiOpenBT()");
    setState(FingerprintState.BT_OPENING);
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.fpiOpenBT();
      if (cb != null) {
        cb.invoke(ret);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  @ReactMethod
  public void sysCloseBT() {
    Log.v(TAG, "sysCloseBT()");
    setState(FingerprintState.IDLE);
    if (mCGfpInterface != null) {
      mCGfpInterface.sysCloseBT();
    }
  }

  @ReactMethod
  public void fpiConnectBT(String btName, Callback cb) {
    Log.v(TAG, "fpiConnectBT(" + btName + ")");
    setState(FingerprintState.BT_CONNECTING);
    if (mCGfpInterface != null) {
      int ret = mCGfpInterface.fpiConnectBT(btName);
      if (cb != null) {
        cb.invoke(ret);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  @ReactMethod
  public void fpiDisconnectBT() {
    Log.v(TAG, "fpiDisconnectBT()");
    setState(FingerprintState.BT_DISCONNECTED);
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiDisconnectBT();
    }
  }

  @ReactMethod
  public void fpiCheckBTOpened(Callback cb) {
    Log.v(TAG, "fpiCheckBTOpened()");
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckBTOpened();
      Log.v(TAG, "fpiCheckBTOpened() -> " + ret);
      if (cb != null) {
        cb.invoke(ret);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  @ReactMethod
  public void fpiCheckBTConnected(Callback cb) {
    Log.v(TAG, "fpiCheckBTConnected()");
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckBTConnected();
      Log.v(TAG, "fpiCheckBTConnected() -> " + ret);
      if (cb != null) {
        cb.invoke(ret);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  @ReactMethod
  public void fpiCheckUSBConnected(Callback cb) {
    Log.v(TAG, "fpiCheckUSBConnected()");
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckUSBConnected();
      Log.v(TAG, "fpiCheckUSBConnected() -> " + ret);
      if (cb != null) {
        cb.invoke(ret);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  @ReactMethod
  public void fpiGetVersion() {
    Log.v(TAG, "fpiGetVersion()");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetVersion();
    }
  }

  @ReactMethod
  public void fpiGetDevSN() {
    Log.v(TAG, "fpiGetDevSN()");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevSN();
    }
  }

  @ReactMethod
  public void fpiSetDevSN(String sn) {
    Log.v(TAG, "fpiGetDevSN(" + sn + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetDevSN(sn);
    }
  }

  @ReactMethod
  public void fpiGetImage(int timeOut) {
    Log.v(TAG, "fpiGetImage(" + timeOut + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetImage(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetDevFTR(int timeOut) {
    Log.v(TAG, "fpiGetDevFTR(" + timeOut + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevFTR(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetDevTPT(int timeOut, int fpSaveNum) {
    Log.v(TAG, "fpiGetDevTPT(" + timeOut + ", " + fpSaveNum + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevTPT(timeOut, fpSaveNum);
    }
  }

  @ReactMethod
  public void fpiGetFeature(int timeOut) {
    Log.v(TAG, "fpiGetFeature(" + timeOut + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetFeature(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetTemplate(int timeOut) {
    Log.v(TAG, "fpiGetTemplate(" + timeOut + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetTemplate(timeOut);
    }
  }

  @ReactMethod
  public void fpiCheckFinger() {
    Log.v(TAG, "fpiCheckFinger()");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiCheckFinger();
    }
  }

  @ReactMethod
  public void fpiGetTPTCnt() {
    Log.v(TAG, "fpiGetTPTCnt()");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetTPTCnt();
    }
  }

  @ReactMethod
  public void fpiDeleteTPT(int fpDelNum) {
    Log.v(TAG, "fpiDeleteTPT()");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiDeleteTPT(fpDelNum);
    }
  }

  @ReactMethod
  public void fpiSetBtName(String btName) {
    Log.v(TAG, "fpiSetBtName(" + btName + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetBtName(btName);
    }
  }

  @ReactMethod
  public void fpiSetSleepTime(int sleepTime) {
    Log.v(TAG, "fpiSetSleepTime(" + sleepTime + ")");
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetSleepTime(sleepTime);
    }
  }

  @ReactMethod
  public void fpiDownVerify(String template, String feature) {
    Log.v(TAG, "fpiDownVerify(" + template + ", " + feature + ")");
    if (mCGfpInterface != null) {
      byte[] tpt = Base64.decode(template, Base64.DEFAULT);
      byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
      mCGfpInterface.fpiDownVerify(tpt, ftr);
    }
  }

  @ReactMethod
  public void sysOneMatch(String template, String feature, Callback cb) {
    Log.v(TAG, "sysOneMatch(" + template + ", " + feature + ")");
    if (mCGfpInterface != null) {
      byte[] tpt = Base64.decode(template, Base64.DEFAULT);
      byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
      int score = mCGfpInterface.sysOneMatch(tpt, ftr);
      if (cb != null) {
        cb.invoke(score);
      }
    } else {
      if (cb != null) {
        cb.invoke(-1);
      }
    }
  }

  // @ReactMethod
  // public void sysSearchMatch(int templateCount, String template, String feature, int[] templateID) {
  //   byte[] tpt = Base64.decode(template, Base64.DEFAULT);
  //   byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
  //   mCGfpInterface.sysSearchMatch(templateCount, tpt, ftr, templateID);
  // }

  // Send event to JS
  private void sendEvent(String eventName,
                         @Nullable WritableMap params) {
    reactContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, params);
  }

  // 蓝牙异步消息处理
  Handler mFpHandler = new Handler(Looper.getMainLooper()) {
    public void handleMessage(Message msg){
      super.handleMessage(msg);
      Log.v(TAG, "Event: " + msg.toString());
      switch(msg.what) {
        case 0xA0:  // Error message
        {
          setState(FingerprintState.IDLE);
           mError = msg.getData().getInt("FPIGetError");
          WritableMap map = Arguments.createMap();
          map.putInt("error", mError);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xA1: // public int fpiConnectBT(String strBTName)
        {
          int btStatus = msg.getData().getInt("FPIBTStatus");
          if (btStatus == 1) {
            setState(FingerprintState.BT_CONNECTED);
          } else {
            setState(FingerprintState.BT_DISCONNECTED);
          }
          WritableMap map = Arguments.createMap();
          map.putInt("btStatus", btStatus);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xA4:  // public void fpiGetFeature (int iTimeOut)
        {
          setState(FingerprintState.READY);
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenFTR", bytesLenFTR);
          if (bytesLenFTR > 0) {
            map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xA5:  // public void fpiGetTemplate (int iTimeOut)
        {
          setState(FingerprintState.READY);
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenTPT", bytesLenTPT);
          if (bytesLenTPT > 0) {
            map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB0:  // public void fpiGetVersion()
        {
          setState(FingerprintState.READY);
          WritableMap map = Arguments.createMap();
          mVer = msg.getData().getString("FPIGetDevVer");
          map.putString("devVer", mVer);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB1:  // public void fpiGetDevSN()
        {
          setState(FingerprintState.READY);
          WritableMap map = Arguments.createMap();
          map.putString("devSN", msg.getData().getString("FPIGetDevSN"));
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB2:  // public void fpiSetDevSN(String strSN)
        {
          setState(FingerprintState.READY);
          WritableMap map = Arguments.createMap();
          map.putString("devSN", msg.getData().getString("FPISetDevSN"));
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB3:  // public void fpiGetImage(int iTimeOut)
        {
          setState(FingerprintState.READY);
          byte[] fpImage = (byte[]) msg.obj;
          int byteLenIMG = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("byteLenIMG", byteLenIMG);
          if (byteLenIMG > 0) {
            map.putString("fpImage", Base64.encodeToString(fpImage, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB4:  // public void fpiGetDevFTR(int iTimeOut)
        {
          setState(FingerprintState.READY);
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenFTR", bytesLenFTR);
          if (bytesLenFTR > 0) {
            map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB5:  // public void fpiGetDevTPT(int iTimeOut, int iFpSaveNum)
        {
          setState(FingerprintState.READY);
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenTPT", bytesLenTPT);
          if (bytesLenTPT > 0) {
            map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB6: // public void fpiDownVerify(byte[] byteArryTPT, byte[] byteArryFTR)
        {
          setState(FingerprintState.READY);
          int downVerify = msg.getData().getInt("FPIDownVerify");
          WritableMap map = Arguments.createMap();
          map.putInt("downVerify", downVerify);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB7:  // public void fpiSearchMatch(int iTimeOut)
        {
          setState(FingerprintState.READY);
          int searchMatch = msg.getData().getInt("FPISearchMatch");
          WritableMap map = Arguments.createMap();
          map.putInt("searchMatch", searchMatch);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB8:  // public void fpiCheckFinger()
        {
          setState(FingerprintState.READY);
          int checkFinger = msg.getData().getInt("FPICheckFinger");
          WritableMap map = Arguments.createMap();
          map.putInt("checkFinger", checkFinger);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xB9:  // EncryptTest --Reserved
          break;
        case 0xBA:  // DecryptTest --Reserved   String.valueOf(i),  Integer.toString(i)
          break;
        case 0xBB: // public void fpiGetTPTCnt()
        {
          setState(FingerprintState.READY);
          int fpTPTCount = msg.getData().getInt("FPIGetTPTCnt");
          WritableMap map = Arguments.createMap();
          map.putInt("fpTPTCount", fpTPTCount);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintevent", map);
          break;
        }
        case 0xBC: // public void fpiDeleteTPT(int iFpDelNum)
        {
          setState(FingerprintState.READY);
          int deleteTPT = msg.getData().getInt("FPIDeleteTPT");
          WritableMap map = Arguments.createMap();
          map.putInt("deleteTPT", deleteTPT);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xBD:
        {
          setState(FingerprintState.READY);
          int setBtName = msg.getData().getInt("FPISetBtName");
          WritableMap map = Arguments.createMap();
          map.putInt("setBtName", setBtName);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
        case 0xBE:
        {
          setState(FingerprintState.READY);
          int setSleepTime = msg.getData().getInt("FPISetSleepTime");
          WritableMap map = Arguments.createMap();
          map.putInt("setSleepTime", setSleepTime);
          map.putInt("type", msg.what);
          sendEvent("rigoiotFingerPrintEvent", map);
          break;
        }
      }
    }
  };
}
