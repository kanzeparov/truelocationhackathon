package org.web3j.sample;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.sample.contracts.generated.CryptoAnchors;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
//import org.web3j.sample.crypto.Sign;
import org.ethereum.jsonrpc.TypeConverter;

// for eth-blockchain
import rx.Subscription;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Application {

    private Web3j web3j;

    Application() {
        web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/C77vAgKQPAk4gP56F7kg"));
    }

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new Application().run();
    }

    public static Bytes32 stringToBytes32(String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

    private void deploy() throws Exception
    {
        Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/C77vAgKQPAk4gP56F7kg"));
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "123",
                        "wallets/UTC--2018-05-19T22-35-02.65261000Z--a079837344995f15b327cace995caddd6138be51.json");
        log.info("Credentials loaded");


        // FIXME: Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");

        /*
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0x19e03255f667bdfd50a32722df860b1eeaf4d635",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash());

        */
        // Now lets deploy a smart contract
        log.info("Deploying smart contract");

        CryptoAnchors contract = CryptoAnchors.deploy(
                web3j, credentials,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                "alc").send();

        String contractAddress = contract.getContractAddress();
        log.info("Smart contract deployed to address " + contractAddress);
        log.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);
    }

    private void updateState() throws Exception
    {
        BigInteger _latitude = new BigInteger("123");
        BigInteger _longitude = new BigInteger("123");
        BigInteger _v = new BigInteger("1c", 16);
        byte[] _r = TypeConverter.StringHexToByteArray("0xf444a383acba466a2aed2582895c614323bb97aa6b74e04f97922b176bc1aa2c");
        byte[] _s = TypeConverter.StringHexToByteArray("0x4f23d377cb19cc04f408a2ea4fb219d69e3bcb0a5585d06561a34712185a0e77");
        byte[] _signedHash = TypeConverter.StringHexToByteArray("0x8dfe9be33ccb1c830e048219729e8c01f54c768004d8dc035105629515feb38e");

        log.info(_r.toString());
        log.info(_s.toString());
        log.info(_v.toString());


        Credentials credentials = Credentials.create("0x9c215ede75b688ce2f30372140068c815b78b2eedfe8bd9af04d8d7fddd8ef2e");

        CryptoAnchors contract = CryptoAnchors.load(
                "0x4296b2dc215cb8e29c8fc81234dd0103f3af6e25",
                web3j, credentials,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
        );

        contract.UpdateState("asdf", _latitude, _longitude, _v, _r, _s, _signedHash, "0xf6f45356002Eee48a0333c480da441dAdFcE1373").send();
    }

    private void run() throws Exception {

        //deploy();

        updateState();

        /*
        BigInteger _price = BigInteger.ONE;
        _price = BigInteger.valueOf(10);

        BigInteger _steps = BigInteger.ONE;
        _steps = BigInteger.valueOf(3);

        CryptoAnchors contract2 = CryptoAnchors.load(
                contractAddress,
                web3j, credentials2,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT
        );
        //log.info("Value stored in remote smart contract: " + contract.greet().send());

        // Lets modify the value in our smart contract
        BigInteger value = BigInteger.ONE;
        value = BigInteger.valueOf(20);
        TransactionReceipt transactionReceipt = contract2.joinGame(value).send();

        //BigInteger rating = contract2.Rating_game().send();

        //contract2.update_step().send();
        contract2.stopGame().send();

        contract2.take_deposit_player().send();

        contract1.take_amount_owner().send();

        //log.info("New value stored in remote smart contract: " + contract.greet().send());
        */
    }
}
