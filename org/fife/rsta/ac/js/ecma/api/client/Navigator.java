package org.fife.rsta.ac.js.ecma.api.client;

import org.fife.rsta.ac.js.ecma.api.client.funtions.NavigatorFunctions;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSBoolean;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSFunction;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSString;
import org.fife.rsta.ac.js.ecma.api.ecma5.JS5Array;

public abstract class Navigator implements NavigatorFunctions {
  protected JSFunction constructor;
  
  public Navigator prototype;
  
  public JSString appCodeName;
  
  public JSString appName;
  
  public JSString appVersion;
  
  public JSBoolean cookieEnabled;
  
  public JS5Array mimeTypes;
  
  public JSString platform;
  
  public JS5Array plugins;
  
  public JSString userAgent;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\client\Navigator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */