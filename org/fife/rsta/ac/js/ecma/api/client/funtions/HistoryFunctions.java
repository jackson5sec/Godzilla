package org.fife.rsta.ac.js.ecma.api.client.funtions;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSNumber;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSString;
import org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5ObjectFunctions;

public interface HistoryFunctions extends JS5ObjectFunctions {
  void back();
  
  void forward();
  
  void go(JSNumber paramJSNumber);
  
  void go(JSString paramJSString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\client\funtions\HistoryFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */