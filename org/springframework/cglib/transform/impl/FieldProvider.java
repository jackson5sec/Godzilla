package org.springframework.cglib.transform.impl;

public interface FieldProvider {
  String[] getFieldNames();
  
  Class[] getFieldTypes();
  
  void setField(int paramInt, Object paramObject);
  
  Object getField(int paramInt);
  
  void setField(String paramString, Object paramObject);
  
  Object getField(String paramString);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\FieldProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */