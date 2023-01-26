package com.devinjapan.aisocialmediaposter.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.devinjapan.aisocialmediaposter.BuildConfig
import com.devinjapan.aisocialmediaposter.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@SuppressLint("VisibleForTests")
@Composable
fun BannerAd(context: Context, modifier: Modifier = Modifier, adUnitId: String) {
    // on below line creating a variable for location.
    // on below line creating a column for our maps.
    Column(
        modifier = modifier
            .wrapContentSize()
            .background(Color.White)
    ) {
        AndroidView(
            // on below line specifying width for ads.
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                // on below line specifying ad view.
                val adView = AdView(context)

                AdView(context).apply {
                    // on below line specifying ad size
                    setAdSize(AdSize.BANNER)
                    // on below line specifying ad unit id
                    // currently added a test ad unit id.
                    setAdUnitId(
                        if (BuildConfig.DEBUG) {
                            "ca-app-pub-3940256099942544/6300978111"
                        } else {
                            adUnitId
                        }
                    )
                    // calling load ad to load our ad.
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

var mInterstitialAd: InterstitialAd? = null

@SuppressLint("VisibleForTests")
fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/8691691433"
        } else {
            context.getString(R.string.ads_interstital_share_screen_start_over)
        },
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        }
    )
}

fun showInterstitial(context: Context, onAdDismissed: () -> Unit) {
    val activity = context.findActivity()

    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(e: AdError) {
                mInterstitialAd = null
            }

            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null

                loadInterstitial(context)
                onAdDismissed()
            }
        }
        mInterstitialAd?.show(activity)
    }
}

fun removeInterstitial() {
    mInterstitialAd?.fullScreenContentCallback = null
    mInterstitialAd = null
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
