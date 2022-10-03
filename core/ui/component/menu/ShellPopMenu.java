/*    */ package core.ui.component.menu;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.awt.datatransfer.Transferable;
/*    */ import java.awt.datatransfer.UnsupportedFlavorException;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.event.MouseListener;
/*    */ import javax.swing.JMenuItem;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.JTextPane;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.Document;
/*    */ 
/*    */ 
/*    */ public class ShellPopMenu
/*    */ {
/*    */   private final JPopupMenu shellmenu;
/*    */   private final JMenuItem copy;
/*    */   private final JMenuItem paste;
/*    */   private final JPanel p;
/*    */   private final JTextPane c;
/*    */   Clipboard clipboard;
/*    */   Transferable contents;
/*    */   Transferable tText;
/*    */   DataFlavor flavor;
/*    */   private final Document shell_doc;
/*    */   
/*    */   public ShellPopMenu(JPanel panel, JTextPane console) {
/* 35 */     this.p = panel;
/* 36 */     this.c = console;
/* 37 */     this.shell_doc = console.getDocument();
/* 38 */     this.shellmenu = new JPopupMenu();
/* 39 */     this.copy = new JMenuItem("复制");
/* 40 */     this.paste = new JMenuItem("粘贴");
/* 41 */     this.shellmenu.add(this.copy);
/* 42 */     this.shellmenu.add(this.paste);
/* 43 */     this.p.add(this.shellmenu);
/* 44 */     MenuAction action = new MenuAction();
/* 45 */     this.copy.addActionListener(action);
/* 46 */     this.paste.addActionListener(action);
/* 47 */     console.addMouseListener(new MouseL());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   class MenuAction
/*    */     implements ActionListener
/*    */   {
/*    */     public void actionPerformed(ActionEvent e) {
/* 56 */       if (e.getSource() == ShellPopMenu.this.copy) {
/* 57 */         String k = ShellPopMenu.this.c.getSelectedText();
/*    */         
/* 59 */         ShellPopMenu.this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 60 */         ShellPopMenu.this.tText = new StringSelection(k);
/* 61 */         ShellPopMenu.this.clipboard.setContents(ShellPopMenu.this.tText, null);
/* 62 */       } else if (e.getSource() == ShellPopMenu.this.paste) {
/*    */ 
/*    */ 
/*    */         
/* 66 */         ShellPopMenu.this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 67 */         Transferable clipT = ShellPopMenu.this.clipboard.getContents(null);
/* 68 */         if (clipT != null)
/*    */         {
/* 70 */           if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) {
/*    */             
/*    */             try {
/* 73 */               String pastestr = (String)clipT.getTransferData(DataFlavor.stringFlavor);
/*    */               try {
/* 75 */                 ShellPopMenu.this.shell_doc.insertString(ShellPopMenu.this.shell_doc.getLength(), pastestr, null);
/* 76 */               } catch (BadLocationException e1) {
/*    */                 
/* 78 */                 e1.printStackTrace();
/*    */               } 
/* 80 */             } catch (UnsupportedFlavorException|java.io.IOException e1) {
/*    */               
/* 82 */               e1.printStackTrace();
/*    */             } 
/*    */           }
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   class MouseL
/*    */     implements MouseListener
/*    */   {
/*    */     public void mouseClicked(MouseEvent e) {
/* 96 */       if (e.isMetaDown())
/*    */       {
/* 98 */         ShellPopMenu.this.shellmenu.show(ShellPopMenu.this.c, e.getX(), e.getY());
/*    */       }
/*    */     }
/*    */     
/*    */     public void mousePressed(MouseEvent e) {}
/*    */     
/*    */     public void mouseReleased(MouseEvent e) {}
/*    */     
/*    */     public void mouseEntered(MouseEvent e) {}
/*    */     
/*    */     public void mouseExited(MouseEvent e) {}
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\menu\ShellPopMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */