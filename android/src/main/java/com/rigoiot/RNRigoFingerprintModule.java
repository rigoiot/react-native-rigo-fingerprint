
package com.rigoiot;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.os.Looper;
import android.os.Build;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.wellcom.verify.GfpInterface;

public class RNRigoFingerprintModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private GfpInterface  mCGfpInterface = null;

  public RNRigoFingerprintModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNRigoFingerprint";
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

  @ReactMethod
  public void init() {
    if (!isEmulator()) {
      mCGfpInterface = new GfpInterface(reactContext, mFpHandler);
    }
  }

  @ReactMethod
  public void destroy() {
    if (mCGfpInterface != null) {
      mCGfpInterface.sysExit();
      mCGfpInterface = null;
    }
  }

  @ReactMethod
  public void sysOnResume() {
    if (mCGfpInterface != null) {
      mCGfpInterface.sysOnResume();
    }
  }

  @ReactMethod
  public void fpiOpenBT(Callback cb) {
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
    if (mCGfpInterface != null) {
      mCGfpInterface.sysCloseBT();
    }
  }

  @ReactMethod
  public void fpiConnectBT(String btName, Callback cb) {
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
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiDisconnectBT();
    }
  }

  @ReactMethod
  public void fpiCheckBTOpened(Callback cb) {
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckBTOpened();
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
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckBTConnected();
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
    if (mCGfpInterface != null) {
      boolean ret = mCGfpInterface.sysCheckUSBConnected();
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
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetVersion();
    }
  }

  @ReactMethod
  public void fpiGetDevSN() {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevSN();
    }
  }

  @ReactMethod
  public void fpiSetDevSN(String sn) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetDevSN(sn);
    }
  }

  @ReactMethod
  public void fpiGetImage(int timeOut) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetImage(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetDevFTR(int timeOut) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevFTR(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetDevTPT(int timeOut, int fpSaveNum) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetDevTPT(timeOut, fpSaveNum);
    }
  }

  @ReactMethod
  public void fpiGetFeature(int timeOut) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetFeature(timeOut);
    }
  }

  @ReactMethod
  public void fpiGetTemplate(int timeOut) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetTemplate(timeOut);
    }
  }

  @ReactMethod
  public void fpiCheckFinger() {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiCheckFinger();
    }
  }

  @ReactMethod
  public void fpiGetTPTCnt() {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiGetTPTCnt();
    }
  }

  @ReactMethod
  public void fpiDeleteTPT(int fpDelNum) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiDeleteTPT(fpDelNum);
    }
  }

  @ReactMethod
  public void fpiSetBtName(String btName) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetBtName(btName);
    }
  }

  @ReactMethod
  public void fpiSetSleepTime(int sleepTime) {
    if (mCGfpInterface != null) {
      mCGfpInterface.fpiSetSleepTime(sleepTime);
    }
  }

  @ReactMethod
  public void fpiDownVerify(String template, String feature) {
    if (mCGfpInterface != null) {
      byte[] tpt = Base64.decode(template, Base64.DEFAULT);
      byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
      mCGfpInterface.fpiDownVerify(tpt, ftr);
    }
  }

  @ReactMethod
  public void sysOneMatch(String template, String feature, Callback cb) {
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
      switch(msg.what) {
        case 0xA0:  // Error message
        {
          int error = msg.getData().getInt("FPIGetError");
          WritableMap map = Arguments.createMap();
          map.putInt("error", error);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xA1: // public int fpiConnectBT(String strBTName)
        {
          int btStatus = msg.getData().getInt("FPIBTStatus");
          WritableMap map = Arguments.createMap();
          map.putInt("btStatus", btStatus);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xA4:  // public void fpiGetFeature (int iTimeOut)
        {
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenFTR", bytesLenFTR);
          if (bytesLenFTR > 0) {
            map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xA5:  // public void fpiGetTemplate (int iTimeOut)
        {
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenTPT", bytesLenTPT);
          if (bytesLenTPT > 0) {
            map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB0:  // public void fpiGetVersion()
        {
          WritableMap map = Arguments.createMap();
          map.putString("devVer", msg.getData().getString("FPIGetDevVer"));
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB1:  // public void fpiGetDevSN()
        {
          WritableMap map = Arguments.createMap();
          map.putString("devSN", msg.getData().getString("FPIGetDevSN"));
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB2:  // public void fpiSetDevSN(String strSN)
        {
          WritableMap map = Arguments.createMap();
          map.putString("devSN", msg.getData().getString("FPISetDevSN"));
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB3:  // public void fpiGetImage(int iTimeOut)
        {
          byte[] fpImage = (byte[]) msg.obj;
          int byteLenIMG = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("byteLenIMG", byteLenIMG);
          if (byteLenIMG > 0) {
            map.putString("fpImage", Base64.encodeToString(fpImage, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB4:  // public void fpiGetDevFTR(int iTimeOut)
        {
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenFTR", bytesLenFTR);
          if (bytesLenFTR > 0) {
            map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB5:  // public void fpiGetDevTPT(int iTimeOut, int iFpSaveNum)
        {
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          WritableMap map = Arguments.createMap();
          map.putInt("bytesLenTPT", bytesLenTPT);
          if (bytesLenTPT > 0) {
            map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
          }
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB6: // public void fpiDownVerify(byte[] byteArryTPT, byte[] byteArryFTR)
        {
          int downVerify = msg.getData().getInt("FPIDownVerify");
          WritableMap map = Arguments.createMap();
          map.putInt("downVerify", downVerify);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB7:  // public void fpiSearchMatch(int iTimeOut)
        {
          int searchMatch = msg.getData().getInt("FPISearchMatch");
          WritableMap map = Arguments.createMap();
          map.putInt("searchMatch", searchMatch);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB8:  // public void fpiCheckFinger()
        {
          int checkFinger = msg.getData().getInt("FPICheckFinger");
          WritableMap map = Arguments.createMap();
          map.putInt("checkFinger", checkFinger);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xB9:  // EncryptTest --Reserved
          break;
        case 0xBA:  // DecryptTest --Reserved   String.valueOf(i),  Integer.toString(i)
          break;
        case 0xBB: // public void fpiGetTPTCnt()
        {
          int fpTPTCount = msg.getData().getInt("FPIGetTPTCnt");
          WritableMap map = Arguments.createMap();
          map.putInt("fpTPTCount", fpTPTCount);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xBC: // public void fpiDeleteTPT(int iFpDelNum)
        {
          int deleteTPT = msg.getData().getInt("FPIDeleteTPT");
          WritableMap map = Arguments.createMap();
          map.putInt("deleteTPT", deleteTPT);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xBD: {
          int setBtName = msg.getData().getInt("FPISetBtName");
          WritableMap map = Arguments.createMap();
          map.putInt("setBtName", setBtName);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
        case 0xBE: {
          int setSleepTime = msg.getData().getInt("FPISetSleepTime");
          WritableMap map = Arguments.createMap();
          map.putInt("setSleepTime", setSleepTime);
          map.putInt("type", msg.what);
          sendEvent("event", map);
          break;
        }
      }
    }
  };
}
