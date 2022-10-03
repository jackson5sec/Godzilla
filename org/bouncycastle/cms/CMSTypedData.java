package org.bouncycastle.cms;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CMSTypedData extends CMSProcessable {
  ASN1ObjectIdentifier getContentType();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSTypedData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */