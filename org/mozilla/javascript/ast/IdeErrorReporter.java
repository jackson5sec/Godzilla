package org.mozilla.javascript.ast;

import org.mozilla.javascript.ErrorReporter;

public interface IdeErrorReporter extends ErrorReporter {
  void warning(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  void error(String paramString1, String paramString2, int paramInt1, int paramInt2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\IdeErrorReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */