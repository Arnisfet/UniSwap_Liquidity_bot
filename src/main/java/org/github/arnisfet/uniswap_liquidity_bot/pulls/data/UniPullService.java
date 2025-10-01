package org.github.arnisfet.uniswap_liquidity_bot.pulls.data;

import lombok.RequiredArgsConstructor;
import org.github.arnisfet.uniswap_liquidity_bot.data.TokenData;
import org.github.arnisfet.uniswap_liquidity_bot.networks.NetworkInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint24;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UniPullService {
    @Value("${uniswap.sepolia.v3.factory}")
    private String UNI_FACTORY;
    private final Web3j web3;
    private final Credentials credentials;

    public String ethToWeth(BigDecimal amount, String wETHAddress) throws Exception {
        BigInteger amountInWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

        // Получаем nonce
        BigInteger nonce = web3.ethGetTransactionCount(
                        credentials.getAddress(), org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                .send().getTransactionCount();

        // Создаем функцию deposit()
        Function function = new Function(
                "deposit",
                Collections.emptyList(),
                Collections.emptyList()
        );
        String encodedFunction = FunctionEncoder.encode(function);

        // Создаем RawTransaction
        org.web3j.crypto.RawTransaction rawTransaction = org.web3j.crypto.RawTransaction.createTransaction(
                nonce,
                DefaultGasProvider.GAS_PRICE,          // gasPrice
                BigInteger.valueOf(100_000),          // gasLimit, подкорректируй по сети
                wETHAddress,                          // to
                amountInWei,                           // value (ETH)
                encodedFunction                         // data
        );

        // Подписываем транзакцию
        byte[] signedMessage = org.web3j.crypto.TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = org.web3j.utils.Numeric.toHexString(signedMessage);

        // Отправляем транзакцию
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction.hasError()) {
            throw new RuntimeException("Transaction failed: " + ethSendTransaction.getError().getMessage());
        }

        return ethSendTransaction.getTransactionHash();
    }

    public String getUniPull(String firstToken, String secondToken, int fee) {
        Function function = new Function(
                "getPool",
                Arrays.asList(new Address(firstToken), new Address(secondToken), new Uint24(BigInteger.valueOf(fee))),
                Collections.singletonList(new TypeReference<Address>() {})
        );

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response;
        try {
            response = web3.ethCall(
                    Transaction.createEthCallTransaction(
                            null, UNI_FACTORY, encodedFunction
                    ),
                    org.web3j.protocol.core.DefaultBlockParameterName.LATEST
            ).send();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());

        if (decoded.isEmpty()) {
            return null;
        }
        Address poolAddress = (Address) decoded.get(0);
        return poolAddress.getValue();
    }
}
