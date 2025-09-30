package org.github.arnisfet.uniswap_liquidity_bot.infrustructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;

@Configuration
@ConfigurationProperties(prefix = "credentials")
@Data
public class MetaMaskConfig {
    private String private_key;

    @Bean
    public Credentials credentials() {
        return Credentials.create(private_key);
    }
}
