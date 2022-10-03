/*    */ package core.ui.component.frame;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import core.annotation.NoI18N;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Frame;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import util.functions;
/*    */ 
/*    */ public class ImageShowFrame
/*    */   extends JFrame {
/*    */   private JPanel panel;
/*    */   @NoI18N
/*    */   private JLabel imageLabel;
/*    */   
/*    */   private ImageShowFrame(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
/* 20 */     super(title);
/*    */     
/* 22 */     this.panel = new JPanel(new BorderLayout());
/* 23 */     this.imageLabel = new JLabel(imageIcon);
/*    */ 
/*    */     
/* 26 */     this.panel.add(this.imageLabel);
/*    */     
/* 28 */     add(this.panel);
/*    */     
/* 30 */     functions.setWindowSize(this, width, height);
/* 31 */     setLocationRelativeTo(owner);
/* 32 */     EasyI18N.installObject(this);
/* 33 */     setVisible(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title, int width, int height) {
/* 38 */     width += 50;
/* 39 */     height += 50;
/* 40 */     if (title == null || title.trim().length() < 1) {
/* 41 */       title = String.format("image info Width:%s Height:%s", new Object[] { Integer.valueOf(imageIcon.getIconWidth()), Integer.valueOf(imageIcon.getIconHeight()) });
/*    */     }
/* 43 */     ImageShowFrame imageShowDialog = new ImageShowFrame(owner, imageIcon, title, width, height);
/*    */   }
/*    */   
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon, String title) {
/* 47 */     showImageDiaolog(owner, imageIcon, title, imageIcon.getIconWidth(), imageIcon.getIconHeight());
/*    */   }
/*    */   public static void showImageDiaolog(ImageIcon imageIcon, String title) {
/* 50 */     showImageDiaolog((Frame)null, imageIcon, title);
/*    */   }
/*    */   public static void showImageDiaolog(Frame owner, ImageIcon imageIcon) {
/* 53 */     showImageDiaolog(owner, imageIcon, (String)null);
/*    */   }
/*    */   public static void showImageDiaolog(ImageIcon imageIcon) {
/* 56 */     showImageDiaolog((Frame)null, imageIcon);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\frame\ImageShowFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */