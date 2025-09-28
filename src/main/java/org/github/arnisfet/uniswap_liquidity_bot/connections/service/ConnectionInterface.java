package org.github.arnisfet.uniswap_liquidity_bot.connections.service;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ConnectionInterface {
    public BigDecimal getBalance();
    public String testConnection();
}
