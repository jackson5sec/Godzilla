package org.bouncycastle.util.test;

public interface TestResult {
  boolean isSuccessful();
  
  Throwable getException();
  
  String toString();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\TestResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */