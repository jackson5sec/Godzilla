package org.bouncycastle.est.jcajce;

import javax.net.ssl.SSLSocketFactory;

public interface SSLSocketFactoryCreator {
  SSLSocketFactory createFactory() throws Exception;
  
  boolean isTrusted();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\jcajce\SSLSocketFactoryCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */