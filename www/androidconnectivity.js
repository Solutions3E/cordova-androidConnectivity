/*global cordova, module*/

module.exports = {
    getJsonResult: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "AndroidConnectivity", "getjsonresult", []);
    }
};
