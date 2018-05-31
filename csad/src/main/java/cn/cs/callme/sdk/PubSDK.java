package cn.cs.callme.sdk;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class PubSDK {
    private ConnectionQueue connectionQueue;
    private Context context;
    private static class CsAdSDKHolder {
        private final static PubSDK INSTANCE = new PubSDK();
    }

    private PubSDK() {
        connectionQueue = new ConnectionQueue();
    }

    /**
     * @return
     */
    public static PubSDK getInstance() {
        return CsAdSDKHolder.INSTANCE;
    }

    /**
     * @param context
     * @param appKey
     * @return
     */
    public synchronized void init(Context context, String appKey) {
        if (context == null) {
            throw new IllegalArgumentException("valid context is required");
        }
        this.context = context;
        connectionQueue.setAppKey_(appKey);
        //
        connectionQueue.loadFloatFlag();
        connectionQueue.loadTBCode();
    }

    /**
     * @param context
     */
    public synchronized void init(Context context) {
        String appId = context.getPackageName();
        init(context, appId);
    }

    public synchronized void initTBCode() {
        if (!isShowFloatIcon()) {
            return;
        }
        try {
            TBCode code = connectionQueue.getTbCodeFuture().get(5, TimeUnit.SECONDS);
            ClipData clip = ClipData.newPlainText("", code.getCommand());
            ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public synchronized boolean isShowFloatIcon() {
        try {
            Boolean res = connectionQueue.getFlagProcessorFuture().get(5, TimeUnit.SECONDS);
            return res.booleanValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw new RuntimeException("please init sdk in Application");
        }
        return false;
    }
}