/*     */ package org.fife.rsta.ac.less;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.css.PropertyValueCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.FunctionCompletion;
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
/*     */ class LessCodeCompletionProvider
/*     */   extends PropertyValueCompletionProvider
/*     */ {
/*     */   private List<Completion> functionCompletions;
/*     */   
/*     */   LessCodeCompletionProvider() {
/*  42 */     super(true);
/*     */     try {
/*  44 */       this.functionCompletions = createFunctionCompletions();
/*  45 */     } catch (IOException ioe) {
/*  46 */       throw new RuntimeException(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean addLessCompletions(List<Completion> completions, PropertyValueCompletionProvider.LexerState state, JTextComponent comp, String alreadyEntered) {
/*  58 */     boolean modified = false;
/*     */     
/*  60 */     if (alreadyEntered != null && alreadyEntered.length() > 0 && alreadyEntered
/*  61 */       .charAt(0) == '@') {
/*  62 */       addLessVariableCompletions(completions, comp, alreadyEntered);
/*  63 */       modified = true;
/*     */     } 
/*     */     
/*  66 */     if (state == PropertyValueCompletionProvider.LexerState.VALUE) {
/*  67 */       addLessBuiltinFunctionCompletions(completions, alreadyEntered);
/*  68 */       modified = true;
/*     */     } 
/*     */     
/*  71 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLessBuiltinFunctionCompletions(List<Completion> completions, String alreadyEntered) {
/*  78 */     completions.addAll(this.functionCompletions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addLessVariableCompletions(List<Completion> completions, JTextComponent comp, String alreadyEntered) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Completion> createFunctionCompletions() throws IOException {
/*  90 */     Icon functionIcon = loadIcon("methpub_obj");
/*     */ 
/*     */     
/*  93 */     List<Completion> completions = loadFromXML("data/less_functions.xml");
/*  94 */     for (Completion fc : completions) {
/*  95 */       ((FunctionCompletion)fc).setIcon(functionIcon);
/*     */     }
/*     */     
/*  98 */     Collections.sort(completions);
/*  99 */     return completions;
/*     */   }
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
/*     */   private Icon loadIcon(String name) {
/* 112 */     String imageFile = "img/" + name + ".gif";
/* 113 */     URL res = getClass().getResource(imageFile);
/* 114 */     if (res == null)
/*     */     {
/*     */ 
/*     */       
/* 118 */       throw new IllegalArgumentException("icon not found: " + imageFile);
/*     */     }
/* 120 */     return new ImageIcon(res);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\less\LessCodeCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */