package com.example.hofa.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader



class RewardedAdManager(
    private val context: Context
) {
    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardCallback: (() -> Unit)? = null

    init {
        initializeRewardedAd()
    }

    private fun initializeRewardedAd() {
        rewardedAdLoader = RewardedAdLoader(context).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdFailedToLoad(error: AdRequestError) {
                    // Handle ad load failure
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })
        }
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder("R-M-14132592-1").build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }

    fun showAd(callback: () -> Unit) {
        rewardCallback = callback
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onAdShown() {
                    Log.d("RewardedAdManager", "Ad shown")
                }

                override fun onRewarded(reward: Reward) {
                    rewardCallback?.invoke()
                }

                override fun onAdFailedToShow(adError: com.yandex.mobile.ads.common.AdError) {
                    Log.e("RewardedAdManager", "Ad failed to show: ${adError.description}")
                    cleanUp()
                    loadRewardedAd()
                    rewardCallback?.invoke()
                }

                override fun onAdDismissed() {
                    Log.d("RewardedAdManager", "Ad dismissed")
                    cleanUp()
                    loadRewardedAd()
                    rewardCallback?.invoke()
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdImpression(impressionData: com.yandex.mobile.ads.common.ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            show(context as Activity)
        }
    }

    private fun cleanUp() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
        rewardCallback = null
    }

    fun destroy() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        cleanUp()
    }
}