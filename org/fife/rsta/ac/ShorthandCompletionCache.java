/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
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
/*     */ public class ShorthandCompletionCache
/*     */ {
/*     */   private List<Completion> shorthandCompletion;
/*     */   private List<Completion> commentCompletion;
/*     */   private AbstractCompletionProvider templateProvider;
/*     */   private AbstractCompletionProvider commentProvider;
/*     */   
/*     */   public ShorthandCompletionCache(AbstractCompletionProvider templateProvider, AbstractCompletionProvider commentProvider) {
/*  53 */     this.shorthandCompletion = new ArrayList<>();
/*  54 */     this.commentCompletion = new ArrayList<>();
/*  55 */     this.templateProvider = templateProvider;
/*  56 */     this.commentProvider = commentProvider;
/*     */   }
/*     */   
/*     */   public void addShorthandCompletion(Completion completion) {
/*  60 */     addSorted(this.shorthandCompletion, completion);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addSorted(List<Completion> list, Completion completion) {
/*  66 */     int index = Collections.binarySearch((List)list, completion);
/*  67 */     if (index < 0)
/*     */     {
/*  69 */       index = -(index + 1);
/*     */     }
/*  71 */     list.add(index, completion);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Completion> getShorthandCompletions() {
/*  76 */     return this.shorthandCompletion;
/*     */   }
/*     */   
/*     */   public void removeShorthandCompletion(Completion completion) {
/*  80 */     this.shorthandCompletion.remove(completion);
/*     */   }
/*     */   
/*     */   public void clearCache() {
/*  84 */     this.shorthandCompletion.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCommentCompletion(Completion completion) {
/*  89 */     addSorted(this.commentCompletion, completion);
/*     */   }
/*     */   
/*     */   public List<Completion> getCommentCompletions() {
/*  93 */     return this.commentCompletion;
/*     */   }
/*     */   
/*     */   public void removeCommentCompletion(Completion completion) {
/*  97 */     this.commentCompletion.remove(completion);
/*     */   }
/*     */   
/*     */   public AbstractCompletionProvider getTemplateProvider() {
/* 101 */     return this.templateProvider;
/*     */   }
/*     */   
/*     */   public AbstractCompletionProvider getCommentProvider() {
/* 105 */     return this.commentProvider;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\ShorthandCompletionCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */