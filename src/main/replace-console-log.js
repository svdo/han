var console={};
function replaceConsole(logType){
  return function() {
    var output={logType:logType, args:arguments};
    if (arguments.length > 2 && arguments[2].message){
      output.message=arguments[2].message;
      output.stack=arguments[2].stack;
    };
    var asString=JSON.stringify(output);
    document.body.innerHTML+='<pre>'+asString+'</pre>';
    window.ReactNativeWebView.postMessage(asString);
    }
};
console.debug=replaceConsole('debug');
console.log=replaceConsole('log');
console.info=replaceConsole('info');
console.error=replaceConsole('error');
console.exception=replaceConsole('exception');
console.warn=replaceConsole('warn');
window.console=console;
