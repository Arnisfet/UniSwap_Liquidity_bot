package org.github.arnisfet.uniswap_liquidity_bot.networks.sepolia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.arnisfet.uniswap_liquidity_bot.networks.NetworkInterface;
import org.github.arnisfet.uniswap_liquidity_bot.infrustructure.MetaMaskConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArbitrumService implements NetworkInterface {

    private final Web3j web3;
    private final MetaMaskConfig metaMaskConfig;
    private final Credentials credentials;

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

    public BigDecimal getBalanceARB() {
        String usdcAddr = "0x75faf114eafb1BDbe2F0316DF893fd58CE46AA4d";
        Function function = new Function(
                "balanceOf",
                Arrays.asList(new Address(credentials.getAddress())),
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = null;

        try {
            response  = web3.ethCall(
                    Transaction.createEthCallTransaction(credentials.getAddress(), usdcAddr, encodedFunction),
                    DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Type> someTypes = FunctionReturnDecoder.decode(
                response.getValue(), function.getOutputParameters()
        );

        if (someTypes.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigInteger rawBalance = (BigInteger) someTypes.get(0).getValue();

        // У USDT 6 знаков после запятой
        return new BigDecimal(rawBalance).divide(BigDecimal.TEN.pow(6));
    }
}
