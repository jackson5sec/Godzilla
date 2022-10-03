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
/*    */ public class SourceFileAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "SourceFile";
/*    */   
/*    */   SourceFileAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 35 */     super(cp, n, in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SourceFileAttribute(ConstPool cp, String filename) {
/* 45 */     super(cp, "SourceFile");
/* 46 */     int index = cp.addUtf8Info(filename);
/* 47 */     byte[] bvalue = new byte[2];
/* 48 */     bvalue[0] = (byte)(index >>> 8);
/* 49 */     bvalue[1] = (byte)index;
/* 50 */     set(bvalue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFileName() {
/* 57 */     return getConstPool().getUtf8Info(ByteArray.readU16bit(get(), 0));
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
/* 70 */     return new SourceFileAttribute(newCp, getFileName());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\SourceFileAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */