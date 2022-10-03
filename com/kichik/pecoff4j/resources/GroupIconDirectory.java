/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ import com.kichik.pecoff4j.io.DataReader;
/*    */ import com.kichik.pecoff4j.io.IDataReader;
/*    */ import com.kichik.pecoff4j.util.Reflection;
/*    */ import java.io.IOException;
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
/*    */ public class GroupIconDirectory
/*    */ {
/*    */   private int reserved;
/*    */   private int type;
/*    */   private int count;
/*    */   private GroupIconDirectoryEntry[] entries;
/*    */   
/*    */   public int getReserved() {
/* 25 */     return this.reserved;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 29 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 33 */     return this.count;
/*    */   }
/*    */   
/*    */   public GroupIconDirectoryEntry getEntry(int index) {
/* 37 */     return this.entries[index];
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return Reflection.toString(this);
/*    */   }
/*    */   
/*    */   public static GroupIconDirectory read(byte[] data) throws IOException {
/* 46 */     return read((IDataReader)new DataReader(data));
/*    */   }
/*    */   
/*    */   public static GroupIconDirectory read(IDataReader dr) throws IOException {
/* 50 */     GroupIconDirectory gi = new GroupIconDirectory();
/* 51 */     gi.reserved = dr.readWord();
/* 52 */     gi.type = dr.readWord();
/* 53 */     gi.count = dr.readWord();
/* 54 */     gi.entries = new GroupIconDirectoryEntry[gi.count];
/* 55 */     for (int i = 0; i < gi.count; i++) {
/* 56 */       gi.entries[i] = GroupIconDirectoryEntry.read(dr);
/*    */     }
/*    */     
/* 59 */     return gi;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\GroupIconDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */