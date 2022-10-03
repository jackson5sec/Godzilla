package org.fife.rsta.ac.js.ecma.api.ecma3.functions;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSNumber;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSString;

public interface JSNumberFunctions extends JSObjectFunctions {
  JSString toFixed(JSNumber paramJSNumber);
  
  JSString toExponential(JSNumber paramJSNumber);
  
  JSString toPrecision(JSNumber paramJSNumber);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\functions\JSNumberFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */