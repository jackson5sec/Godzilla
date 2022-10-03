/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
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
/*    */ public class LwButtonGroup
/*    */   implements IButtonGroup
/*    */ {
/*    */   private String myName;
/* 29 */   private ArrayList myComponentIds = new ArrayList();
/*    */   private boolean myBound;
/*    */   
/*    */   public void read(Element element) {
/* 33 */     this.myName = element.getAttributeValue("name");
/* 34 */     this.myBound = LwXmlReader.getOptionalBoolean(element, "bound", false);
/* 35 */     for (Iterator i = element.getChildren().iterator(); i.hasNext(); ) {
/* 36 */       Element child = i.next();
/* 37 */       this.myComponentIds.add(child.getAttributeValue("id"));
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return this.myName;
/*    */   }
/*    */   
/*    */   public String[] getComponentIds() {
/* 46 */     return (String[])this.myComponentIds.toArray((Object[])new String[this.myComponentIds.size()]);
/*    */   }
/*    */   
/*    */   public boolean isBound() {
/* 50 */     return this.myBound;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwButtonGroup.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */