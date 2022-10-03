package org.fife.rsta.ac.java;

interface MemberCompletion extends JavaSourceCompletion {
  String getEnclosingClassName(boolean paramBoolean);
  
  String getSignature();
  
  String getType();
  
  boolean isDeprecated();
  
  public static interface Data extends IconFactory.IconData {
    String getEnclosingClassName(boolean param1Boolean);
    
    String getSignature();
    
    String getSummary();
    
    String getType();
    
    boolean isConstructor();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\MemberCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */