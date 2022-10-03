/*    */ package org.springframework.cglib.transform.impl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AbstractInterceptFieldCallback
/*    */   implements InterceptFieldCallback
/*    */ {
/*    */   public int writeInt(Object obj, String name, int oldValue, int newValue) {
/* 23 */     return newValue;
/* 24 */   } public char writeChar(Object obj, String name, char oldValue, char newValue) { return newValue; }
/* 25 */   public byte writeByte(Object obj, String name, byte oldValue, byte newValue) { return newValue; }
/* 26 */   public boolean writeBoolean(Object obj, String name, boolean oldValue, boolean newValue) { return newValue; }
/* 27 */   public short writeShort(Object obj, String name, short oldValue, short newValue) { return newValue; }
/* 28 */   public float writeFloat(Object obj, String name, float oldValue, float newValue) { return newValue; }
/* 29 */   public double writeDouble(Object obj, String name, double oldValue, double newValue) { return newValue; }
/* 30 */   public long writeLong(Object obj, String name, long oldValue, long newValue) { return newValue; } public Object writeObject(Object obj, String name, Object oldValue, Object newValue) {
/* 31 */     return newValue;
/*    */   }
/* 33 */   public int readInt(Object obj, String name, int oldValue) { return oldValue; }
/* 34 */   public char readChar(Object obj, String name, char oldValue) { return oldValue; }
/* 35 */   public byte readByte(Object obj, String name, byte oldValue) { return oldValue; }
/* 36 */   public boolean readBoolean(Object obj, String name, boolean oldValue) { return oldValue; }
/* 37 */   public short readShort(Object obj, String name, short oldValue) { return oldValue; }
/* 38 */   public float readFloat(Object obj, String name, float oldValue) { return oldValue; }
/* 39 */   public double readDouble(Object obj, String name, double oldValue) { return oldValue; }
/* 40 */   public long readLong(Object obj, String name, long oldValue) { return oldValue; } public Object readObject(Object obj, String name, Object oldValue) {
/* 41 */     return oldValue;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\AbstractInterceptFieldCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */