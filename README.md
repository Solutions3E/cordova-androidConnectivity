cordova-uriConverter
===================

Cordova Android Plugin for accessing Device Connectivity Attributes like Cell Signal Strength,Wifi Link Speed and Wifi Signal Level etc.

## Installing the plugin

The plugin confirms to the Cordova plugin specification, it can be installed
using the Cordova / Phonegap command line interface.

    phonegap plugin add https://github.com/Solutions3E/cordova-androidConnectivity.git

    cordova plugin add https://github.com/Solutions3E/cordova-androidConnectivity.git


## Using the plugin

The plugin creates the object `androidconnectivity` with the method `getJsonResult(success, fail)`.

Example - Get Android Connectivity Details as Json:
```javascript

androidconnectivity.getJsonResult(
function(resp){
    console.log(resp);
},
function(err){
    alert("Error: "+(err));
});


```
    
### Note for Android Use

AS the Device Connectivity Attributes like Cell Signal Strength,Wifi Link Speed etc are fetched through this plugin, the units for measuring the specification is also important. For example, Wifi Link Speed (dBm Units),Cell Signal Strength (0-30 Integer) and Wifi Signal Level (0-5 Integer).





