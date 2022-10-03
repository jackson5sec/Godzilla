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
/*    */ public class DeprecatedAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "Deprecated";
/*    */   
/*    */   DeprecatedAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 35 */     super(cp, n, in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DeprecatedAttribute(ConstPool cp) {
/* 44 */     super(cp, "Deprecated", new byte[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 55 */     return new DeprecatedAttribute(newCp);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\DeprecatedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */