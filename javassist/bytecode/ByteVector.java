/*    */ package javassist.bytecode;
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
/*    */ class ByteVector
/*    */   implements Cloneable
/*    */ {
/* 27 */   private byte[] buffer = new byte[64];
/* 28 */   private int size = 0;
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 33 */     ByteVector bv = (ByteVector)super.clone();
/* 34 */     bv.buffer = (byte[])this.buffer.clone();
/* 35 */     return bv;
/*    */   }
/*    */   public final int getSize() {
/* 38 */     return this.size;
/*    */   }
/*    */   public final byte[] copy() {
/* 41 */     byte[] b = new byte[this.size];
/* 42 */     System.arraycopy(this.buffer, 0, b, 0, this.size);
/* 43 */     return b;
/*    */   }
/*    */   
/*    */   public int read(int offset) {
/* 47 */     if (offset < 0 || this.size <= offset) {
/* 48 */       throw new ArrayIndexOutOfBoundsException(offset);
/*    */     }
/* 50 */     return this.buffer[offset];
/*    */   }
/*    */   
/*    */   public void write(int offset, int value) {
/* 54 */     if (offset < 0 || this.size <= offset) {
/* 55 */       throw new ArrayIndexOutOfBoundsException(offset);
/*    */     }
/* 57 */     this.buffer[offset] = (byte)value;
/*    */   }
/*    */   
/*    */   public void add(int code) {
/* 61 */     addGap(1);
/* 62 */     this.buffer[this.size - 1] = (byte)code;
/*    */   }
/*    */   
/*    */   public void add(int b1, int b2) {
/* 66 */     addGap(2);
/* 67 */     this.buffer[this.size - 2] = (byte)b1;
/* 68 */     this.buffer[this.size - 1] = (byte)b2;
/*    */   }
/*    */   
/*    */   public void add(int b1, int b2, int b3, int b4) {
/* 72 */     addGap(4);
/* 73 */     this.buffer[this.size - 4] = (byte)b1;
/* 74 */     this.buffer[this.size - 3] = (byte)b2;
/* 75 */     this.buffer[this.size - 2] = (byte)b3;
/* 76 */     this.buffer[this.size - 1] = (byte)b4;
/*    */   }
/*    */   
/*    */   public void addGap(int length) {
/* 80 */     if (this.size + length > this.buffer.length) {
/* 81 */       int newSize = this.size << 1;
/* 82 */       if (newSize < this.size + length) {
/* 83 */         newSize = this.size + length;
/*    */       }
/* 85 */       byte[] newBuf = new byte[newSize];
/* 86 */       System.arraycopy(this.buffer, 0, newBuf, 0, this.size);
/* 87 */       this.buffer = newBuf;
/*    */     } 
/*    */     
/* 90 */     this.size += length;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ByteVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */