package org.github.arnisfet.uniswap_liquidity_bot.pulls.data;

import lombok.RequiredArgsConstructor;
import org.github.arnisfet.uniswap_liquidity_bot.networks.NetworkInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UniPullService {
    @Value(value = "${contracts.weth}")
    private String wETHContract;
    private final NetworkInterface networkInterface;
    private final Web3j web3;
    private final Credentials credentials;
    public String ethToWeth(BigDecimal amount) throws Exception {
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
                wETHContract,                          // to
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

}
