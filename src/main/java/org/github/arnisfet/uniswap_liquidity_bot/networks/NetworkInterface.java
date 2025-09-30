package org.github.arnisfet.uniswap_liquidity_bot.networks;

import java.math.BigDecimal;

public interface NetworkInterface {
    public BigDecimal getBalance();
    public String testConnection();
}
