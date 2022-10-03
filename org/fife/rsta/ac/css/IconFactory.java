/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.ImageIcon;
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
/*    */ class IconFactory
/*    */ {
/*    */   private static IconFactory INSTANCE;
/* 37 */   private Map<String, Icon> iconMap = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IconFactory get() {
/* 47 */     if (INSTANCE == null) {
/* 48 */       INSTANCE = new IconFactory();
/*    */     }
/* 50 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon(String key) {
/* 61 */     Icon icon = this.iconMap.get(key);
/* 62 */     if (icon == null) {
/* 63 */       icon = loadIcon(key + ".gif");
/* 64 */       this.iconMap.put(key, icon);
/*    */     } 
/* 66 */     return icon;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Icon loadIcon(String name) {
/* 77 */     URL res = getClass().getResource("img/" + name);
/* 78 */     if (res == null)
/*    */     {
/*    */ 
/*    */       
/* 82 */       throw new IllegalArgumentException("icon not found: img/" + name);
/*    */     }
/* 84 */     return new ImageIcon(res);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\IconFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */