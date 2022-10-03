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
/*    */ final class LongVector
/*    */ {
/*    */   static final int ASIZE = 128;
/*    */   static final int ABITS = 7;
/*    */   static final int VSIZE = 8;
/*    */   private ConstInfo[][] objects;
/*    */   private int elements;
/*    */   
/*    */   public LongVector() {
/* 27 */     this.objects = new ConstInfo[8][];
/* 28 */     this.elements = 0;
/*    */   }
/*    */   
/*    */   public LongVector(int initialSize) {
/* 32 */     int vsize = (initialSize >> 7 & 0xFFFFFFF8) + 8;
/* 33 */     this.objects = new ConstInfo[vsize][];
/* 34 */     this.elements = 0;
/*    */   }
/*    */   public int size() {
/* 37 */     return this.elements;
/*    */   } public int capacity() {
/* 39 */     return this.objects.length * 128;
/*    */   }
/*    */   public ConstInfo elementAt(int i) {
/* 42 */     if (i < 0 || this.elements <= i) {
/* 43 */       return null;
/*    */     }
/* 45 */     return this.objects[i >> 7][i & 0x7F];
/*    */   }
/*    */   
/*    */   public void addElement(ConstInfo value) {
/* 49 */     int nth = this.elements >> 7;
/* 50 */     int offset = this.elements & 0x7F;
/* 51 */     int len = this.objects.length;
/* 52 */     if (nth >= len) {
/* 53 */       ConstInfo[][] newObj = new ConstInfo[len + 8][];
/* 54 */       System.arraycopy(this.objects, 0, newObj, 0, len);
/* 55 */       this.objects = newObj;
/*    */     } 
/*    */     
/* 58 */     if (this.objects[nth] == null) {
/* 59 */       this.objects[nth] = new ConstInfo[128];
/*    */     }
/* 61 */     this.objects[nth][offset] = value;
/* 62 */     this.elements++;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\LongVector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */