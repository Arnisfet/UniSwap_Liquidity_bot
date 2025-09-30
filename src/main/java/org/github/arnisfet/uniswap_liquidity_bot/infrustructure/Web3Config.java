package org.github.arnisfet.uniswap_liquidity_bot.infrustructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {
    @Value(value = "${infura.arbitrum}")
    private String connection;
    @Bean
    public Web3j build() {
        return Web3j.build(new HttpService(connection));
    }
}
