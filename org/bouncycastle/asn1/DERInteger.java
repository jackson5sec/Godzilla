package org.bouncycastle.asn1;

import java.math.BigInteger;

public class DERInteger extends ASN1Integer {
  public DERInteger(byte[] paramArrayOfbyte) {
    super(paramArrayOfbyte, true);
  }
  
  public DERInteger(BigInteger paramBigInteger) {
    super(paramBigInteger);
  }
  
  public DERInteger(long paramLong) {
    super(paramLong);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DERInteger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */