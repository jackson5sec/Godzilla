package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Integer;

public class SubsequentMessage extends ASN1Integer {
  public static final SubsequentMessage encrCert = new SubsequentMessage(0);
  
  public static final SubsequentMessage challengeResp = new SubsequentMessage(1);
  
  private SubsequentMessage(int paramInt) {
    super(paramInt);
  }
  
  public static SubsequentMessage valueOf(int paramInt) {
    if (paramInt == 0)
      return encrCert; 
    if (paramInt == 1)
      return challengeResp; 
    throw new IllegalArgumentException("unknown value: " + paramInt);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\crmf\SubsequentMessage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */