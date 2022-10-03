/*    */ package org.hamcrest;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class StringDescription
/*    */   extends BaseDescription
/*    */ {
/*    */   private final Appendable out;
/*    */   
/*    */   public StringDescription() {
/* 12 */     this(new StringBuilder());
/*    */   }
/*    */   
/*    */   public StringDescription(Appendable out) {
/* 16 */     this.out = out;
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
/*    */   public static String toString(SelfDescribing selfDescribing) {
/* 28 */     return (new StringDescription()).appendDescriptionOf(selfDescribing).toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String asString(SelfDescribing selfDescribing) {
/* 35 */     return toString(selfDescribing);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void append(String str) {
/*    */     try {
/* 41 */       this.out.append(str);
/* 42 */     } catch (IOException e) {
/* 43 */       throw new RuntimeException("Could not write description", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void append(char c) {
/*    */     try {
/* 50 */       this.out.append(c);
/* 51 */     } catch (IOException e) {
/* 52 */       throw new RuntimeException("Could not write description", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return this.out.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\StringDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */