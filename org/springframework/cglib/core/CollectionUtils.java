/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class CollectionUtils
/*    */ {
/*    */   public static Map bucket(Collection c, Transformer t) {
/* 29 */     Map<Object, Object> buckets = new HashMap<Object, Object>();
/* 30 */     for (Iterator it = c.iterator(); it.hasNext(); ) {
/* 31 */       Object value = it.next();
/* 32 */       Object key = t.transform(value);
/* 33 */       List<Object> bucket = (List)buckets.get(key);
/* 34 */       if (bucket == null) {
/* 35 */         buckets.put(key, bucket = new LinkedList());
/*    */       }
/* 37 */       bucket.add(value);
/*    */     } 
/* 39 */     return buckets;
/*    */   }
/*    */   
/*    */   public static void reverse(Map source, Map target) {
/* 43 */     for (Iterator it = source.keySet().iterator(); it.hasNext(); ) {
/* 44 */       Object key = it.next();
/* 45 */       target.put(source.get(key), key);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Collection filter(Collection c, Predicate p) {
/* 50 */     Iterator it = c.iterator();
/* 51 */     while (it.hasNext()) {
/* 52 */       if (!p.evaluate(it.next())) {
/* 53 */         it.remove();
/*    */       }
/*    */     } 
/* 56 */     return c;
/*    */   }
/*    */   
/*    */   public static List transform(Collection c, Transformer t) {
/* 60 */     List<Object> result = new ArrayList(c.size());
/* 61 */     for (Iterator it = c.iterator(); it.hasNext();) {
/* 62 */       result.add(t.transform(it.next()));
/*    */     }
/* 64 */     return result;
/*    */   }
/*    */   
/*    */   public static Map getIndexMap(List list) {
/* 68 */     Map<Object, Object> indexes = new HashMap<Object, Object>();
/* 69 */     int index = 0;
/* 70 */     for (Iterator it = list.iterator(); it.hasNext();) {
/* 71 */       indexes.put(it.next(), new Integer(index++));
/*    */     }
/* 73 */     return indexes;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\CollectionUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */