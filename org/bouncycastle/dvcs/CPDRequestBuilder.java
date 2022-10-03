package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.DVCSRequestInformationBuilder;
import org.bouncycastle.asn1.dvcs.Data;
import org.bouncycastle.asn1.dvcs.ServiceType;

public class CPDRequestBuilder extends DVCSRequestBuilder {
  public CPDRequestBuilder() {
    super(new DVCSRequestInformationBuilder(ServiceType.CPD));
  }
  
  public DVCSRequest build(byte[] paramArrayOfbyte) throws DVCSException {
    Data data = new Data(paramArrayOfbyte);
    return createDVCRequest(data);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\dvcs\CPDRequestBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */