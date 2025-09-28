package org.github.arnisfet.uniswap_liquidity_bot.connections.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.github.arnisfet.uniswap_liquidity_bot.connections.service.ConnectionInterface;
import org.github.arnisfet.uniswap_liquidity_bot.infrustructure.MetaMaskConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class ARBConnectionService implements ConnectionInterface {

    private final String HTTP_CONNECT;
    private final Web3j web3;
    private final MetaMaskConfig metaMaskConfig;
    private final Credentials credentials;

    ARBConnectionService(@Value(value = "${infura.arbitrum}") String http,
                         MetaMaskConfig metaMaskConfig) {
        HTTP_CONNECT = http;
        this.web3 = Web3j.build(new HttpService(HTTP_CONNECT));
        this.metaMaskConfig = metaMaskConfig;
        this.credentials = Credentials.create(metaMaskConfig.getPrivate_key());
    }

    @Override
    public BigDecimal getBalance() {
        try {
            BigInteger value =  web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
            return Convert.fromWei(new BigDecimal(value), Convert.Unit.ETHER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String testConnection() {
        try {
            String clientVersion = web3.web3ClientVersion().send().getWeb3ClientVersion();
            return clientVersion;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
