/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class LittleEndianDataOutputStream
/*     */   extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   public LittleEndianDataOutputStream(OutputStream out) {
/*  48 */     super(new DataOutputStream((OutputStream)Preconditions.checkNotNull(out)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  54 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBoolean(boolean v) throws IOException {
/*  59 */     ((DataOutputStream)this.out).writeBoolean(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeByte(int v) throws IOException {
/*  64 */     ((DataOutputStream)this.out).writeByte(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeBytes(String s) throws IOException {
/*  74 */     ((DataOutputStream)this.out).writeBytes(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeChar(int v) throws IOException {
/*  85 */     writeShort(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeChars(String s) throws IOException {
/*  96 */     for (int i = 0; i < s.length(); i++) {
/*  97 */       writeChar(s.charAt(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeDouble(double v) throws IOException {
/* 109 */     writeLong(Double.doubleToLongBits(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFloat(float v) throws IOException {
/* 120 */     writeInt(Float.floatToIntBits(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInt(int v) throws IOException {
/* 131 */     this.out.write(0xFF & v);
/* 132 */     this.out.write(0xFF & v >> 8);
/* 133 */     this.out.write(0xFF & v >> 16);
/* 134 */     this.out.write(0xFF & v >> 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLong(long v) throws IOException {
/* 145 */     byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
/* 146 */     write(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeShort(int v) throws IOException {
/* 157 */     this.out.write(0xFF & v);
/* 158 */     this.out.write(0xFF & v >> 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeUTF(String str) throws IOException {
/* 163 */     ((DataOutputStream)this.out).writeUTF(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 171 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\LittleEndianDataOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */