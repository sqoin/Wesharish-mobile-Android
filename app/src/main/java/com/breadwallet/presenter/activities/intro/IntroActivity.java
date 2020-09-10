/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan <mihail@breadwallet.com> on 8/4/15.
 * Copyright (c) 2016 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.breadwallet.presenter.activities.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.breadwallet.BuildConfig;
import com.breadwallet.R;
import com.breadwallet.presenter.activities.HomeActivity;
import com.breadwallet.presenter.activities.InputPinActivity;
import com.breadwallet.presenter.activities.PaperKeyActivity;
import com.breadwallet.presenter.activities.util.BRActivity;
import com.breadwallet.presenter.customviews.BRButton;
import com.breadwallet.tools.animation.UiUtils;
import com.breadwallet.tools.security.BRKeyStore;
import com.breadwallet.tools.security.PostAuth;
import com.breadwallet.tools.threads.executor.BRExecutor;
import com.breadwallet.tools.util.EventUtils;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.tools.util.Utils;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;
import com.platform.APIClient;

/**
 * Activity shown when there is no wallet, here the user can pick between creating new wallet or recovering one with
 * the paper key.
 */
public class IntroActivity extends BRActivity {

    private View mLoadingView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingView = findViewById(R.id.loading_view);
        setContentView(R.layout.activity_intro);
        setOnClickListeners();
        //progressToBrowse();
        if (BuildConfig.DEBUG) {
            Utils.printPhoneSpecs(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventUtils.pushEvent(EventUtils.EVENT_LANDING_PAGE_APPEARED);
    }

    private void setOnClickListeners() {
        BRButton buttonNewWallet = findViewById(R.id.button_new_wallet);
        BRButton buttonRecoverWallet = findViewById(R.id.button_recover_wallet);
        ImageButton faq = findViewById(R.id.faq_button);
        buttonNewWallet.setOnClickListener(v -> {
            if (!UiUtils.isClickAllowed()) {
                return;
            }
            EventUtils.pushEvent(EventUtils.EVENT_LANDING_PAGE_GET_STARTED);
            //Intent intent = new Intent(IntroActivity.this, OnBoardingActivity.class);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            //startActivity(intent);
            progressToBrowse();
        });

        buttonRecoverWallet.setOnClickListener(v -> {
            if (!UiUtils.isClickAllowed()) {
                return;
            }
            EventUtils.pushEvent(EventUtils.EVENT_LANDING_PAGE_RESTORE_WALLET);
            Intent intent = new Intent(IntroActivity.this, RecoverActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });
        faq.setOnClickListener(v -> {
            if (!UiUtils.isClickAllowed()) return;
            BaseWalletManager wm = WalletsMaster.getInstance().getCurrentWallet(IntroActivity.this);
            UiUtils.showSupportFragment(IntroActivity.this, BRConstants.FAQ_START_VIEW, wm);
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return checkOverlayAndDispatchTouchEvent(event);
    }

    public void progressToBrowse() {
        //EventUtils.pushEvent(EventUtils.EVENT_FINAL_PAGE_BROWSE_FIRST);
        if (BRKeyStore.getPinCode(this).length() > 0) {
            //UiUtils.startBreadActivity(this, true);
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        } else {
            setupPin();
        }
    }

    public void setupPin() {
        //mLoadingView.setVisibility(View.VISIBLE);
        BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(() -> {
            PostAuth.getInstance().onCreateWalletAuth(this, false, () -> {
                APIClient.getInstance(IntroActivity.this).updatePlatform();
                Intent intent = new Intent(IntroActivity.this, InputPinActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                /*if (nextScreen == OnBoardingActivity.NextScreen.BUY_SCREEN) {
                    intent.putExtra(InputPinActivity.EXTRA_PIN_NEXT_SCREEN, PaperKeyActivity.DoneAction.SHOW_BUY_SCREEN.name());
                }*/
                intent.putExtra(InputPinActivity.EXTRA_PIN_IS_ONBOARDING, true);
                //IntroActivity.this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                IntroActivity.this.startActivity(intent);
            });
        });
    }
}
