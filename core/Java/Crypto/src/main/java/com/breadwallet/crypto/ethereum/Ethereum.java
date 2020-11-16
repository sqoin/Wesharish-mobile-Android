/*
 * Ethereum
 *
 * Created by Ed Gamble <ed@sqoin.com> on 1/22/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package us.sqoin.crypto.ethereum;

import us.sqoin.crypto.Currency;
import us.sqoin.crypto.Network;
import us.sqoin.crypto.Unit;

public interface Ethereum {
    Currency currency = new Currency ("ETH", "Ξ", "Ethereum", 18, "WEI", "wei");
    interface Units {
        Unit WEI = currency.baseUnit;
        Unit ETHER = currency.defaultUnit;

        Unit GWEI = new Unit ("GWEI", "gwei", 1000000000, WEI);
    }

    interface Networks {
        Network mainnet = new Network(new Network.Ethereum("Eth Mainnet", 1));
        Network ropsten = new Network(new Network.Ethereum("Eth Ropsten", 3));
        Network rinkeby = new Network(new Network.Ethereum("Eth Rinkeby", 4));
    }
}

