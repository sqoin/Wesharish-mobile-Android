/*
 * BaseAIT
 *
 * Created by Ed Gamble <ed@sqoin.com> on 1/22/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.crypto;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseAIT {
    // Which?
    protected Context context    = InstrumentationRegistry.getInstrumentation().getContext();
    protected Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() {
        assertEquals("us.sqoin.crypto.test", appContext.getPackageName());
        assertEquals("us.sqoin.crypto.test", context.getPackageName());
    }

}

