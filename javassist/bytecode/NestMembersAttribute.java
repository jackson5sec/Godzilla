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
/*    */ public class NestMembersAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "NestMembers";
/*    */   
/*    */   NestMembersAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 36 */     super(cp, n, in);
/*    */   }
/*    */   
/*    */   private NestMembersAttribute(ConstPool cp, byte[] info) {
/* 40 */     super(cp, "NestMembers", info);
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
/* 53 */     byte[] src = get();
/* 54 */     byte[] dest = new byte[src.length];
/* 55 */     ConstPool cp = getConstPool();
/*    */     
/* 57 */     int n = ByteArray.readU16bit(src, 0);
/* 58 */     ByteArray.write16bit(n, dest, 0);
/*    */     
/* 60 */     for (int i = 0, j = 2; i < n; i++, j += 2) {
/* 61 */       int index = ByteArray.readU16bit(src, j);
/* 62 */       int newIndex = cp.copy(index, newCp, classnames);
/* 63 */       ByteArray.write16bit(newIndex, dest, j);
/*    */     } 
/*    */     
/* 66 */     return new NestMembersAttribute(newCp, dest);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int numberOfClasses() {
/* 74 */     return ByteArray.readU16bit(this.info, 0);
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
/*    */   public int memberClass(int index) {
/* 86 */     return ByteArray.readU16bit(this.info, index * 2 + 2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\NestMembersAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */