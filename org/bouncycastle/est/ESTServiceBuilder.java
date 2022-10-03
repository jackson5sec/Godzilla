package org.bouncycastle.est;

public class ESTServiceBuilder {
  protected final String server;
  
  protected ESTClientProvider clientProvider;
  
  protected String label;
  
  public ESTServiceBuilder(String paramString) {
    this.server = paramString;
  }
  
  public ESTServiceBuilder withLabel(String paramString) {
    this.label = paramString;
    return this;
  }
  
  public ESTServiceBuilder withClientProvider(ESTClientProvider paramESTClientProvider) {
    this.clientProvider = paramESTClientProvider;
    return this;
  }
  
  public ESTService build() {
    return new ESTService(this.server, this.label, this.clientProvider);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\ESTServiceBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */