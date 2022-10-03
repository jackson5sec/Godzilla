package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.SecurityController;

public abstract class SecurityProxy extends SecurityController {
  protected abstract void callProcessFileSecure(Context paramContext, Scriptable paramScriptable, String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\SecurityProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */