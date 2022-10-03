/*    */ package org.fife.rsta.ac.java.classreader;
/*    */ 
/*    */ import java.io.DataInputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Util
/*    */   implements AccessFlags
/*    */ {
/*    */   public static boolean isDefault(int accessFlags) {
/* 42 */     int access = 7;
/* 43 */     return ((accessFlags & access) == 0);
/*    */   }
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
/*    */   public static boolean isPrivate(int accessFlags) {
/* 56 */     return ((accessFlags & 0x2) > 0);
/*    */   }
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
/*    */   public static boolean isProtected(int accessFlags) {
/* 69 */     return ((accessFlags & 0x4) > 0);
/*    */   }
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
/*    */   public static boolean isPublic(int accessFlags) {
/* 82 */     return ((accessFlags & 0x1) > 0);
/*    */   }
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
/*    */   public static void skipBytes(DataInputStream in, int count) throws IOException {
/* 95 */     int skipped = 0;
/* 96 */     while (skipped < count)
/* 97 */       skipped += in.skipBytes(count - skipped); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */