/*    */ package util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import util.http.Parameter;
/*    */ 
/*    */ public class ParameterInputStream {
/*    */   private InputStream in;
/*    */   
/*    */   public ParameterInputStream(byte[] data) {
/* 11 */     this(new ByteArrayInputStream(data));
/*    */   }
/*    */   public ParameterInputStream(InputStream inputStream) {
/* 14 */     this.in = inputStream;
/*    */   }
/*    */   public Parameter readParameter() {
/* 17 */     return Parameter.unSerialize(this.in);
/*    */   }
/*    */   
/*    */   public static ParameterInputStream asParameterInputStream(byte[] data) {
/* 21 */     return new ParameterInputStream(new ByteArrayInputStream(data));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\ParameterInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */