package org.mozilla.javascript;

public interface Function extends Scriptable, Callable {
  Object call(Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
  
  Scriptable construct(Context paramContext, Scriptable paramScriptable, Object[] paramArrayOfObject);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */