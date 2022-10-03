/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.jdom.Element;
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
/*    */ public class LwIntroListModelProperty
/*    */   extends LwIntrospectedProperty
/*    */ {
/*    */   public LwIntroListModelProperty(String name, String propertyClassName) {
/* 29 */     super(name, propertyClassName);
/*    */   }
/*    */   
/*    */   public Object read(Element element) throws Exception {
/* 33 */     List list = element.getChildren("item", element.getNamespace());
/* 34 */     String[] result = new String[list.size()];
/* 35 */     for (int i = 0; i < list.size(); i++) {
/* 36 */       Element itemElement = list.get(i);
/* 37 */       result[i] = LwXmlReader.getRequiredString(itemElement, "value");
/*    */     } 
/* 39 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwIntroListModelProperty.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */