/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Action;
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
/*     */ public class RoundRobinAutoCompletion
/*     */   extends AutoCompletion
/*     */ {
/*  38 */   private List<CompletionProvider> cycle = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RoundRobinAutoCompletion(CompletionProvider provider) {
/*  49 */     super(provider);
/*  50 */     this.cycle.add(provider);
/*     */ 
/*     */     
/*  53 */     setHideOnCompletionProviderChange(false);
/*     */ 
/*     */ 
/*     */     
/*  57 */     setHideOnNoText(false);
/*     */ 
/*     */ 
/*     */     
/*  61 */     setAutoCompleteSingleChoices(false);
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
/*     */   public void addCompletionProvider(CompletionProvider provider) {
/*  73 */     this.cycle.add(provider);
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
/*     */   public boolean advanceProvider() {
/*  86 */     CompletionProvider currentProvider = getCompletionProvider();
/*  87 */     int i = (this.cycle.indexOf(currentProvider) + 1) % this.cycle.size();
/*  88 */     setCompletionProvider(this.cycle.get(i));
/*  89 */     return (i == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Action createAutoCompleteAction() {
/*  98 */     return new CycleAutoCompleteAction();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetProvider() {
/* 106 */     CompletionProvider currentProvider = getCompletionProvider();
/* 107 */     CompletionProvider defaultProvider = this.cycle.get(0);
/* 108 */     if (currentProvider != defaultProvider) {
/* 109 */       setCompletionProvider(defaultProvider);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class CycleAutoCompleteAction
/*     */     extends AutoCompletion.AutoCompleteAction
/*     */   {
/*     */     private CycleAutoCompleteAction() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 123 */       if (RoundRobinAutoCompletion.this.isAutoCompleteEnabled()) {
/* 124 */         if (RoundRobinAutoCompletion.this.isPopupVisible()) {
/*     */ 
/*     */           
/* 127 */           RoundRobinAutoCompletion.this.advanceProvider();
/*     */         }
/*     */         else {
/*     */           
/* 131 */           RoundRobinAutoCompletion.this.resetProvider();
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 137 */         for (int i = 1; i < RoundRobinAutoCompletion.this.cycle.size(); i++) {
/* 138 */           List<Completion> completions = RoundRobinAutoCompletion.this.getCompletionProvider().getCompletions(RoundRobinAutoCompletion.this.getTextComponent());
/* 139 */           if (completions.size() > 0) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 145 */           RoundRobinAutoCompletion.this.advanceProvider();
/*     */         } 
/*     */       } 
/*     */       
/* 149 */       super.actionPerformed(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\RoundRobinAutoCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */