/*    */ package core.ui.component.dialog;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import java.awt.Component;
/*    */ import java.awt.HeadlessException;
/*    */ import java.io.File;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.filechooser.FileSystemView;
/*    */ 
/*    */ public class GFileChooser extends JFileChooser {
/* 11 */   private static String lastDirectory = (new File("")).getPath();
/*    */   public GFileChooser() {
/* 13 */     super(lastDirectory);
/*    */   }
/*    */   
/*    */   public GFileChooser(FileSystemView fsv) {
/* 17 */     super(lastDirectory, fsv);
/*    */   }
/*    */ 
/*    */   
/*    */   public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
/* 22 */     int flag = super.showDialog(parent, EasyI18N.getI18nString(approveButtonText));
/* 23 */     if (flag == 0) {
/* 24 */       lastDirectory = getSelectedFile().getAbsolutePath();
/*    */     }
/* 26 */     return flag;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\GFileChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */