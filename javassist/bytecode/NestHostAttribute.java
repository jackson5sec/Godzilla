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
/*    */ public class NestHostAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "NestHost";
/*    */   
/*    */   NestHostAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 36 */     super(cp, n, in);
/*    */   }
/*    */   
/*    */   private NestHostAttribute(ConstPool cp, int hostIndex) {
/* 40 */     super(cp, "NestHost", new byte[2]);
/* 41 */     ByteArray.write16bit(hostIndex, get(), 0);
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
/* 54 */     int hostIndex = ByteArray.readU16bit(get(), 0);
/* 55 */     int newHostIndex = getConstPool().copy(hostIndex, newCp, classnames);
/* 56 */     return new NestHostAttribute(newCp, newHostIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hostClassIndex() {
/* 65 */     return ByteArray.readU16bit(this.info, 0);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\NestHostAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */