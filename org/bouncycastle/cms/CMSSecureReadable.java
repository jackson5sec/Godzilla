package org.bouncycastle.cms;

import java.io.IOException;
import java.io.InputStream;

interface CMSSecureReadable {
  InputStream getInputStream() throws IOException, CMSException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSSecureReadable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */