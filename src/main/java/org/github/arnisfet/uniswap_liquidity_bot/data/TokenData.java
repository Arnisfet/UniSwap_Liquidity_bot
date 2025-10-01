package org.github.arnisfet.uniswap_liquidity_bot.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "contracts")
public class TokenData {
    private Map<String, String> sepolia;
}
