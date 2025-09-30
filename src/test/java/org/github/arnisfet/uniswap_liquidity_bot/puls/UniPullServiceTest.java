package org.github.arnisfet.uniswap_liquidity_bot.puls;

import org.github.arnisfet.uniswap_liquidity_bot.pulls.data.UniPullService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UniPullServiceTest {
    @Autowired
    private UniPullService uniPullService;
    @Test
    public void ethToWethTest() {
        try {
            String hash = uniPullService.ethToWeth(new BigDecimal(0.001));
            assertNotNull(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
