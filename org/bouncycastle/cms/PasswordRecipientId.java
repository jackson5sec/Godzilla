package org.bouncycastle.cms;

public class PasswordRecipientId extends RecipientId {
  public PasswordRecipientId() {
    super(3);
  }
  
  public int hashCode() {
    return 3;
  }
  
  public boolean equals(Object paramObject) {
    return !!(paramObject instanceof PasswordRecipientId);
  }
  
  public Object clone() {
    return new PasswordRecipientId();
  }
  
  public boolean match(Object paramObject) {
    return (paramObject instanceof PasswordRecipientInformation);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\PasswordRecipientId.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */