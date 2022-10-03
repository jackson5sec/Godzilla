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
/*    */ public class C2Request
/*    */ {
/*    */   @YamlComment(Comment = "Request 查询字符串 支持C2信道")
/* 43 */   public String requestQueryString = ""; @YamlComment(Comment = "Request Method")
/* 44 */   public String requestMethod = "POST"; @YamlComment(Comment = "请求协议头 支持C2信道")
/* 45 */   public LinkedHashMap<String, String> requestHeaders = new LinkedHashMap<>(); @YamlComment(Comment = "请求表单参数 支持C2信道")
/* 46 */   public LinkedHashMap<String, String> requestFormParameters = new LinkedHashMap<>(); @YamlComment(Comment = "请求url参数 支持C2信道")
/* 47 */   public LinkedHashMap<String, String> requestUrlParameters = new LinkedHashMap<>(); @YamlComment(Comment = "请求Cookies 支持C2信道")
/* 48 */   public LinkedHashMap<String, String> requestCookies = new LinkedHashMap<>(); @YamlComment(Comment = "请求左边追加数据")
/* 49 */   public byte[] requestLeftBody = "".getBytes(); @YamlComment(Comment = "请求右边追加数据")
/* 50 */   public byte[] requestRightBody = "".getBytes();
/*    */   @YamlComment(Comment = "是否开启Request Body写入")
/*    */   public boolean enabledRequestBody;
/*    */   @YamlComment(Comment = "请求中间数据 支持C2信道")
/*    */   public String requestMiddleBody;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2Request.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */