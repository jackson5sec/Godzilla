package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.x509.DigestInfo;

public class MessageImprint {
  private final DigestInfo messageImprint;
  
  public MessageImprint(DigestInfo paramDigestInfo) {
    this.messageImprint = paramDigestInfo;
  }
  
  public DigestInfo toASN1Structure() {
    return this.messageImprint;
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject instanceof MessageImprint) ? this.messageImprint.equals(((MessageImprint)paramObject).messageImprint) : false);
  }
  
  public int hashCode() {
    return this.messageImprint.hashCode();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\dvcs\MessageImprint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */