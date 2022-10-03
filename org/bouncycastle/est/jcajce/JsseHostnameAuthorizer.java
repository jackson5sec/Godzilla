package org.bouncycastle.est.jcajce;

import java.io.IOException;
import javax.net.ssl.SSLSession;

public interface JsseHostnameAuthorizer {
  boolean verified(String paramString, SSLSession paramSSLSession) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\jcajce\JsseHostnameAuthorizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */