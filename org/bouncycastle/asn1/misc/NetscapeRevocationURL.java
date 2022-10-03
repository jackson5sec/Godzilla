package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.DERIA5String;

public class NetscapeRevocationURL extends DERIA5String {
  public NetscapeRevocationURL(DERIA5String paramDERIA5String) {
    super(paramDERIA5String.getString());
  }
  
  public String toString() {
    return "NetscapeRevocationURL: " + getString();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\misc\NetscapeRevocationURL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */