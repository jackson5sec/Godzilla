/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ParameterizedCompletion
/*     */   extends Completion
/*     */ {
/*     */   String getDefinitionString();
/*     */   
/*     */   Parameter getParam(int paramInt);
/*     */   
/*     */   int getParamCount();
/*     */   
/*     */   ParameterizedCompletionInsertionInfo getInsertionInfo(JTextComponent paramJTextComponent, boolean paramBoolean);
/*     */   
/*     */   boolean getShowParameterToolTip();
/*     */   
/*     */   public static class Parameter
/*     */   {
/*     */     private String name;
/*     */     private Object type;
/*     */     private String desc;
/*     */     private boolean isEndParam;
/*     */     
/*     */     public Parameter(Object type, String name) {
/*  92 */       this(type, name, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Parameter(Object type, String name, boolean endParam) {
/* 113 */       this.name = name;
/* 114 */       this.type = type;
/* 115 */       this.isEndParam = endParam;
/*     */     }
/*     */     
/*     */     public String getDescription() {
/* 119 */       return this.desc;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 123 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType() {
/* 132 */       return (this.type == null) ? null : this.type.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getTypeObject() {
/* 141 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEndParam() {
/* 152 */       return this.isEndParam;
/*     */     }
/*     */     
/*     */     public void setDescription(String desc) {
/* 156 */       this.desc = desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 161 */       StringBuilder sb = new StringBuilder();
/* 162 */       if (getType() != null) {
/* 163 */         sb.append(getType());
/*     */       }
/* 165 */       if (getName() != null) {
/* 166 */         if (getType() != null) {
/* 167 */           sb.append(' ');
/*     */         }
/* 169 */         sb.append(getName());
/*     */       } 
/* 171 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ParameterizedCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */