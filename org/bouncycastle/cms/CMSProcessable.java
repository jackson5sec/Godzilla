package org.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;

public interface CMSProcessable {
  void write(OutputStream paramOutputStream) throws IOException, CMSException;
  
  Object getContent();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSProcessable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */