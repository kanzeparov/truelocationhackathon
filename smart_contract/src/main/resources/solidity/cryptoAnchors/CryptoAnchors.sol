pragma solidity ^0.4.0;
contract CryptoAnchors {
    
    address owner;
//    address user;

    
//    string status; //status now (in progress, transportation)
//    address key;
//    uint256 latitude;   //gps
//    uint256 longitude; 
//    uint time;
    string name_type;
    mapping(address => address) units;
    mapping(address => address) user_unit;     //key => user
    mapping(address => string) status_unit; //key => status
    mapping(address => uint256) latitude_unit;  //key => latitude
    mapping(address => uint256) longitude_unit;  //key => longitude
    mapping(address => uint) time_unit;  //key => time
    
    
    function CryptoAnchors (string _name_type) public {
        owner = msg.sender;
        name_type=_name_type;      
    }    
    
    function UpdateState (string _status, uint256 _latitude, uint256 _longitude, uint8 v, bytes32 r, bytes32 s, bytes32 signedHash, address UnitId) public
    {
        require(ecrecover(signedHash, v, r, s) == UnitId);
        require(units[UnitId]==UnitId);
        
        user_unit[UnitId] = msg.sender;
        status_unit[UnitId] = _status;
        latitude_unit[UnitId] = _latitude;
        longitude_unit[UnitId] = _longitude;
        time_unit[UnitId] = now;
    
    }
    
    //event States(address UnitId, address user_unit, string status_unit, uint256 latitude_unit, uint256 longitude_unit, uint time_unit);


    function AddUnit (address _key) public
    {
        require(owner == msg.sender);
        
        units[_key]=_key;
    }
    
    function TypeName() public view returns (string) {
        return name_type;
    }

    /*
    function State(address UnitId) public view returns (address, string, uint256, uint256, uint) {
        return (user_unit[UnitId], status_unit[UnitId], latitude_unit[UnitId], longitude_unit[UnitId], time_unit[UnitId]);
    }
    */

}