package org.github.arnisfet.uniswap_liquidity_bot.puls;

import lombok.extern.slf4j.Slf4j;
import org.github.arnisfet.uniswap_liquidity_bot.data.TokenData;
import org.github.arnisfet.uniswap_liquidity_bot.pulls.data.UniPullService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class UniPullServiceTest {
    @Autowired
    private UniPullService uniPullService;
    @Autowired
    private TokenData tokenData;
    @Test
    public void ethToWethTest() {
        String wETHAdress = tokenData.getSepolia().get("WETH");
        try {
            String hash = uniPullService.ethToWeth(new BigDecimal(0.001), wETHAdress);
            assertNotNull(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getPullUniV3Test() {
        String weth = "0x980B62Da83eFf3D4576C647993b0c1D7faf17c73";  // адрес WETH в Arbitrum Sepolia
        String usdc = "0x75faf114eafb1BDbe2F0316DF893fd58CE46AA4d";  // адрес USDC в Arbitrum Sepolia

        String pool = uniPullService.getUniPull(weth, usdc, 500);
        log.info("Pool adress: " + pool);
    }
}
