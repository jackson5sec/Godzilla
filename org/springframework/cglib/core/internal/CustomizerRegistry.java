/*    */ package org.springframework.cglib.core.internal;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.cglib.core.Customizer;
/*    */ import org.springframework.cglib.core.KeyFactoryCustomizer;
/*    */ 
/*    */ public class CustomizerRegistry {
/* 11 */   private Map<Class, List<KeyFactoryCustomizer>> customizers = (Map)new HashMap<Class<?>, List<KeyFactoryCustomizer>>(); private final Class[] customizerTypes;
/*    */   
/*    */   public CustomizerRegistry(Class[] customizerTypes) {
/* 14 */     this.customizerTypes = customizerTypes;
/*    */   }
/*    */   
/*    */   public void add(KeyFactoryCustomizer customizer) {
/* 18 */     Class<? extends KeyFactoryCustomizer> klass = (Class)customizer.getClass();
/* 19 */     for (Class type : this.customizerTypes) {
/* 20 */       if (type.isAssignableFrom(klass)) {
/* 21 */         List<KeyFactoryCustomizer> list = this.customizers.get(type);
/* 22 */         if (list == null) {
/* 23 */           this.customizers.put(type, list = new ArrayList<KeyFactoryCustomizer>());
/*    */         }
/* 25 */         list.add(customizer);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public <T> List<T> get(Class<T> klass) {
/* 31 */     List<KeyFactoryCustomizer> list = this.customizers.get(klass);
/* 32 */     if (list == null) {
/* 33 */       return Collections.emptyList();
/*    */     }
/* 35 */     return (List)list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static CustomizerRegistry singleton(Customizer customizer) {
/* 44 */     CustomizerRegistry registry = new CustomizerRegistry(new Class[] { Customizer.class });
/* 45 */     registry.add((KeyFactoryCustomizer)customizer);
/* 46 */     return registry;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\internal\CustomizerRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */