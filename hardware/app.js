const bleno = require("bleno");

var Web3 = require('web3');
var web3 = new Web3();

const CALCULATOR_SERVICE_UUID = "00010000-89BD-43C8-9231-40F6E305F96D";
const ARGUMENT_1_UUID = "00010001-89BD-43C8-9231-40F6E305F96D";
const RESULT_UUID = "00010010-89BD-43C8-9231-40F6E305F96D";
const PUBKEY_UUID = "00010020-89BD-43C8-9231-40F6E305F96D";
const SMCONTR_UUID = "00010030-89BD-43C8-9231-40F6E305F96D";

const PRIV_KEY = "9c215ede75b688ce2f30372140068c815b78b2eedfe8bd9af04d8d7fddd8ef2e";
const PUB_KEY = "0x4296b2dc215cb8e29c8fc81234dd0103f3af6e25";
const SMCONTR = "0x0096b2dc215cb8e29c8fc81234dd0103f3af6e00"; // ?

var res;

class ArgumentCharacteristic extends bleno.Characteristic {
    constructor(uuid, name) {
        super({
            uuid: uuid,
            properties: ["write"],
            value: null,
            descriptors: [
                new bleno.Descriptor({
                    uuid: "2901",
                    value: name
                  })
            ]
        });

        this.argument = 0;
        this.name = name;
    }

    onWriteRequest(data, offset, withoutResponse, callback) {
        try {
            this.argument = data.readUInt8();
            console.log(`Argument ${this.name} is now ${this.argument}`);
            callback(this.RESULT_SUCCESS);

        } catch (err) {
            console.error(err);
            callback(this.RESULT_UNLIKELY_ERROR);
        }
    }
}

class ResultCharacteristic extends bleno.Characteristic {
    constructor(calcResultFunc) {
        super({
            uuid: RESULT_UUID,
            properties: ["read"],
            value: null,
            descriptors: [
                new bleno.Descriptor({
                    uuid: "2901",
                    value: "Signed result"
                  })
            ]
        });

        this.calcResultFunc = calcResultFunc;
    }

    onReadRequest(offset, callback) {
        try {
            console.log(`offset: ${offset}`);

            if(!offset) {
                let result = this.calcResultFunc();
                this._value = new Buffer(result)
            }
            callback(this.RESULT_SUCCESS, this._value.slice(offset, this._value.length));
        } catch (err) {
            console.error(err);
            callback(this.RESULT_UNLIKELY_ERROR);
        }
    }
}

class PubKeyCharacteristic extends bleno.Characteristic {
    constructor() {
        super({
            uuid: PUBKEY_UUID,
            properties: ["read"],
            value: PUB_KEY,
            descriptors: [
                new bleno.Descriptor({
                    uuid: "2901",
                    value: "Public Key"
                  })
            ]
        });
    }

}

class SmartContrCharacteristic extends bleno.Characteristic {
    constructor() {
        super({
            uuid: PUBKEY_UUID,
            properties: ["read"],
            value: SMCONTR,
            descriptors: [
                new bleno.Descriptor({
                    uuid: "2901",
                    value: "Smart Contract Address"
                  })
            ]
        });
    }
}

console.log("Starting bleno...");

bleno.on("stateChange", state => {

    if (state === "poweredOn") {
        
        bleno.startAdvertising("Crypto Anchor", [CALCULATOR_SERVICE_UUID], err => {
            if (err) console.log(err);
        });

    } else {
        console.log("Stopping...");
        bleno.stopAdvertising();
    }        
});

function signHash(){
    let res = web3.eth.accounts.sign(SMCONTR, PRIV_KEY);
    console.log(res);
    return res.signature;
}

bleno.on("advertisingStart", err => {

    console.log("Configuring services...");
    
    if(err) {
        console.error(err);
        return;
    }

    let argument1 = new ArgumentCharacteristic(ARGUMENT_1_UUID, "Hash to sign");
    let result = new ResultCharacteristic(() => signHash());
    let pubKey = new PubKeyCharacteristic();
    let smCntr = new SmartContrCharacteristic();

    let calculator = new bleno.PrimaryService({
        uuid: CALCULATOR_SERVICE_UUID,
        characteristics: [
            argument1,
            result,
            pubKey,
            smCntr
        ]
    });

    bleno.setServices([calculator], err => {
        if(err)
            console.log(err);
        else
            console.log("Services configured");
    });
});


// some diagnostics 
bleno.on("stateChange", state => console.log(`Bleno: Adapter changed state to ${state}`));

bleno.on("advertisingStart", err => console.log("Bleno: advertisingStart"));
bleno.on("advertisingStartError", err => console.log("Bleno: advertisingStartError"));
bleno.on("advertisingStop", err => console.log("Bleno: advertisingStop"));

bleno.on("servicesSet", err => console.log("Bleno: servicesSet"));
bleno.on("servicesSetError", err => console.log("Bleno: servicesSetError"));

bleno.on("accept", clientAddress => console.log(`Bleno: accept ${clientAddress}`));
bleno.on("disconnect", clientAddress => console.log(`Bleno: disconnect ${clientAddress}`));

