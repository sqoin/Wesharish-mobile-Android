/*
 * MasterPublicKeyAIT
 *
 * Created by Ed Gamble <ed@sqoin.com> on 3/20/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.core.bitcoin;

import android.support.test.runner.AndroidJUnit4;

import us.sqoin.core.BRCoreMasterPubKey;
import us.sqoin.core.BaseAIT;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MasterPublicKeyAIT extends BaseAIT {
    @Test
    public void testMPKOne () {
        byte[] paperKeyAsBytes = paperKey.getBytes();
        BRCoreMasterPubKey key = new BRCoreMasterPubKey(paperKeyAsBytes, true);

        byte[] serializedMPK = key.serialize();
        BRCoreMasterPubKey keyToo = new BRCoreMasterPubKey(serializedMPK, false);

        assertArrayEquals(key.getPubKey(), keyToo.getPubKey());
    }
}
