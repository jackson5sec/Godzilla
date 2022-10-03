package org.bouncycastle.crypto.agreement.jpake;

import java.math.BigInteger;

public class JPAKERound3Payload {
  private final String participantId;
  
  private final BigInteger macTag;
  
  public JPAKERound3Payload(String paramString, BigInteger paramBigInteger) {
    this.participantId = paramString;
    this.macTag = paramBigInteger;
  }
  
  public String getParticipantId() {
    return this.participantId;
  }
  
  public BigInteger getMacTag() {
    return this.macTag;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\agreement\jpake\JPAKERound3Payload.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */