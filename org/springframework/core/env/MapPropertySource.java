/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MapPropertySource
/*    */   extends EnumerablePropertySource<Map<String, Object>>
/*    */ {
/*    */   public MapPropertySource(String name, Map<String, Object> source) {
/* 43 */     super(name, source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getProperty(String name) {
/* 50 */     return this.source.get(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean containsProperty(String name) {
/* 55 */     return this.source.containsKey(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getPropertyNames() {
/* 60 */     return StringUtils.toStringArray(this.source.keySet());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\MapPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */