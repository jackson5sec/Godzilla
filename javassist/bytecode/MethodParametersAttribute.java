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
/*    */ public class MethodParametersAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "MethodParameters";
/*    */   
/*    */   MethodParametersAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 19 */     super(cp, n, in);
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
/*    */   public MethodParametersAttribute(ConstPool cp, String[] names, int[] flags) {
/* 31 */     super(cp, "MethodParameters");
/* 32 */     byte[] data = new byte[names.length * 4 + 1];
/* 33 */     data[0] = (byte)names.length;
/* 34 */     for (int i = 0; i < names.length; i++) {
/* 35 */       ByteArray.write16bit(cp.addUtf8Info(names[i]), data, i * 4 + 1);
/* 36 */       ByteArray.write16bit(flags[i], data, i * 4 + 3);
/*    */     } 
/*    */     
/* 39 */     set(data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 47 */     return this.info[0] & 0xFF;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int name(int i) {
/* 56 */     return ByteArray.readU16bit(this.info, i * 4 + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String parameterName(int i) {
/* 64 */     return getConstPool().getUtf8Info(name(i));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int accessFlags(int i) {
/* 74 */     return ByteArray.readU16bit(this.info, i * 4 + 3);
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
/* 85 */     int s = size();
/* 86 */     ConstPool cp = getConstPool();
/* 87 */     String[] names = new String[s];
/* 88 */     int[] flags = new int[s];
/* 89 */     for (int i = 0; i < s; i++) {
/* 90 */       names[i] = cp.getUtf8Info(name(i));
/* 91 */       flags[i] = accessFlags(i);
/*    */     } 
/*    */     
/* 94 */     return new MethodParametersAttribute(newCp, names, flags);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\MethodParametersAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */