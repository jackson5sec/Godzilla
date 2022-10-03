package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.Data;

public class CCPDRequestData extends DVCSRequestData {
  CCPDRequestData(Data paramData) throws DVCSConstructionException {
    super(paramData);
    initDigest();
  }
  
  private void initDigest() throws DVCSConstructionException {
    if (this.data.getMessageImprint() == null)
      throw new DVCSConstructionException("DVCSRequest.data.messageImprint should be specified for CCPD service"); 
  }
  
  public MessageImprint getMessageImprint() {
    return new MessageImprint(this.data.getMessageImprint());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\dvcs\CCPDRequestData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */