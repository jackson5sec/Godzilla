package org.bouncycastle.i18n.filter;

public class TrustedInput {
  protected Object input;
  
  public TrustedInput(Object paramObject) {
    this.input = paramObject;
  }
  
  public Object getInput() {
    return this.input;
  }
  
  public String toString() {
    return this.input.toString();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\i18n\filter\TrustedInput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */