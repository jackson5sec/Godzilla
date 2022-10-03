/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.UndoableEditEvent;
/*     */ import javax.swing.undo.CompoundEdit;
/*     */ import javax.swing.undo.UndoManager;
/*     */ import javax.swing.undo.UndoableEdit;
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
/*     */ public class RUndoManager
/*     */   extends UndoManager
/*     */ {
/*     */   private RCompoundEdit compoundEdit;
/*     */   private RTextArea textArea;
/*     */   private int lastOffset;
/*     */   private String cantUndoText;
/*     */   private String cantRedoText;
/*     */   private int internalAtomicEditDepth;
/*     */   private static final String MSG = "org.fife.ui.rtextarea.RTextArea";
/*     */   
/*     */   public RUndoManager(RTextArea textArea) {
/*  51 */     this.textArea = textArea;
/*  52 */     ResourceBundle msg = ResourceBundle.getBundle("org.fife.ui.rtextarea.RTextArea");
/*  53 */     this.cantUndoText = msg.getString("Action.CantUndo.Name");
/*  54 */     this.cantRedoText = msg.getString("Action.CantRedo.Name");
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
/*     */   public void beginInternalAtomicEdit() {
/*  68 */     if (++this.internalAtomicEditDepth == 1) {
/*  69 */       if (this.compoundEdit != null) {
/*  70 */         this.compoundEdit.end();
/*     */       }
/*  72 */       this.compoundEdit = new RCompoundEdit();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endInternalAtomicEdit() {
/*  83 */     if (this.internalAtomicEditDepth > 0 && --this.internalAtomicEditDepth == 0) {
/*  84 */       addEdit(this.compoundEdit);
/*  85 */       this.compoundEdit.end();
/*  86 */       this.compoundEdit = null;
/*  87 */       updateActions();
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
/*     */   public String getCantRedoText() {
/*  99 */     return this.cantRedoText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCantUndoText() {
/* 110 */     return this.cantUndoText;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void redo() {
/* 116 */     super.redo();
/* 117 */     updateActions();
/*     */   }
/*     */ 
/*     */   
/*     */   private RCompoundEdit startCompoundEdit(UndoableEdit edit) {
/* 122 */     this.lastOffset = this.textArea.getCaretPosition();
/* 123 */     this.compoundEdit = new RCompoundEdit();
/* 124 */     this.compoundEdit.addEdit(edit);
/* 125 */     addEdit(this.compoundEdit);
/* 126 */     return this.compoundEdit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void undo() {
/* 132 */     super.undo();
/* 133 */     updateActions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void undoableEditHappened(UndoableEditEvent e) {
/* 142 */     if (this.compoundEdit == null) {
/* 143 */       this.compoundEdit = startCompoundEdit(e.getEdit());
/* 144 */       updateActions();
/*     */       
/*     */       return;
/*     */     } 
/* 148 */     if (this.internalAtomicEditDepth > 0) {
/* 149 */       this.compoundEdit.addEdit(e.getEdit());
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 158 */     int diff = this.textArea.getCaretPosition() - this.lastOffset;
/*     */ 
/*     */     
/* 161 */     if (Math.abs(diff) <= 1) {
/* 162 */       this.compoundEdit.addEdit(e.getEdit());
/* 163 */       this.lastOffset += diff;
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 172 */     this.compoundEdit.end();
/* 173 */     this.compoundEdit = startCompoundEdit(e.getEdit());
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
/*     */   public void updateActions() {
/* 187 */     Action a = RTextArea.getAction(6);
/* 188 */     if (canUndo()) {
/* 189 */       a.setEnabled(true);
/* 190 */       String text = getUndoPresentationName();
/* 191 */       a.putValue("Name", text);
/* 192 */       a.putValue("ShortDescription", text);
/*     */     
/*     */     }
/* 195 */     else if (a.isEnabled()) {
/* 196 */       a.setEnabled(false);
/* 197 */       String text = this.cantUndoText;
/* 198 */       a.putValue("Name", text);
/* 199 */       a.putValue("ShortDescription", text);
/*     */     } 
/*     */ 
/*     */     
/* 203 */     a = RTextArea.getAction(4);
/* 204 */     if (canRedo()) {
/* 205 */       a.setEnabled(true);
/* 206 */       String text = getRedoPresentationName();
/* 207 */       a.putValue("Name", text);
/* 208 */       a.putValue("ShortDescription", text);
/*     */     
/*     */     }
/* 211 */     else if (a.isEnabled()) {
/* 212 */       a.setEnabled(false);
/* 213 */       String text = this.cantRedoText;
/* 214 */       a.putValue("Name", text);
/* 215 */       a.putValue("ShortDescription", text);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class RCompoundEdit
/*     */     extends CompoundEdit
/*     */   {
/*     */     public String getUndoPresentationName() {
/* 228 */       return UIManager.getString("AbstractUndoableEdit.undoText");
/*     */     }
/*     */ 
/*     */     
/*     */     public String getRedoPresentationName() {
/* 233 */       return UIManager.getString("AbstractUndoableEdit.redoText");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInProgress() {
/* 238 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void undo() {
/* 243 */       if (RUndoManager.this.compoundEdit != null) {
/* 244 */         RUndoManager.this.compoundEdit.end();
/*     */       }
/* 246 */       super.undo();
/* 247 */       RUndoManager.this.compoundEdit = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RUndoManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */