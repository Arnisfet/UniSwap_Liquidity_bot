package org.github.arnisfet.uniswap_liquidity_bot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.github.arnisfet.uniswap_liquidity_bot.connections.service.impl.ARBConnectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class TestEthConnection {
    @Autowired
    private ARBConnectionService uniService;

    @Test
    public void testConnection () {
        String connection = uniService.testConnection();
        assertNotNull(connection);
        log.info("Ethereum client version: {}", connection);
    }
    @Test
    public void getBalanceTest() {
        uniService.getBalance();
    }
}
