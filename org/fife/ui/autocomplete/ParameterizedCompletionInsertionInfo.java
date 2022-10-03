/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.text.Position;
/*     */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
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
/*     */ public class ParameterizedCompletionInsertionInfo
/*     */ {
/*     */   private int minOffs;
/*     */   private Position maxOffs;
/*  39 */   private int defaultEnd = -1;
/*     */   private int selStart;
/*     */   private int selEnd;
/*     */   
/*     */   public void addReplacementCopy(String id, int start, int end) {
/*  44 */     if (this.replacementCopies == null) {
/*  45 */       this.replacementCopies = new ArrayList<>(1);
/*     */     }
/*  47 */     this.replacementCopies.add(new ReplacementCopy(id, start, end));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String textToInsert;
/*     */ 
/*     */   
/*     */   private List<DocumentRange> replacementLocations;
/*     */   
/*     */   private List<ReplacementCopy> replacementCopies;
/*     */ 
/*     */   
/*     */   public void addReplacementLocation(int start, int end) {
/*  61 */     if (this.replacementLocations == null) {
/*  62 */       this.replacementLocations = new ArrayList<>(1);
/*     */     }
/*  64 */     this.replacementLocations.add(new DocumentRange(start, end));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultEndOffs() {
/*  69 */     return (this.defaultEnd > -1) ? this.defaultEnd : getMaxOffset().getOffset();
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
/*     */   public Position getMaxOffset() {
/*  81 */     return this.maxOffs;
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
/*     */   public int getMinOffset() {
/*  93 */     return this.minOffs;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReplacementCopyCount() {
/*  98 */     return (this.replacementCopies == null) ? 0 : this.replacementCopies.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReplacementCount() {
/* 108 */     return (this.replacementLocations == null) ? 0 : this.replacementLocations.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public ReplacementCopy getReplacementCopy(int index) {
/* 113 */     return this.replacementCopies.get(index);
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
/*     */   public DocumentRange getReplacementLocation(int index) {
/* 126 */     return this.replacementLocations.get(index);
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
/*     */   public int getSelectionEnd() {
/* 139 */     return this.selEnd;
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
/*     */   public int getSelectionStart() {
/* 152 */     return this.selStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTextToInsert() {
/* 163 */     return this.textToInsert;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelection() {
/* 174 */     return (this.selEnd != this.selStart);
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
/*     */   public void setInitialSelection(int selStart, int selEnd) {
/* 187 */     this.selStart = selStart;
/* 188 */     this.selEnd = selEnd;
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
/*     */   public void setCaretRange(int minOffs, Position maxOffs) {
/* 203 */     this.minOffs = minOffs;
/* 204 */     this.maxOffs = maxOffs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultEndOffs(int end) {
/* 209 */     this.defaultEnd = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextToInsert(String text) {
/* 220 */     this.textToInsert = text;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ReplacementCopy
/*     */   {
/*     */     private String id;
/*     */     
/*     */     private int start;
/*     */     
/*     */     private int end;
/*     */ 
/*     */     
/*     */     ReplacementCopy(String id, int start, int end) {
/* 234 */       this.id = id;
/* 235 */       this.start = start;
/* 236 */       this.end = end;
/*     */     }
/*     */     
/*     */     public int getEnd() {
/* 240 */       return this.end;
/*     */     }
/*     */     
/*     */     public String getId() {
/* 244 */       return this.id;
/*     */     }
/*     */     
/*     */     public int getStart() {
/* 248 */       return this.start;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ParameterizedCompletionInsertionInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */