package org.bouncycastle.util.test;

import org.bouncycastle.util.Strings;

public class SimpleTestResult implements TestResult {
  private static final String SEPARATOR = Strings.lineSeparator();
  
  private boolean success;
  
  private String message;
  
  private Throwable exception;
  
  public SimpleTestResult(boolean paramBoolean, String paramString) {
    this.success = paramBoolean;
    this.message = paramString;
  }
  
  public SimpleTestResult(boolean paramBoolean, String paramString, Throwable paramThrowable) {
    this.success = paramBoolean;
    this.message = paramString;
    this.exception = paramThrowable;
  }
  
  public static TestResult successful(Test paramTest, String paramString) {
    return new SimpleTestResult(true, paramTest.getName() + ": " + paramString);
  }
  
  public static TestResult failed(Test paramTest, String paramString) {
    return new SimpleTestResult(false, paramTest.getName() + ": " + paramString);
  }
  
  public static TestResult failed(Test paramTest, String paramString, Throwable paramThrowable) {
    return new SimpleTestResult(false, paramTest.getName() + ": " + paramString, paramThrowable);
  }
  
  public static TestResult failed(Test paramTest, String paramString, Object paramObject1, Object paramObject2) {
    return failed(paramTest, paramString + SEPARATOR + "Expected: " + paramObject1 + SEPARATOR + "Found   : " + paramObject2);
  }
  
  public static String failedMessage(String paramString1, String paramString2, String paramString3, String paramString4) {
    StringBuffer stringBuffer = new StringBuffer(paramString1);
    stringBuffer.append(" failing ").append(paramString2);
    stringBuffer.append(SEPARATOR).append("    expected: ").append(paramString3);
    stringBuffer.append(SEPARATOR).append("    got     : ").append(paramString4);
    return stringBuffer.toString();
  }
  
  public boolean isSuccessful() {
    return this.success;
  }
  
  public String toString() {
    return this.message;
  }
  
  public Throwable getException() {
    return this.exception;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\SimpleTestResult.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */