/*    */ package util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import util.http.Parameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParameterOutputStream
/*    */ {
/*    */   private ByteArrayOutputStream out;
/*    */   
/*    */   public ParameterOutputStream() {
/* 14 */     this(new ByteArrayOutputStream());
/*    */   }
/*    */   
/*    */   public ParameterOutputStream(ByteArrayOutputStream outputStream) {
/* 18 */     this.out = outputStream;
/*    */   }
/*    */   public void writeParameter(Parameter parameter) throws IOException {
/* 21 */     parameter.serialize(this.out);
/* 22 */     this.out.write(3);
/*    */   }
/*    */   public byte[] getBuffer() {
/* 25 */     return this.out.toByteArray();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\ParameterOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */