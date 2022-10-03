/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.UIManager;
/*     */ import org.fife.ui.autocomplete.AutoCompletion;
/*     */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.Util;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ public abstract class AbstractLanguageSupport
/*     */   implements LanguageSupport
/*     */ {
/*     */   private Map<RSyntaxTextArea, AutoCompletion> textAreaToAutoCompletion;
/*     */   private boolean autoCompleteEnabled;
/*     */   private boolean autoActivationEnabled;
/*     */   private int autoActivationDelay;
/*     */   private boolean parameterAssistanceEnabled;
/*     */   private boolean showDescWindow;
/*     */   private ListCellRenderer<Object> renderer;
/*     */   
/*     */   protected AbstractLanguageSupport() {
/*  84 */     setDefaultCompletionCellRenderer(null);
/*  85 */     this.textAreaToAutoCompletion = new HashMap<>();
/*  86 */     this.autoCompleteEnabled = true;
/*  87 */     this.autoActivationEnabled = false;
/*  88 */     this.autoActivationDelay = 300;
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
/*     */   protected AutoCompletion createAutoCompletion(CompletionProvider p) {
/* 100 */     AutoCompletion ac = new AutoCompletion(p);
/* 101 */     ac.setListCellRenderer(getDefaultCompletionCellRenderer());
/* 102 */     ac.setAutoCompleteEnabled(isAutoCompleteEnabled());
/* 103 */     ac.setAutoActivationEnabled(isAutoActivationEnabled());
/* 104 */     ac.setAutoActivationDelay(getAutoActivationDelay());
/* 105 */     ac.setParameterAssistanceEnabled(isParameterAssistanceEnabled());
/* 106 */     ac.setShowDescWindow(getShowDescWindow());
/* 107 */     return ac;
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
/*     */   protected ListCellRenderer<Object> createDefaultCompletionCellRenderer() {
/* 119 */     return new DefaultListCellRenderer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delegateToSubstanceRenderer(CompletionCellRenderer ccr) {
/*     */     try {
/* 130 */       ccr.delegateToSubstanceRenderer();
/* 131 */     } catch (Exception e) {
/*     */       
/* 133 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutoActivationDelay() {
/* 140 */     return this.autoActivationDelay;
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
/*     */   protected AutoCompletion getAutoCompletionFor(RSyntaxTextArea textArea) {
/* 152 */     return this.textAreaToAutoCompletion.get(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListCellRenderer<Object> getDefaultCompletionCellRenderer() {
/* 158 */     return this.renderer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowDescWindow() {
/* 164 */     return this.showDescWindow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<RSyntaxTextArea> getTextAreas() {
/* 174 */     return this.textAreaToAutoCompletion.keySet();
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
/*     */ 
/*     */   
/*     */   protected void installImpl(RSyntaxTextArea textArea, AutoCompletion ac) {
/* 189 */     this.textAreaToAutoCompletion.put(textArea, ac);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivationEnabled() {
/* 195 */     return this.autoActivationEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoCompleteEnabled() {
/* 201 */     return this.autoCompleteEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParameterAssistanceEnabled() {
/* 207 */     return this.parameterAssistanceEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoActivationDelay(int ms) {
/* 213 */     ms = Math.max(0, ms);
/* 214 */     if (ms != this.autoActivationDelay) {
/* 215 */       this.autoActivationDelay = ms;
/* 216 */       for (AutoCompletion ac : this.textAreaToAutoCompletion.values()) {
/* 217 */         ac.setAutoActivationDelay(this.autoActivationDelay);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoActivationEnabled(boolean enabled) {
/* 225 */     if (enabled != this.autoActivationEnabled) {
/* 226 */       this.autoActivationEnabled = enabled;
/* 227 */       for (AutoCompletion ac : this.textAreaToAutoCompletion.values()) {
/* 228 */         ac.setAutoActivationEnabled(enabled);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoCompleteEnabled(boolean enabled) {
/* 236 */     if (enabled != this.autoCompleteEnabled) {
/* 237 */       this.autoCompleteEnabled = enabled;
/* 238 */       for (AutoCompletion ac : this.textAreaToAutoCompletion.values()) {
/* 239 */         ac.setAutoCompleteEnabled(enabled);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCompletionCellRenderer(ListCellRenderer<Object> r) {
/* 247 */     if (r == null) {
/* 248 */       r = createDefaultCompletionCellRenderer();
/*     */     }
/* 250 */     if (r instanceof CompletionCellRenderer && 
/* 251 */       Util.getUseSubstanceRenderers() && 
/* 252 */       UIManager.getLookAndFeel().getClass().getName()
/* 253 */       .contains(".Substance")) {
/* 254 */       CompletionCellRenderer ccr = (CompletionCellRenderer)r;
/* 255 */       delegateToSubstanceRenderer(ccr);
/*     */     } 
/*     */     
/* 258 */     this.renderer = r;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterAssistanceEnabled(boolean enabled) {
/* 264 */     if (enabled != this.parameterAssistanceEnabled) {
/* 265 */       this.parameterAssistanceEnabled = enabled;
/* 266 */       for (AutoCompletion ac : this.textAreaToAutoCompletion.values()) {
/* 267 */         ac.setParameterAssistanceEnabled(enabled);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShowDescWindow(boolean show) {
/* 275 */     if (show != this.showDescWindow) {
/* 276 */       this.showDescWindow = show;
/* 277 */       for (AutoCompletion ac : this.textAreaToAutoCompletion.values()) {
/* 278 */         ac.setShowDescWindow(show);
/*     */       }
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void uninstallImpl(RSyntaxTextArea textArea) {
/* 294 */     AutoCompletion ac = getAutoCompletionFor(textArea);
/* 295 */     if (ac != null) {
/* 296 */       ac.uninstall();
/*     */     }
/* 298 */     this.textAreaToAutoCompletion.remove(textArea);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\AbstractLanguageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */