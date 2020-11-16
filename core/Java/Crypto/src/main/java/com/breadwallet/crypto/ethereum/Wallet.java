/*
 * Wallet
 *
 * Created by Ed Gamble <ed@sqoin.com> on 1/22/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.crypto.ethereum;

import us.sqoin.crypto.Amount;
import us.sqoin.crypto.Currency;
import us.sqoin.crypto.WalletManager;

public class Wallet extends us.sqoin.crypto.Wallet {
    long core;

    /// Balance

    protected Amount balance;

    @Override
    public Amount getBalance() {
        return balance;
    }

    protected void setBalance(Amount balance) {
        this.balance = balance;
    }

    /// Transfers
    protected Transfer[] transfers;

    public Transfer[] getTransfers() {
        return transfers;
    }

    public void setTransfers(Transfer[] transfers) {
        this.transfers = transfers;
    }


    public Wallet(long core, WalletManager manager, Currency currency, String name, Amount balance, Transfer[] transfers) {
        super(manager, currency, name);
        this.core = core;
        this.balance = balance;
        this.transfers = transfers;
    }

    public Wallet (WalletManager manager, Currency currency, long core) {
        this (core, manager, currency, currency.name,
                new Amount (0, currency.baseUnit),
                new Transfer[]{});
    }
}
