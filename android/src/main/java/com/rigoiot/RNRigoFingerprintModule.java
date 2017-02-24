
package com.rigoiot;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.os.Looper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;

import com.wellcom.verify.GfpInterface;

public class RNRigoFingerprintModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private Callback mCb;
  private GfpInterface  mCGfpInterface;

  public RNRigoFingerprintModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNRigoFingerprint";
  }

  @ReactMethod
  public void initialize(Callback cb) {
    mCb = cb;
    mCGfpInterface = new GfpInterface(reactContext, mFpHandler);
  }

  @ReactMethod
  public void destroy() {
    mCb = null;
    if (mCGfpInterface != null) {
      mCGfpInterface.sysExit();
      mCGfpInterface = null;
    }
  }

  @ReactMethod
  public void fpiOpenBT(Callback cb) {
    boolean ret = mCGfpInterface.fpiOpenBT();
    if (cb != null) {
      cb.invoke(ret);
    }
  }

  @ReactMethod
  public void sysCloseBT() {
    mCGfpInterface.sysCloseBT();
  }

  @ReactMethod
  public void fpiConnectBT(String btName, Callback cb) {
    int ret = mCGfpInterface.fpiConnectBT(btName);
    if (cb != null) {
      cb.invoke(ret);
    }
  }

  @ReactMethod
  public void fpiDisconnectBT() {
    mCGfpInterface.fpiDisconnectBT();
  }

  @ReactMethod
  public void fpiCheckBTOpened(Callback cb) {
    boolean ret = mCGfpInterface.sysCheckBTOpened();
    if (cb != null) {
      cb.invoke(ret);
    }
  }

  @ReactMethod
  public void fpiCheckBTConnected(Callback cb) {
    boolean ret = mCGfpInterface.sysCheckBTConnected();
    if (cb != null) {
      cb.invoke(ret);
    }
  }

  @ReactMethod
  public void fpiCheckUSBConnected(Callback cb) {
    boolean ret = mCGfpInterface.sysCheckUSBConnected();
    if (cb != null) {
      cb.invoke(ret);
    }
  }

  @ReactMethod
  public void fpiGetVersion() {
    mCGfpInterface.fpiGetVersion();
  }

  @ReactMethod
  public void fpiGetDevSN() {
    mCGfpInterface.fpiGetDevSN();
  }

  @ReactMethod
  public void fpiSetDevSN(String sn) {
    mCGfpInterface.fpiSetDevSN(sn);
  }

  @ReactMethod
  public void fpiGetImage(int timeOut) {
    mCGfpInterface.fpiGetImage(timeOut);
  }

  @ReactMethod
  public void fpiGetDevFTR(int timeOut) {
    mCGfpInterface.fpiGetDevFTR(timeOut);
  }

  @ReactMethod
  public void fpiGetDevTPT(int timeOut, int fpSaveNum) {
    mCGfpInterface.fpiGetDevTPT(timeOut, fpSaveNum);
  }

  @ReactMethod
  public void fpiGetFeature(int timeOut) {
    mCGfpInterface.fpiGetFeature(timeOut);
  }

  @ReactMethod
  public void fpiGetTemplate(int timeOut) {
    mCGfpInterface.fpiGetTemplate(timeOut);
  }

  @ReactMethod
  public void fpiCheckFinger() {
    mCGfpInterface.fpiCheckFinger();
  }

  @ReactMethod
  public void fpiGetTPTCnt() {
    mCGfpInterface.fpiGetTPTCnt();
  }

  @ReactMethod
  public void fpiDeleteTPT(int fpDelNum) {
    mCGfpInterface.fpiDeleteTPT(fpDelNum);
  }

  @ReactMethod
  public void fpiSetBtName(String btName) {
    mCGfpInterface.fpiSetBtName(btName);
  }

  @ReactMethod
  public void fpiSetSleepTime(int sleepTime) {
    mCGfpInterface.fpiSetSleepTime(sleepTime);
  }

  @ReactMethod
  public void fpiDownVerify(String template, String feature) {
    byte[] tpt = Base64.decode(template, Base64.DEFAULT);
    byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
    mCGfpInterface.fpiDownVerify(tpt, ftr);
  }

  @ReactMethod
  public void sysOneMatch(String template, String feature) {
    byte[] tpt = Base64.decode(template, Base64.DEFAULT);
    byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
    mCGfpInterface.sysOneMatch(tpt, ftr);
  }

  @ReactMethod
  public void sysSearchMatch(int templateCount, String template, String feature, int[] templateID) {
    byte[] tpt = Base64.decode(template, Base64.DEFAULT);
    byte[] ftr = Base64.decode(feature, Base64.DEFAULT);
    mCGfpInterface.sysSearchMatch(templateCount, tpt, ftr, templateID);
  }

  // 蓝牙异步消息处理
  Handler mFpHandler = new Handler(Looper.getMainLooper()) {
    public void handleMessage(Message msg){
      super.handleMessage(msg);
      switch(msg.what) {
        case 0xA0:  // Error message
          int error = msg.getData().getInt("FPIGetError");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("error", error);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xA1: // public int fpiConnectBT(String strBTName)
          int btStatus = msg.getData().getInt("FPIBTStatus");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("btStatus", btStatus);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xA4:  // public void fpiGetFeature (int iTimeOut)
        {
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("bytesLenFTR", bytesLenFTR);
            if (bytesLenFTR > 0) {
              map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
            }
            mCb.invoke(msg.what, map);
          }
          break;
        }
        case 0xA5:  // public void fpiGetTemplate (int iTimeOut)
        {
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("bytesLenTPT", bytesLenTPT);
            if (bytesLenTPT > 0) {
              map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
            }
            mCb.invoke(msg.what, map);
          }
          break;
        }
        case 0xB0:  // public void fpiGetVersion()
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putString("devVer", msg.getData().getString("FPIGetDevVer"));
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB1:  // public void fpiGetDevSN()
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putString("devSN", msg.getData().getString("FPIGetDevSN"));
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB2:  // public void fpiSetDevSN(String strSN)
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putString("devSN", msg.getData().getString("FPISetDevSN"));
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB3:  // public void fpiGetImage(int iTimeOut)
          byte[] fpImage = (byte[]) msg.obj;
          int byteLenIMG = msg.arg1;
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("byteLenIMG", byteLenIMG);
            if (byteLenIMG > 0) {
              map.putString("fpImage", Base64.encodeToString(fpImage, Base64.DEFAULT));
            }
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB4:  // public void fpiGetDevFTR(int iTimeOut)
        {
          byte[] fpFeature = (byte[]) msg.obj;
          int bytesLenFTR = msg.arg1;
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("bytesLenFTR", bytesLenFTR);
            if (bytesLenFTR > 0) {
              map.putString("fpFeature", Base64.encodeToString(fpFeature, Base64.DEFAULT));
            }
            mCb.invoke(msg.what, map);
          }
          break;
        }
        case 0xB5:  // public void fpiGetDevTPT(int iTimeOut, int iFpSaveNum)
        {
          byte[] fpTemplate = (byte[]) msg.obj;
          int bytesLenTPT = msg.arg1;
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("bytesLenTPT", bytesLenTPT);
            if (bytesLenTPT > 0) {
              map.putString("fpTemplate", Base64.encodeToString(fpTemplate, Base64.DEFAULT));
            }
            mCb.invoke(msg.what, map);
          }
          break;
        }
        case 0xB6: // public void fpiDownVerify(byte[] byteArryTPT, byte[] byteArryFTR)
          int downVerify = msg.getData().getInt("FPIDownVerify");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("downVerify", downVerify);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB7:  // public void fpiSearchMatch(int iTimeOut)
          int searchMatch = msg.getData().getInt("FPISearchMatch");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("searchMatch", searchMatch);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB8:  // public void fpiCheckFinger()
          int checkFinger = msg.getData().getInt("FPICheckFinger");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("checkFinger", checkFinger);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xB9:  // EncryptTest --Reserved
          break;
        case 0xBA:  // DecryptTest --Reserved   String.valueOf(i),  Integer.toString(i)
          break;
        case 0xBB: // public void fpiGetTPTCnt()
          int fpTPTCount = msg.getData().getInt("FPIGetTPTCnt");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("fpTPTCount", fpTPTCount);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xBC: // public void fpiDeleteTPT(int iFpDelNum)
          int deleteTPT = msg.getData().getInt("FPIDeleteTPT");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("deleteTPT", deleteTPT);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xBD:
          int setBtName = msg.getData().getInt("FPISetBtName");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("setBtName", setBtName);
            mCb.invoke(msg.what, map);
          }
          break;
        case 0xBE:
          int setSleepTime = msg.getData().getInt("FPISetSleepTime");
          if (mCb != null) {
            WritableMap map = Arguments.createMap();
            map.putInt("setSleepTime", setSleepTime);
            mCb.invoke(msg.what, map);
          }
          break;
      }
    }
  };
}
