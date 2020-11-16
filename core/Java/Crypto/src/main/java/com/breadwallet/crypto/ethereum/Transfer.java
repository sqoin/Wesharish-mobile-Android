/*
 * Transfer
 *
 * Created by Ed Gamble <ed@sqoin.com> on 1/22/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.crypto.ethereum;

import us.sqoin.crypto.Address;
import us.sqoin.crypto.Amount;
import us.sqoin.crypto.Wallet;

class Transfer extends us.sqoin.crypto.Transfer {
    long core;

    private Transfer(long core, Wallet wallet, Address source, Address target, Amount amount, Amount fee) {
        super(wallet, source, target, amount, fee);
        this.core = core;
    }

    protected Transfer (Wallet wallet, long core) {
        this (core, wallet, null, null, null, null);
    }

}
