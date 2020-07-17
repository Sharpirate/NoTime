package com.sharpirate.notime;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sharpirate.notime.tools.AdHandler;

public class AndroidLauncher extends AndroidApplication implements AdHandler {

	// admob
	InterstitialAd interstitial;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// initialize config
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		initialize(new Main(this), config);

		// initialize ad networks
		initChartboost();
		initAdmob();
	}

	@Override
	public void onStart() {
		super.onStart();
		Chartboost.onStart(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Chartboost.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		Chartboost.onPause(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Chartboost.onStop(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Chartboost.onDestroy(this);
	}

	@Override
	public void onBackPressed() {
		// If an interstitial is on screen, close it.
		if(!Chartboost.onBackPressed())
			super.onBackPressed();
	}

	@Override
	public void loadInterstitial() {

		Gdx.app.log("TAG", "LOAD INTERSTITIAL");
		if(!Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT))
			Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
	}

	@Override
	public void showInterstitial() {
		Gdx.app.log("TAG", "SHOW INTERSTITIAL");

		if(Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT))
			Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
		else
			showAdmobInterstitial();
	}

	private void loadAdmobInterstitial() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!interstitial.isLoaded())
					requestAdmobInterstitial();
			}
		});
	}

	private void showAdmobInterstitial() {
		Gdx.app.log("TAG", "SHOW ADMOB");

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(interstitial.isLoaded())
					interstitial.show();
			}
		});
	}

	private void requestAdmobInterstitial() {
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial.loadAd(adRequest);
	}

	private void initChartboost() {
		Chartboost.startWithAppId(this, getResources().getString(R.string.app_id), getResources().getString(R.string.app_signature));
		Chartboost.setDelegate(createChartboostDelegate());
		Chartboost.onCreate(this);
	}

	private ChartboostDelegate createChartboostDelegate() {
		return new ChartboostDelegate() {
			@Override
			public void didFailToLoadInterstitial(String location, CBError.CBImpressionError error) {
				super.didFailToLoadInterstitial(location, error);

				// if chartboost interstitial fails, load admob
				loadAdmobInterstitial();
			}
		};
	}

	private void initAdmob() {
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(getResources().getString(R.string.interstitial_id));
	}
}
