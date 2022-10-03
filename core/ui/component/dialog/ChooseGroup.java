/*     */ package core.ui.component.dialog;
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import com.intellij.uiDesigner.core.Spacer;
/*     */ import core.EasyI18N;
/*     */ import core.ui.component.ShellGroup;
/*     */ import java.awt.Component;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import util.functions;
/*     */ 
/*     */ public class ChooseGroup extends JDialog {
/*     */   private JPanel contentPane;
/*     */   private JButton buttonOK;
/*     */   private JButton buttonCancel;
/*     */   
/*     */   public ChooseGroup(Window parentWindow, String defaultGroup) {
/*  27 */     this.parentWindow = parentWindow;
/*     */     
/*  29 */     $$$setupUI$$$();
/*     */     
/*  31 */     this.groupTree.setSelectNote(defaultGroup);
/*  32 */     setContentPane(this.contentPane);
/*  33 */     setModal(true);
/*  34 */     getRootPane().setDefaultButton(this.buttonOK);
/*     */     
/*  36 */     this.buttonOK.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  38 */             ChooseGroup.this.onOK();
/*     */           }
/*     */         });
/*     */     
/*  42 */     this.buttonCancel.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  44 */             ChooseGroup.this.onCancel();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  49 */     setDefaultCloseOperation(0);
/*  50 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent e) {
/*  52 */             ChooseGroup.this.onCancel();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  57 */     this.contentPane.registerKeyboardAction(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent e) {
/*  59 */             ChooseGroup.this.onCancel();
/*     */           }
/*  61 */         },  KeyStroke.getKeyStroke(27, 0), 1);
/*     */     
/*  63 */     EasyI18N.installObject(this);
/*     */   }
/*     */   public JPanel groupPanel; public ShellGroup groupTree; private String groupId; private Window parentWindow;
/*     */   private void onOK() {
/*  67 */     this.groupId = this.groupTree.getSelectedGroupName();
/*  68 */     if (this.groupId.isEmpty()) {
/*  69 */       this.groupId = null;
/*  70 */       GOptionPane.showMessageDialog(UiFunction.getParentWindow((Container)this.groupTree), "未选中组!");
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     dispose();
/*     */   }
/*     */   
/*     */   private void onCancel() {
/*  78 */     this.groupId = null;
/*     */     
/*  80 */     dispose();
/*     */   }
/*     */   
/*     */   public String getChooseGroup() {
/*  84 */     setTitle("选择分组");
/*  85 */     pack();
/*  86 */     functions.setWindowSize(this, 600, 630);
/*  87 */     setLocationRelativeTo(this.parentWindow);
/*  88 */     EasyI18N.installObject(this);
/*  89 */     setVisible(true);
/*  90 */     return this.groupId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void $$$setupUI$$$() {
/* 101 */     this.contentPane = new JPanel();
/* 102 */     this.contentPane.setLayout((LayoutManager)new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
/* 103 */     JPanel panel1 = new JPanel();
/* 104 */     panel1.setLayout((LayoutManager)new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
/* 105 */     this.contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, 0, 3, 3, 1, null, null, null, 0, false));
/* 106 */     Spacer spacer1 = new Spacer();
/* 107 */     panel1.add((Component)spacer1, new GridConstraints(0, 0, 1, 1, 0, 1, 4, 1, null, null, null, 0, false));
/* 108 */     JPanel panel2 = new JPanel();
/* 109 */     panel2.setLayout((LayoutManager)new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
/* 110 */     panel1.add(panel2, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 111 */     this.buttonOK = new JButton();
/* 112 */     this.buttonOK.setText("OK");
/* 113 */     panel2.add(this.buttonOK, new GridConstraints(0, 0, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 114 */     this.buttonCancel = new JButton();
/* 115 */     this.buttonCancel.setText("Cancel");
/* 116 */     panel2.add(this.buttonCancel, new GridConstraints(0, 1, 1, 1, 0, 1, 3, 0, null, null, null, 0, false));
/* 117 */     this.groupPanel = new JPanel();
/* 118 */     this.groupPanel.setLayout((LayoutManager)new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
/* 119 */     this.contentPane.add(this.groupPanel, new GridConstraints(0, 0, 1, 1, 0, 3, 3, 3, null, null, null, 0, false));
/* 120 */     JScrollPane scrollPane1 = new JScrollPane();
/* 121 */     this.groupPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, 0, 3, 5, 5, null, null, null, 0, false));
/* 122 */     this.groupTree = new ShellGroup();
/* 123 */     scrollPane1.setViewportView((Component)this.groupTree);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent $$$getRootComponent$$$() {
/* 130 */     return this.contentPane;
/*     */   }
/*     */   
/*     */   private void createUIComponents() {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\ChooseGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */