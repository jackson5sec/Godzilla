/*    */ package javassist.bytecode;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
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
/*    */ public class ConstantAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "ConstantValue";
/*    */   
/*    */   ConstantAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 35 */     super(cp, n, in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConstantAttribute(ConstPool cp, int index) {
/* 46 */     super(cp, "ConstantValue");
/* 47 */     byte[] bvalue = new byte[2];
/* 48 */     bvalue[0] = (byte)(index >>> 8);
/* 49 */     bvalue[1] = (byte)index;
/* 50 */     set(bvalue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getConstantValue() {
/* 57 */     return ByteArray.readU16bit(get(), 0);
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
/*    */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 70 */     int index = getConstPool().copy(getConstantValue(), newCp, classnames);
/*    */     
/* 72 */     return new ConstantAttribute(newCp, index);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ConstantAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */