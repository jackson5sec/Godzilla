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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalVariableTypeAttribute
/*    */   extends LocalVariableAttribute
/*    */ {
/*    */   public static final String tag = "LocalVariableTypeTable";
/*    */   
/*    */   public LocalVariableTypeAttribute(ConstPool cp) {
/* 38 */     super(cp, "LocalVariableTypeTable", new byte[2]);
/* 39 */     ByteArray.write16bit(0, this.info, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   LocalVariableTypeAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 45 */     super(cp, n, in);
/*    */   }
/*    */   
/*    */   private LocalVariableTypeAttribute(ConstPool cp, byte[] dest) {
/* 49 */     super(cp, "LocalVariableTypeTable", dest);
/*    */   }
/*    */ 
/*    */   
/*    */   String renameEntry(String desc, String oldname, String newname) {
/* 54 */     return SignatureAttribute.renameClass(desc, oldname, newname);
/*    */   }
/*    */ 
/*    */   
/*    */   String renameEntry(String desc, Map<String, String> classnames) {
/* 59 */     return SignatureAttribute.renameClass(desc, classnames);
/*    */   }
/*    */ 
/*    */   
/*    */   LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
/* 64 */     return new LocalVariableTypeAttribute(cp, dest);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\LocalVariableTypeAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */