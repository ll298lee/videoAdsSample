package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mopub.common.util.Views;
import com.taiwanmobile.pt.adp.view.TWMAd;
import com.taiwanmobile.pt.adp.view.TWMAdRequest;
import com.taiwanmobile.pt.adp.view.TWMAdSize;
import com.taiwanmobile.pt.adp.view.TWMAdView;
import com.taiwanmobile.pt.adp.view.TWMAdViewListener;

import java.util.Map;

/**
 * Created by ll298lee on 7/23/17.
 */

public class TAMediaMopubBanner extends CustomEventBanner {
    private static final String TAG = "TAMediaMopubBanner";

    private TWMAdView adView = null;

    @Override
    protected void loadBanner(Context context, final CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        Log.d(TAG, "loadBanner(" + serverExtras.get("adUnitId") + ") invoked!!");

        String adUnitId = serverExtras.get("adUnitId"); //後台設定的adUnitId會透過此Key值取得
        adView = new TWMAdView( (Activity)context, TWMAdSize.SMART_BANNER, adUnitId );
        adView.setAdListener(new TWMAdViewListener(){
            @Override
            public void onReceiveAd(TWMAd ad) {
                customEventBannerListener.onBannerLoaded(adView);
            }

            @Override
            public void onFailedToReceiveAd(TWMAd ad, TWMAdRequest.ErrorCode errorCode) {
                customEventBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }

            @Override
            public void onPresentScreen(TWMAd ad) {
                customEventBannerListener.onBannerClicked();
            }

            @Override
            public void onDismissScreen(TWMAd ad) {}

            @Override
            public void onLeaveApplication(TWMAd ad) {
                customEventBannerListener.onLeaveApplication();
            }

        });
        adView.loadAd(new TWMAdRequest());

    }

    @Override
    protected void onInvalidate() {
        // Called when MoPubView is being invalidated or destroyed
        Log.e(TAG, "onInvalidate invoke !!");
        Views.removeFromParent(adView);
        if (adView != null) {
            adView.destroy();
        }
    }
}