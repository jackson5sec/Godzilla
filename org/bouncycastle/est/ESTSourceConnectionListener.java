package org.bouncycastle.est;

import java.io.IOException;

public interface ESTSourceConnectionListener<T, I> {
  ESTRequest onConnection(Source<T> paramSource, ESTRequest paramESTRequest) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\ESTSourceConnectionListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */