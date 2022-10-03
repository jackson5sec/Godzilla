/*    */ package util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class TemplateEx
/*    */ {
/*    */   public static String run(String code) {
/* 11 */     HashMap<String, String> map = new HashMap<>();
/* 12 */     String regex = "\\{[a-zA-Z][a-zA-Z0-9_]*\\}";
/* 13 */     Pattern p = Pattern.compile(regex);
/* 14 */     Matcher m = p.matcher(code);
/*    */     
/* 16 */     while (m.find()) {
/* 17 */       String g = m.group(0);
/* 18 */       map.putIfAbsent(g, functions.getRandomString(functions.random(3, 8)));
/*    */     } 
/* 20 */     Iterator<String> iterator = map.keySet().iterator();
/* 21 */     while (iterator.hasNext()) {
/* 22 */       String key = iterator.next();
/* 23 */       code = code.replace(key, map.get(key));
/*    */     } 
/* 25 */     return code;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\TemplateEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */