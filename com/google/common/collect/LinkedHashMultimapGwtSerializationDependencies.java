/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ abstract class LinkedHashMultimapGwtSerializationDependencies<K, V>
/*    */   extends AbstractSetMultimap<K, V>
/*    */ {
/*    */   LinkedHashMultimapGwtSerializationDependencies(Map<K, Collection<V>> map) {
/* 36 */     super(map);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\LinkedHashMultimapGwtSerializationDependencies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */