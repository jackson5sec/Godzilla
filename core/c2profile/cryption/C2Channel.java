/*    */ package core.c2profile.cryption;
/*    */ 
/*    */ import core.Encoding;
/*    */ import core.imp.Cryption;
/*    */ import core.imp.Payload;
/*    */ import core.shell.ShellEntity;
/*    */ 
/*    */ public class C2Channel
/*    */   implements Cryption
/*    */ {
/*    */   public ShellEntity shellEntity;
/*    */   public Payload payload;
/*    */   public Encoding encoding;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 16 */     this.shellEntity = context; this.shellEntity.getEncodingModule();
/* 17 */     this.payload = this.shellEntity.getPayloadModule();
/* 18 */     this.encoding = this.shellEntity.getEncodingModule();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/* 23 */     return new byte[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/* 28 */     return new byte[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 38 */     return new byte[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 43 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\cryption\C2Channel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */