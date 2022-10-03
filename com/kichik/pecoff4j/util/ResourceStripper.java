/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import com.kichik.pecoff4j.PE;
/*    */ import com.kichik.pecoff4j.io.PEParser;
/*    */ import java.io.File;
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
/*    */ public class ResourceStripper
/*    */ {
/*    */   public static void remove(File pecoff, File output) throws Exception {
/* 19 */     PE pe = PEParser.parse(pecoff);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\ResourceStripper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */