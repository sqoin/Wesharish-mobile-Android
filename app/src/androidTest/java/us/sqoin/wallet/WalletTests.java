package us.sqoin.wallet;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import us.com.breadwallet.BreadApp;
import us.sqoin.core.BRCoreMasterPubKey;
import us.com.breadwallet.presenter.activities.settings.TestActivity;
import us.com.breadwallet.presenter.entities.CurrencyEntity;
import us.com.breadwallet.presenter.entities.CryptoRequest;
import us.com.breadwallet.repository.RatesRepository;
import us.sqoin.tools.manager.BRSharedPrefs;
import us.com.breadwallet.tools.security.BRKeyStore;
import us.com.breadwallet.tools.threads.executor.BRExecutor;
import us.com.breadwallet.wallet.abstracts.BaseWalletManager;
import us.com.breadwallet.wallet.wallets.bitcoin.WalletBchManager;
import us.com.breadwallet.wallet.util.CryptoUriParser;
import us.com.breadwallet.tools.util.BRConstants;
import us.com.breadwallet.wallet.wallets.bitcoin.WalletBitcoinManager;
import us.com.breadwallet.wallet.wallets.ethereum.WalletEthManager;
import us.com.breadwallet.wallet.wallets.ethereum.WalletTokenManager;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on 4/29/16.
 * Copyright (c) 2016 sqoin llc <mihail@sqoin.com>
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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class WalletTests {
    public static final String TAG = WalletTests.class.getName();

    @Rule
    public ActivityTestRule<TestActivity> mActivityRule = new ActivityTestRule<>(TestActivity.class);


    // Test Wallets
    BaseWalletManager mBtcWallet;
    BaseWalletManager mBchWallet;

    @Before
    public void setUp() {
        Log.e(TAG, "setUp: ");
        BRCoreMasterPubKey pubKey = new BRCoreMasterPubKey("cat circle quick rotate arena primary walnut mask record smile violin state".getBytes(), true);
        BRKeyStore.putMasterPublicKey(pubKey.serialize(), mActivityRule.getActivity());
        mBtcWallet = WalletBitcoinManager.getInstance(mActivityRule.getActivity());
        mBchWallet = WalletBchManager.getInstance(mActivityRule.getActivity());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void paymentRequestTest() {
        Activity app = mActivityRule.getActivity();
        CryptoRequest obj = CryptoUriParser.parseRequest(app, "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals("n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi", obj.getAddress());

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=1");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        BigDecimal bigDecimal = obj.getAmount();
        long amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "100000000");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=0.00000001");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.getAmount();
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "1");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=21000000");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.getAmount();
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "2100000000000000");

        // test for floating point rounding issues, these values cannot be exactly represented with an IEEE 754 double
        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=20999999.99999999");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.getAmount();
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "2099999999999999");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=20999999.99999995");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.getAmount();
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "2099999999999995");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=0.07433");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        bigDecimal = obj.getAmount();
        amountAsLong = bigDecimal.longValue();
        assertEquals(String.valueOf(amountAsLong), "7433000");

        // invalid amount string
        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?amount=foobar");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.getAmount(), null);

        // test correct encoding of '&' in argument value
        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?label=foo%26bar");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.getLabel(), "foo");

        // test handling of ' ' in label or message
        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?label=foo bar&message=bar foo");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.getLabel(), "foo bar");
        assertEquals(obj.getMessage(), "bar foo");

        // test bip73
        obj = CryptoUriParser.parseRequest(app, "bitcoin:n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi?r=https://foobar.com");
        assertEquals(obj.getAddress(), "n2eMqTT929pb1RDNuqEnxdaLau1rxy3efi");
        assertEquals(obj.getRUrl(), "https://foobar.com");

        obj = CryptoUriParser.parseRequest(app, "bitcoin:?r=https://foobar.com");
        assertEquals(obj.getAddress(), "");
        assertEquals(obj.getRUrl(), "https://foobar.com");

        obj = CryptoUriParser.parseRequest(app, "0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");
        assertEquals(obj.getAddress(), "0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");

        obj = CryptoUriParser.parseRequest(app, "ethereum:0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");
        assertEquals(obj.getAddress(), "0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");

        obj = CryptoUriParser.parseRequest(app, "ethereum://0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");
        assertEquals(obj.getAddress(), "0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");

        obj = CryptoUriParser.parseRequest(app, "ethereum://0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F?value=1");
        assertEquals(obj.getAddress(), "0x8CCF9C4a7674D5784831b5E1237d9eC9Dddf9d7F");
        assertEquals(obj.getAmount(), new BigDecimal("1000000000000000000"));
    }

    @Test
    public void currencyManagerTests() {

    }

    @Test
    public void walletCurrencyConversionTests() {
        Activity app = mActivityRule.getActivity();

        WalletBitcoinManager btcWallet = WalletBitcoinManager.getInstance(app);

        BRSharedPrefs.putPreferredFiatIso(app, "USD");

        int btcRate = 12000;
        float ethRate = 0.05f;
        float brdRate = 0.00005f;

        Set<CurrencyEntity> tmp = new HashSet<>();

        tmp.add(new CurrencyEntity("USD", "Dollar", btcRate, "WSC"));
        tmp.add(new CurrencyEntity("WSC", "Bitcoin", ethRate, "ETH"));
        tmp.add(new CurrencyEntity("WSC", "Bitcoin", brdRate, "BRD"));
        RatesRepository.getInstance(app).putCurrencyRates(tmp);

        BRSharedPrefs.putCryptoDenomination(app, "BTC", BRConstants.CURRENT_UNIT_BITCOINS);

        /**TEST BTC*/

        //getCryptoForSmallestCrypto(..)
        BigDecimal val = new BigDecimal(20000);
        BigDecimal res = btcWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertEquals(res.doubleValue(), new BigDecimal(0.0002).doubleValue(), 0.000000001);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(0.5);
        res = btcWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertEquals(res.longValue(), 50000000, 0);

        //getFiatForSmallestCrypto(..)
        val = btcWallet.getSmallestCryptoForCrypto(app, new BigDecimal(0.5));
        res = btcWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), btcRate / 2, 0); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = btcWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(50000000)) == 0); //dollars

        //getCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = btcWallet.getCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 0.5, 0); //dollars


        BRSharedPrefs.putCryptoDenomination(app, "BTC", BRConstants.CURRENT_UNIT_BITS);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal(20000);
        res = btcWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertEquals(res.doubleValue(), new BigDecimal(200).doubleValue(), 0.000000001);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(200);
        res = btcWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertEquals(res.longValue(), 20000, 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal(50000000);
        res = btcWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), btcRate / 2, 0); // dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = btcWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 50000000, 0); //dollars

        //getCryptoForFiat(..)
        val = new BigDecimal(6000);//$6000.00 = c600000
        res = btcWallet.getCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(500000)) == 0); //dollars

        /**TEST ETH*/

        WalletEthManager ethWallet = WalletEthManager.getInstance(app.getApplicationContext());
        Assert.assertNotNull(ethWallet);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal("25000000000000000000");
        res = ethWallet.getCryptoForSmallestCrypto(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(25)) == 0);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(25);
        res = ethWallet.getSmallestCryptoForCrypto(app, val);
        Assert.assertTrue(res.toPlainString().compareTo(new BigDecimal("25000000000000000000").toPlainString()) == 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal("25000000000000000000");
        res = ethWallet.getFiatForSmallestCrypto(app, val, null);
        Assert.assertEquals(res.doubleValue(), btcRate * ethRate * 25, 0.001); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(ethRate).multiply(new BigDecimal(3)).multiply(new BigDecimal(btcRate));//
        res = ethWallet.getSmallestCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal("3000000000000000000")) == 0);

        //getCryptoForFiat(..)
        val = new BigDecimal(ethRate).multiply(new BigDecimal(3)).multiply(new BigDecimal(btcRate));
        res = ethWallet.getCryptoForFiat(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal("3")) == 0);

        /**TEST erc20*/

        WalletTokenManager tokenManager = WalletTokenManager.getBrdWallet(ethWallet);
        Assert.assertNotNull(tokenManager);

        //getCryptoForSmallestCrypto(..)
        val = new BigDecimal("25000000000000000000");
        res = tokenManager.getCryptoForSmallestCrypto(app, val);
        Assert.assertTrue(res.compareTo(new BigDecimal(25)) == 0);

        //getSmallestCryptoForCrypto(..)
        val = new BigDecimal(25);
        res = tokenManager.getSmallestCryptoForCrypto(app, val);
        Assert.assertTrue(res.toPlainString().compareTo(new BigDecimal("25000000000000000000").toPlainString()) == 0);

        //getFiatForSmallestCrypto(..)
        val = new BigDecimal("2000000000000000000");
        res = tokenManager.getFiatForSmallestCrypto(app, val, null).setScale(8, BRConstants.ROUNDING_MODE);
        Assert.assertEquals(res.doubleValue(), new BigDecimal(btcRate * brdRate * 2).setScale(8, BRConstants.ROUNDING_MODE).doubleValue(), 0.0000001); //dollars

        //getSmallestCryptoForFiat(..)
        val = new BigDecimal(3);//
        res = tokenManager.getSmallestCryptoForFiat(app, val).setScale(0);
        Assert.assertEquals(res.toPlainString(), "5000000130000000000");

        //getCryptoForFiat(..)
        val = new BigDecimal(3);//
        res = tokenManager.getCryptoForFiat(app, val);
        Assert.assertEquals(res.doubleValue(), 5, 0.00001);

    }

}
