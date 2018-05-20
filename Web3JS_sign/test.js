var Web3 = require('web3');
var utils = require('ethereumjs-util');
var Accounts = require('web3-eth-accounts');

//var web3 = new Web3();

var accounts = new Accounts('ws://localhost:8546');

//var defaultProvider = new web3.providers.HttpProvider('https://rinkeby.infura.io/C77vAgKQPAk4gP56F7kg');
//var defaultProvider = new web3.providers.HttpProvider();
//web3.setProvider(defaultProvider);

var message = '8dfe9be33ccb1c830e048219729e8c01f54c768004d8dc035105629515feb38e';
var messageBuffer = new Buffer(message, 'hex');
console.log( 'message: ', message);

//var signature = web3.eth.sign('0xa079837344995f15b327cace995caddd6138be51', message);
//console.log(web3.eth.accounts);
var res = accounts.sign(message, '0x9c215ede75b688ce2f30372140068c815b78b2eedfe8bd9af04d8d7fddd8ef2e');
console.log( 'signature: ', res);
/*
signature = signature.split('x')[1];

var r = new Buffer(signature.substring(0, 64), 'hex')
var s = new Buffer(signature.substring(64, 128), 'hex')
var v = new Buffer((parseInt(signature.substring(128, 130)) + 27).toString());

// console.log('r s v: ', r, s , v)

// console.log('v: ', v)

var pub = utils.ecrecover(messageBuffer, v, r, s);

var recoveredAddress = '0x' + utils.pubToAddress(pub).toString('hex')

console.log('recoveredAddress: ',   recoveredAddress);

console.log( 'isMatch: ', recoveredAddress === account_address );
*/