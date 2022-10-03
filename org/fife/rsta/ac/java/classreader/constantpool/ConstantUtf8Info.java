/*     */ package org.fife.rsta.ac.java.classreader.constantpool;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantUtf8Info
/*     */   extends ConstantPoolInfo
/*     */ {
/*     */   private String representedString;
/*     */   
/*     */   public ConstantUtf8Info(byte[] bytes) {
/*  32 */     super(1);
/*     */     
/*  34 */     this.representedString = createRepresentedString(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String createRepresentedString(byte[] bytes) {
/*  96 */     this.representedString = new String(bytes, StandardCharsets.UTF_8);
/*  97 */     return this.representedString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRepresentedString(boolean quoted) {
/* 110 */     if (!quoted) {
/* 111 */       return this.representedString;
/*     */     }
/* 113 */     String temp = "\"" + this.representedString.replaceAll("\"", "\\\"") + "\"";
/* 114 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return "[ConstantUtf8Info: " + this.representedString + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantUtf8Info.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */