/*    */ package core.ui.component.dialog;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Frame;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import util.functions;
/*    */ 
/*    */ public class ImageShowDialog extends JDialog {
/*    */   private JPanel panel;
/*    */   private JLabel imageLabel;
/*    */   
/*    */   private ImageShowDialog(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
/* 17 */     super(owner, title, true);
/*    */     
/* 19 */     this.panel = new JPanel(new BorderLayout());
/* 20 */     this.imageLabel = new JLabel(imageIcon);
/*    */ 
/*    */     
/* 23 */     this.panel.add(this.imageLabel);
/*    */     
/* 25 */     add(this.panel);
/*    */     
/* 27 */     functions.setWindowSize(this, width, height);
/* 28 */     setLocationRelativeTo(owner);
/* 29 */     EasyI18N.installObject(this);
/* 30 */     setVisible(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
/* 35 */     width += 50;
/* 36 */     height += 50;
/* 37 */     if (title == null || title.trim().length() < 1) {
/* 38 */       title = String.format("image info Width:%s Height:%s", new Object[] { Integer.valueOf(imageIcon.getIconWidth()), Integer.valueOf(imageIcon.getIconHeight()) });
/*    */     }
/* 40 */     ImageShowDialog imageShowDialog = new ImageShowDialog(owner, imageIcon, title, width, height);
/*    */   }
/*    */   
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title) {
/* 44 */     showImageDiaolog(owner, imageIcon, title, imageIcon.getIconWidth(), imageIcon.getIconHeight());
/*    */   }
/*    */   public static void showImageDiaolog(ImageIcon imageIcon, String title) {
/* 47 */     showImageDiaolog((Frame)null, imageIcon, title);
/*    */   }
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon) {
/* 50 */     showImageDiaolog(owner, imageIcon, (String)null);
/*    */   }
/*    */   public static void showImageDiaolog(ImageIcon imageIcon) {
/* 53 */     showImageDiaolog((Frame)null, imageIcon);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\ImageShowDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */