/*    */ package core.ui.component.dialog;
/*    */ import core.EasyI18N;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JProgressBar;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ 
/*    */ public class HttpProgressBar extends JFrame {
/* 12 */   private static final String CURRENT_VALUE_FORMAT = EasyI18N.getI18nString("已完成  %s Mb");
/* 13 */   private static final String MAX_VALUE_FORMAT = EasyI18N.getI18nString("共  %s Mb");
/*    */   
/*    */   private final JPanel panel;
/*    */   
/*    */   private final JLabel currentValueLabel;
/*    */   private final JLabel maxValueLabel;
/*    */   private final JProgressBar progressBar;
/*    */   private boolean isClose;
/*    */   
/*    */   public HttpProgressBar(String title, int MaxValue) {
/* 23 */     this.panel = new JPanel();
/* 24 */     this.currentValueLabel = new JLabel();
/* 25 */     this.maxValueLabel = new JLabel();
/* 26 */     this.progressBar = new JProgressBar(0, 0, MaxValue);
/*    */     
/* 28 */     this.panel.add(this.progressBar);
/* 29 */     this.panel.add(this.maxValueLabel);
/* 30 */     this.panel.add(this.currentValueLabel);
/*    */     
/* 32 */     this.maxValueLabel.setText(String.format(MAX_VALUE_FORMAT, new Object[] { String.format("%.4f", new Object[] { Float.valueOf(MaxValue / Float.valueOf(1048576.0F).floatValue()) }) }));
/* 33 */     this.currentValueLabel.setText(String.format(CURRENT_VALUE_FORMAT, new Object[] { Integer.valueOf(0) }));
/*    */ 
/*    */ 
/*    */     
/* 37 */     add(this.panel);
/*    */     
/* 39 */     setTitle(title);
/* 40 */     this.progressBar.setStringPainted(true);
/*    */     
/* 42 */     setDefaultCloseOperation(2);
/* 43 */     setLocationRelativeTo((Component)null);
/* 44 */     functions.setWindowSize(this, 430, 90);
/*    */     
/* 46 */     this.progressBar.updateUI();
/*    */     
/* 48 */     EasyI18N.installObject(this);
/*    */     
/* 50 */     setVisible(true);
/*    */   }
/*    */   public void setValue(int value) {
/* 53 */     this.progressBar.setValue(value);
/* 54 */     this.currentValueLabel.setText(String.format(CURRENT_VALUE_FORMAT, new Object[] { String.format("%.4f", new Object[] { Float.valueOf(this.progressBar.getValue() / Float.valueOf(1048576.0F).floatValue()) }) }));
/* 55 */     Log.log(this.maxValueLabel.getText() + "\t" + this.currentValueLabel.getText(), new Object[0]);
/* 56 */     if (this.progressBar.getMaximum() <= this.progressBar.getValue()) {
/* 57 */       close();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isClose() {
/* 62 */     return this.isClose;
/*    */   }
/*    */   
/*    */   public void close() {
/* 66 */     this.isClose = true;
/* 67 */     dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 72 */     this.isClose = true;
/* 73 */     super.dispose();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\HttpProgressBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */