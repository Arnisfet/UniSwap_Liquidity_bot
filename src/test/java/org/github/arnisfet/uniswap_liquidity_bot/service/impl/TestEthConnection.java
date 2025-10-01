package org.github.arnisfet.uniswap_liquidity_bot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.github.arnisfet.uniswap_liquidity_bot.data.TokenData;
import org.github.arnisfet.uniswap_liquidity_bot.networks.sepolia.service.ArbitrumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class TestEthConnection {
    @Autowired
    private ArbitrumService uniService;
    @Autowired
    TokenData tokenData;

    @Test
    public void testConnection () {
        String connection = uniService.testConnection();
        assertNotNull(connection);
        log.info("Ethereum client version: {}", connection);
    }
    @Test
    public void getBalanceTest() {
        BigDecimal value = uniService.getBalance();
        assertNotNull(value);
    }

    @Test
    public  void getARBbalance() {
        BigDecimal val = uniService.getBalanceARB(tokenData.getSepolia().get("USDC"));
        assertNotNull(val);
    }
}
