/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionTableEntry
/*     */ {
/*     */   private ClassFile cf;
/*     */   private int startPC;
/*     */   private int endPC;
/*     */   private int handlerPC;
/*     */   private int catchType;
/*     */   
/*     */   public ExceptionTableEntry(ClassFile cf) {
/*  77 */     this.cf = cf;
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
/*     */   public String getCaughtThrowableType(boolean fullyQualified) {
/*  90 */     return (this.catchType == 0) ? null : this.cf
/*  91 */       .getClassNameFromConstantPool(this.catchType, fullyQualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndPC() {
/*  96 */     return this.endPC;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHandlerPC() {
/* 101 */     return this.handlerPC;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartPC() {
/* 106 */     return this.startPC;
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
/*     */   public static ExceptionTableEntry read(ClassFile cf, DataInputStream in) throws IOException {
/* 120 */     ExceptionTableEntry entry = new ExceptionTableEntry(cf);
/* 121 */     entry.startPC = in.readUnsignedShort();
/* 122 */     entry.endPC = in.readUnsignedShort();
/* 123 */     entry.handlerPC = in.readUnsignedShort();
/* 124 */     entry.catchType = in.readUnsignedShort();
/* 125 */     return entry;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\ExceptionTableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */