/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.TemplateCompletion;
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
/*    */ public class JavaTemplateCompletion
/*    */   extends TemplateCompletion
/*    */   implements JavaSourceCompletion
/*    */ {
/*    */   private String icon;
/*    */   
/*    */   public JavaTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template) {
/* 34 */     this(provider, inputText, definitionString, template, (String)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String shortDesc) {
/* 41 */     this(provider, inputText, definitionString, template, shortDesc, (String)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String shortDesc, String summary) {
/* 48 */     super(provider, inputText, definitionString, template, shortDesc, summary);
/* 49 */     setIcon("templateIcon");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 55 */     return IconFactory.get().getIcon(this.icon);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 61 */     JavaShorthandCompletion.renderText(g, getInputText(), 
/* 62 */         getShortDescription(), x, y, selected);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setIcon(String iconId) {
/* 67 */     this.icon = iconId;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaTemplateCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */