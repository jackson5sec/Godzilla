package org.bouncycastle.asn1;

import java.util.Date;

public class DERGeneralizedTime extends ASN1GeneralizedTime {
  DERGeneralizedTime(byte[] paramArrayOfbyte) {
    super(paramArrayOfbyte);
  }
  
  public DERGeneralizedTime(Date paramDate) {
    super(paramDate);
  }
  
  public DERGeneralizedTime(String paramString) {
    super(paramString);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DERGeneralizedTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */