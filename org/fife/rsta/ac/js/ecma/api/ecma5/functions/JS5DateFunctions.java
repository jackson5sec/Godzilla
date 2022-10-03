package org.fife.rsta.ac.js.ecma.api.ecma5.functions;

import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSDateFunctions;
import org.fife.rsta.ac.js.ecma.api.ecma5.JS5String;

public interface JS5DateFunctions extends JS5ObjectFunctions, JSDateFunctions {
  JS5String toISOString();
  
  JS5String toJSON(JS5String paramJS5String);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma5\functions\JS5DateFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */