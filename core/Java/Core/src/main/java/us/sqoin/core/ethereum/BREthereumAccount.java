/*
 * EthereumAccount
 *
 * Created by Ed Gamble <ed@sqoin.com> on 3/20/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.core.ethereum;

public class BREthereumAccount extends BREthereumEWM.Reference {

    protected BREthereumAccount(BREthereumEWM ewm, long identifier) {
        super (ewm, identifier);
    }

    public String getPrimaryAddress () {
        return ewm.get().jniGetAccountPrimaryAddress(identifier);
    }

    public byte[] getPrimaryAddressPublicKey () {
        return ewm.get().jniGetAccountPrimaryAddressPublicKey(identifier);
    }

    public byte[] getPrimaryAddressPrivateKey (String paperKey) {
        return ewm.get().jniGetAccountPrimaryAddressPrivateKey(identifier, paperKey);
    }
}
