/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import com.kichik.pecoff4j.ResourceDirectory;
/*    */ import com.kichik.pecoff4j.ResourceEntry;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class ResourceHelper
/*    */ {
/*    */   public static ResourceEntry[] findResources(ResourceDirectory rd, int type) {
/* 20 */     return findResources(rd, type, -1, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ResourceEntry[] findResources(ResourceDirectory rd, int type, int name) {
/* 25 */     return findResources(rd, type, name, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ResourceEntry[] findResources(ResourceDirectory rd, int type, int name, int lang) {
/* 30 */     List<ResourceEntry> entries = new ArrayList<>();
/* 31 */     if (rd != null) {
/* 32 */       findResources(rd, type, name, lang, entries);
/*    */     }
/* 34 */     return entries.<ResourceEntry>toArray(new ResourceEntry[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   private static void findResources(ResourceDirectory parent, int type, int name, int language, List<ResourceEntry> entries) {
/* 39 */     int id = type;
/* 40 */     if (id == -1)
/* 41 */       id = name; 
/* 42 */     if (id == -1)
/* 43 */       id = language; 
/* 44 */     for (int i = 0; i < parent.size(); i++) {
/* 45 */       ResourceEntry e = parent.get(i);
/* 46 */       if (id == -1 || id == e.getId())
/* 47 */         if (e.getData() != null) {
/* 48 */           entries.add(e);
/*    */         } else {
/* 50 */           ResourceDirectory rd = e.getDirectory();
/* 51 */           if (rd != null) {
/* 52 */             if (type != -1) {
/* 53 */               type = -1;
/* 54 */             } else if (name != -1) {
/* 55 */               name = -1;
/*    */             } else {
/* 57 */               language = -1;
/* 58 */             }  findResources(rd, type, name, language, entries);
/*    */           } 
/*    */         }  
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void addResource(int type, int name, int lang, byte[] data) {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\ResourceHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */