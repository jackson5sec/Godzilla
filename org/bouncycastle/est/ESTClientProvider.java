package org.bouncycastle.est;

public interface ESTClientProvider {
  ESTClient makeClient() throws ESTException;
  
  boolean isTrusted();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\ESTClientProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */