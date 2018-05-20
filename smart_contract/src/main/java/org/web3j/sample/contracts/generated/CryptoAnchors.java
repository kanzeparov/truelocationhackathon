package org.web3j.sample.contracts.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class CryptoAnchors extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405161073738038061073783398101604052805160008054600160a060020a03191633600160a060020a031617905501805161005590600190602084019061005c565b50506100f7565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061009d57805160ff19168380011785556100ca565b828001600101855582156100ca579182015b828111156100ca5782518255916020019190600101906100af565b506100d69291506100da565b5090565b6100f491905b808211156100d657600081556001016100e0565b90565b610631806101066000396000f3006080604052600436106100615763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166305aa48f18114610066578063515dbdec146100895780637dbfdbcc14610113578063bf007653146101da575b600080fd5b34801561007257600080fd5b50610087600160a060020a0360043516610262565b005b34801561009557600080fd5b5061009e6102b4565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100d85781810151838201526020016100c0565b50505050905090810190601f1680156101055780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011f57600080fd5b50610134600160a060020a036004351661034a565b6040518086600160a060020a0316600160a060020a0316815260200180602001858152602001848152602001838152602001828103825286818151815260200191508051906020019080838360005b8381101561019b578181015183820152602001610183565b50505050905090810190601f1680156101c85780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b3480156101e657600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261008794369492936024939284019190819084018382808284375094975050843595505050602083013592604081013560ff1692506060810135915060808101359060a08101359060c00135600160a060020a0316610432565b60005433600160a060020a0390811691161461027d57600080fd5b600160a060020a03166000818152600260205260409020805473ffffffffffffffffffffffffffffffffffffffff19169091179055565b60018054604080516020601f6002600019610100878916150201909516949094049384018190048102820181019092528281526060939092909183018282801561033f5780601f106103145761010080835404028352916020019161033f565b820191906000526020600020905b81548152906001019060200180831161032257829003601f168201915b505050505090505b90565b600160a060020a03818116600090815260036020908152604080832054600483528184206005845282852054600685528386205460078652848720548354865160026101006001841615026000190190921691909104601f810189900489028201890190975286815297986060988a9889988998909316969594939286918301828280156104195780601f106103ee57610100808354040283529160200191610419565b820191906000526020600020905b8154815290600101906020018083116103fc57829003601f168201915b50989f939e50959c50939a509198509650505050505050565b604080516000808252602080830180855286905260ff89168385015260608301889052608083018790529251600160a060020a0385169360019360a0808201949293601f198101939281900390910191865af1158015610496573d6000803e3d6000fd5b50505060206040510351600160a060020a03161415156104b557600080fd5b600160a060020a03808216600081815260026020526040902054909116146104dc57600080fd5b600160a060020a038181166000908152600360209081526040808320805473ffffffffffffffffffffffffffffffffffffffff19163390951694909417909355600481529190208951610531928b019061056d565b50600160a060020a0316600090815260056020908152604080832098909855600681528782209690965560079095525050509190204290555050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105ae57805160ff19168380011785556105db565b828001600101855582156105db579182015b828111156105db5782518255916020019190600101906105c0565b506105e79291506105eb565b5090565b61034791905b808211156105e757600081556001016105f15600a165627a7a7230582060ddb9091536d7b51bb0e1cad0fcf55697e79a01333f1fbf3e1dfebb7a0096c20029";

    protected CryptoAnchors(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CryptoAnchors(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> AddUnit(String _key) {
        final Function function = new Function(
                "AddUnit", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> TypeName() {
        final Function function = new Function("TypeName", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>> GetState(String UnitId) {
        final Function function = new Function("GetState", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(UnitId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> UpdateState(String _status, BigInteger _latitude, BigInteger _longitude, BigInteger v, byte[] r, byte[] s, byte[] signedHash, String UnitId) {
        final Function function = new Function(
                "UpdateState", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_status), 
                new org.web3j.abi.datatypes.generated.Uint256(_latitude), 
                new org.web3j.abi.datatypes.generated.Uint256(_longitude), 
                new org.web3j.abi.datatypes.generated.Uint8(v), 
                new org.web3j.abi.datatypes.generated.Bytes32(r), 
                new org.web3j.abi.datatypes.generated.Bytes32(s), 
                new org.web3j.abi.datatypes.generated.Bytes32(signedHash), 
                new org.web3j.abi.datatypes.Address(UnitId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<CryptoAnchors> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name_type) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name_type)));
        return deployRemoteCall(CryptoAnchors.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<CryptoAnchors> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name_type) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name_type)));
        return deployRemoteCall(CryptoAnchors.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static CryptoAnchors load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CryptoAnchors(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CryptoAnchors load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CryptoAnchors(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
