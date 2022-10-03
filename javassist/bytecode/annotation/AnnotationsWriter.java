/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class AnnotationsWriter
/*     */ {
/*     */   protected OutputStream output;
/*     */   private ConstPool pool;
/*     */   
/*     */   public AnnotationsWriter(OutputStream os, ConstPool cp) {
/*  72 */     this.output = os;
/*  73 */     this.pool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/*  80 */     return this.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  88 */     this.output.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numParameters(int num) throws IOException {
/*  98 */     this.output.write(num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numAnnotations(int num) throws IOException {
/* 108 */     write16bit(num);
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
/*     */   public void annotation(String type, int numMemberValuePairs) throws IOException {
/* 123 */     annotation(this.pool.addUtf8Info(type), numMemberValuePairs);
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
/*     */   public void annotation(int typeIndex, int numMemberValuePairs) throws IOException {
/* 138 */     write16bit(typeIndex);
/* 139 */     write16bit(numMemberValuePairs);
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
/*     */   public void memberValuePair(String memberName) throws IOException {
/* 152 */     memberValuePair(this.pool.addUtf8Info(memberName));
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
/*     */   public void memberValuePair(int memberNameIndex) throws IOException {
/* 166 */     write16bit(memberNameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(boolean value) throws IOException {
/* 176 */     constValueIndex(90, this.pool.addIntegerInfo(value ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(byte value) throws IOException {
/* 186 */     constValueIndex(66, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(char value) throws IOException {
/* 196 */     constValueIndex(67, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(short value) throws IOException {
/* 206 */     constValueIndex(83, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(int value) throws IOException {
/* 216 */     constValueIndex(73, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(long value) throws IOException {
/* 226 */     constValueIndex(74, this.pool.addLongInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(float value) throws IOException {
/* 236 */     constValueIndex(70, this.pool.addFloatInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(double value) throws IOException {
/* 246 */     constValueIndex(68, this.pool.addDoubleInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(String value) throws IOException {
/* 256 */     constValueIndex(115, this.pool.addUtf8Info(value));
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
/*     */   public void constValueIndex(int tag, int index) throws IOException {
/* 270 */     this.output.write(tag);
/* 271 */     write16bit(index);
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
/*     */   public void enumConstValue(String typeName, String constName) throws IOException {
/* 284 */     enumConstValue(this.pool.addUtf8Info(typeName), this.pool
/* 285 */         .addUtf8Info(constName));
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
/*     */   public void enumConstValue(int typeNameIndex, int constNameIndex) throws IOException {
/* 300 */     this.output.write(101);
/* 301 */     write16bit(typeNameIndex);
/* 302 */     write16bit(constNameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void classInfoIndex(String name) throws IOException {
/* 312 */     classInfoIndex(this.pool.addUtf8Info(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void classInfoIndex(int index) throws IOException {
/* 322 */     this.output.write(99);
/* 323 */     write16bit(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void annotationValue() throws IOException {
/* 332 */     this.output.write(64);
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
/*     */   public void arrayValue(int numValues) throws IOException {
/* 346 */     this.output.write(91);
/* 347 */     write16bit(numValues);
/*     */   }
/*     */   
/*     */   protected void write16bit(int value) throws IOException {
/* 351 */     byte[] buf = new byte[2];
/* 352 */     ByteArray.write16bit(value, buf, 0);
/* 353 */     this.output.write(buf);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\AnnotationsWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */