/*    */ package core.ui.component;
/*    */ 
/*    */ import core.Db;
/*    */ import core.annotation.DisplayName;
/*    */ import core.shell.ShellEntity;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JPanel;
/*    */ import org.fife.ui.rtextarea.RTextArea;
/*    */ import org.fife.ui.rtextarea.RTextScrollPane;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ @DisplayName(DisplayName = "笔记")
/*    */ public class ShellNote
/*    */   extends JPanel
/*    */ {
/*    */   private final ShellEntity shellEntity;
/*    */   private String noteData;
/*    */   private final String shellId;
/*    */   private final String lastNoteMd5;
/*    */   private final RTextArea textArea;
/*    */   private boolean state;
/*    */   
/*    */   public ShellNote(ShellEntity entity) {
/* 27 */     this.shellEntity = entity;
/* 28 */     this.shellId = this.shellEntity.getId();
/* 29 */     setLayout(new BorderLayout(1, 1));
/* 30 */     String noteData = Db.getShellNote(this.shellId);
/* 31 */     this.lastNoteMd5 = functions.md5(noteData);
/* 32 */     this.textArea = new RTextArea();
/* 33 */     this.textArea.setText(noteData);
/* 34 */     this.state = true;
/* 35 */     Thread thread = new Thread(new Runnable()
/*    */         {
/*    */           public void run()
/*    */           {
/* 39 */             while (ShellNote.this.state) {
/*    */               try {
/* 41 */                 Thread.sleep(3000L);
/* 42 */                 ShellNote.this.updateDbNote();
/* 43 */               } catch (InterruptedException e) {
/* 44 */                 Log.error(e);
/*    */               } 
/*    */             } 
/*    */           }
/*    */         });
/*    */     
/* 50 */     thread.start();
/* 51 */     RTextScrollPane scrollPane = new RTextScrollPane((RTextArea)this.textArea, true);
/* 52 */     scrollPane.setIconRowHeaderEnabled(true);
/* 53 */     scrollPane.getGutter().setBookmarkingEnabled(true);
/* 54 */     add((Component)scrollPane);
/* 55 */     this.textArea.registerReplaceDialog();
/*    */   }
/*    */   public void updateDbNote() {
/* 58 */     String noteData = this.textArea.getText();
/* 59 */     String md5 = functions.md5(noteData);
/* 60 */     if (!this.lastNoteMd5.equals(md5)) {
/* 61 */       Db.updateShellNote(this.shellId, noteData);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void disable() {
/* 67 */     this.state = false;
/* 68 */     super.disable();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */