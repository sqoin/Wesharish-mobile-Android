/*
 * EthereumTransaction
 *
 * Created by Ed Gamble <ed@breadwallet.com> on 3/20/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.core.ethereum;

import java.math.BigDecimal;

/**
 *
 */
public class BREthereumTransfer extends BREthereumEWM.ReferenceWithDefaultUnit {

    /**
     *
     * @param ewm
     * @param identifier
     * @param unit  The transaction's unit; should be identical with that unit used to create
     *              the transaction identifier.
     */
    protected BREthereumTransfer(BREthereumEWM ewm, long identifier, BREthereumAmount.Unit unit) {
        super(ewm, identifier, unit);
    }

    public boolean isConfirmed () {
        return ewm.get().jniTransactionIsConfirmed(identifier);
    }

    public boolean isSubmitted () {
        return ewm.get().jniTransactionIsSubmitted(identifier);
    }

    public boolean isErrored () {
        return ewm.get().jniTransactionIsErrored(identifier);
    }

    public String getSourceAddress () {
        return ewm.get().jniTransactionSourceAddress(identifier);
    }

    public String getTargetAddress () {
        return ewm.get().jniTransactionTargetAddress(identifier);
    }

    public String getIdentifier () {
        return ewm.get().jniTransactionGetIdentifier(identifier);
    }

    public String getOriginationTransactionHash() {
        return ewm.get().jniTransactionOriginatingTransactionHash(identifier);
    }
    
    //
    // Amount
    //
    public String getAmount () {
        return getAmount(defaultUnit);
    }

    public String getAmount(BREthereumAmount.Unit unit) {
        validUnitOrException(unit);
        return ewm.get().jniTransactionGetAmount(identifier, unit.jniValue);
    }

    /**
     * Convert the amount to fiat.
     *
     * As an (typical) example - assume the conversion is 600 $/ETH. `fiatPerCryto` would be
     * 600.0 and `unitForCrypto` would be Unit.ETHER_ETHER. Assume the amount was 1.2 ETH.  The
     * return will be $720.0
     *
     * @param fiatPerCrypto
     * @param unitForFiatPerCrypto
     * @return
     */
    public double getAmountInFiat (double fiatPerCrypto,
                                   BREthereumAmount.Unit unitForFiatPerCrypto) {
        return fiatPerCrypto * Double.parseDouble(getAmount(unitForFiatPerCrypto));
    }

    /**
     * See `double getAmountInFiat (double, Unit)`
     *
     */
    public BigDecimal getAmountInFiat (BigDecimal fiatPerCrypto,
                                       BREthereumAmount.Unit unitForFiatPerCrypto) {
        return fiatPerCrypto.multiply(new BigDecimal(getAmount(unitForFiatPerCrypto)));
    }

    //
    // Fee
    //

    /**
     * The fee in GWEI
     *
     * @return in GWEI
     */
    public String getFee () {
        return getFee(BREthereumAmount.Unit.ETHER_GWEI);
    }

    /**
     * The fee in `unit`
     *
     * @param unit must be an ether unit, otherwise fatal()
     * @return in `unit`
     */
    public String getFee (BREthereumAmount.Unit unit) {
        assert (!unit.isTokenUnit());
        return ewm.get().jniTransactionGetFee(identifier, unit.jniValue);
    }

    /**
     * Convert the fee to fiat.
     *
     * As an (typical) example - assume the conversion is 600 $/ETH. `fiatPerCryto` would be
     * 600.0 and `unitForCrypto` would be Unit.ETHER_ETHER. Assume the fee was 42000 GWEI (as
     * 21000 gas * 2 GWEI/gas).
     *
     * @param fiatPerCrypto
     * @param unitForFiatPerCrypto
     * @return
     */
    public double getFeeInFiat (double fiatPerCrypto,
                                BREthereumAmount.Unit unitForFiatPerCrypto) {
        return fiatPerCrypto * Double.parseDouble(getFee(unitForFiatPerCrypto));
    }

    /**
     * See `double getFeeInFiat (double, Unit)`
     *
     */
    public BigDecimal getFeeInFiat (BigDecimal fiatPerCrypto,
                                    BREthereumAmount.Unit unitForFiatPerCrypto) {
        return fiatPerCrypto.multiply(new BigDecimal(getFee(unitForFiatPerCrypto)));
    }

    //
    // Gas Price, Limit, Used
    //

    /**
     * The gasPrise in GWEI
     *
     * @return in GWEI
     */
    public String getGasPrice () {
        return getGasPrice(BREthereumAmount.Unit.ETHER_GWEI);
    }

    /**
     * The gasPrice in `unit`
     *
     * @param unit unit must be an ether unit, otherwise fatal()
     * @return in `unit`
     */
    public String getGasPrice (BREthereumAmount.Unit unit) {
        assert (!unit.isTokenUnit());
        return ewm.get().jniTransactionGetGasPrice(identifier, unit.jniValue);
    }

    /**
     * The gasLimit in `gas`
     *
     * @return in `gas`
     */
    public long getGasLimit () {
        return ewm.get().jniTransactionGetGasLimit(identifier);
    }

    /**
     * The gasUsed in `gas`
     *
     * @return in `gas`
     */
    public long getGasUsed () {
        return ewm.get().jniTransactionGetGasUsed(identifier);
    }

    //
    // Nonce
    //
    public long getNonce () {
        return ewm.get().jniTransactionGetNonce(identifier);
    }

    //
    // Block Number, Timestamp
    //
    public long getBlockNumber () {
        return ewm.get().jniTransactionGetBlockNumber(identifier);
    }

    public long getBlockTimestamp () {
        return ewm.get().jniTransactionGetBlockTimestamp(identifier);
    }

    public long getBlockConfirmations () {
        return ewm.get().jniTransactionGetBlockConfirmations(identifier);
    }

    public String getErrorDescription () {
        return ewm.get().jniTransactionGetErrorDescription(identifier);
    }
}
