/*
 * EthereumToken
 *
 * Created by Ed Gamble <ed@sqoin.com> on 3/20/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.core.ethereum;


import us.sqoin.core.BRCoreJniReference;

public class BREthereumToken extends BRCoreJniReference {

    protected BREthereumToken (long jniReferenceAddress) {
        super (jniReferenceAddress);
    }

    public native String getAddress ();
    public native String getSymbol ();
    public native String getName ();
    public native String getDescription ();
    public native int getDecimals ();

    public long getIdentifier () {
        return jniReferenceAddress;
    }


    protected static native long jniGetTokenBRD ();
    protected static native long[] jniTokenAll ();

    @Override
    public void dispose() {
        // avoid 'super.dispose()' and thus 'native.dispose()'
    }

    @Override
    public int hashCode() {
        return getAddress().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "BREthereumToken{" + getSymbol() + "}";
    }
}
