package org.bouncycastle.tsp.cms;

import org.bouncycastle.tsp.TimeStampToken;

public class ImprintDigestInvalidException extends Exception {
  private TimeStampToken token;
  
  public ImprintDigestInvalidException(String paramString, TimeStampToken paramTimeStampToken) {
    super(paramString);
    this.token = paramTimeStampToken;
  }
  
  public TimeStampToken getTimeStampToken() {
    return this.token;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\tsp\cms\ImprintDigestInvalidException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */