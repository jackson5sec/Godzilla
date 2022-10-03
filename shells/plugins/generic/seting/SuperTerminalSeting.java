/*     */ package shells.plugins.generic.seting;
/*     */ 
/*     */ import com.jediterm.terminal.ui.JediTermWidget;
/*     */ import com.jediterm.terminal.ui.settings.SettingsProvider;
/*     */ import core.Db;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ 
/*     */ 
/*     */ public class SuperTerminalSeting
/*     */   extends JPanel
/*     */ {
/*     */   private static final String SHOW_STR = "你好 hello ❤❤❤";
/*     */   private JLabel fontLabel;
/*     */   private JLabel sizeLabel;
/*     */   private JLabel fontTypeLabel;
/*     */   private JLabel terminalStyleLabel;
/*     */   
/*     */   public SuperTerminalSeting() {
/*  34 */     super(new BorderLayout());
/*  35 */     this.fontLabel = new JLabel("字体: ");
/*  36 */     this.sizeLabel = new JLabel("字体大小: ");
/*  37 */     this.fontTypeLabel = new JLabel("字体类型 : ");
/*  38 */     this.terminalStyleLabel = new JLabel("终端配色: ");
/*     */     
/*  40 */     this.fontCombobox = new JComboBox<>(UiFunction.getAllFontName());
/*  41 */     this.fontSizeCombobox = new JComboBox((Object[])UiFunction.getAllFontSize());
/*  42 */     this.fontTypeComboBox = new JComboBox<>(UiFunction.getAllFontType());
/*  43 */     this.terminalStyleComboBox = new JComboBox<>(TerminalSettingsProvider.getTerminalStyles());
/*  44 */     this.saveButton = new JButton("保存配置");
/*     */     
/*  46 */     this.fontCombobox.setSelectedItem(TerminalSettingsProvider.getFontName());
/*  47 */     this.fontTypeComboBox.setSelectedItem(TerminalSettingsProvider.getFontType());
/*  48 */     this.fontSizeCombobox.setSelectedItem(String.valueOf(TerminalSettingsProvider.getFontSize()));
/*  49 */     this.terminalStyleComboBox.setSelectedItem(TerminalSettingsProvider.getTerminalStyle());
/*     */     
/*  51 */     this.jediTerminal = new JediTermWidget((SettingsProvider)new TerminalSettingsProvider());
/*     */     
/*  53 */     this.jediTerminal.getTerminal().writeCharacters("你好 hello ❤❤❤");
/*  54 */     this.jediTerminal.getTerminal().nextLine();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     JPanel topPanel = new JPanel();
/*     */ 
/*     */     
/*  63 */     topPanel.add(this.fontLabel);
/*  64 */     topPanel.add(this.fontCombobox);
/*  65 */     topPanel.add(this.fontTypeLabel);
/*  66 */     topPanel.add(this.fontTypeComboBox);
/*  67 */     topPanel.add(this.sizeLabel);
/*  68 */     topPanel.add(this.fontSizeCombobox);
/*  69 */     topPanel.add(this.terminalStyleLabel);
/*  70 */     topPanel.add(this.terminalStyleComboBox);
/*  71 */     topPanel.add(this.saveButton);
/*     */     
/*  73 */     JSplitPane splitPane = new JSplitPane(0);
/*     */     
/*  75 */     splitPane.setTopComponent(topPanel);
/*     */     
/*  77 */     splitPane.setBottomComponent((Component)this.jediTerminal);
/*     */     
/*  79 */     ItemListener listener = e -> {
/*     */         this.jediTerminal = new JediTermWidget((SettingsProvider)new TerminalSettingsProvider(this.terminalStyleComboBox.getSelectedItem().toString())
/*     */             {
/*     */               public Font getTerminalFont() {
/*  83 */                 return new Font(SuperTerminalSeting.this.fontCombobox.getSelectedItem().toString(), UiFunction.getFontType(SuperTerminalSeting.this.fontTypeComboBox.getSelectedItem().toString()), (int)getTerminalFontSize());
/*     */               }
/*     */ 
/*     */               
/*     */               public float getTerminalFontSize() {
/*  88 */                 return Integer.parseInt(SuperTerminalSeting.this.fontSizeCombobox.getSelectedItem().toString());
/*     */               }
/*     */             });
/*     */         
/*     */         try {
/*     */           this.jediTerminal.getTerminal().writeCharacters("你好 hello ❤❤❤");
/*     */           
/*     */           this.jediTerminal.setSize(1024, 1024);
/*  96 */         } catch (Exception e2) {
/*     */           Log.error(e2);
/*     */         } 
/*     */         
/*     */         splitPane.setBottomComponent((Component)this.jediTerminal);
/*     */       };
/* 102 */     this.fontCombobox.addItemListener(listener);
/* 103 */     this.fontTypeComboBox.addItemListener(listener);
/* 104 */     this.fontSizeCombobox.addItemListener(listener);
/* 105 */     this.terminalStyleComboBox.addItemListener(listener);
/*     */     
/* 107 */     add(splitPane);
/*     */     
/* 109 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */   private JComboBox<String> fontCombobox; private JComboBox<Integer> fontSizeCombobox; private JComboBox<String> fontTypeComboBox; private JComboBox<String> terminalStyleComboBox; private JButton saveButton;
/*     */   private JediTermWidget jediTerminal;
/*     */   
/*     */   private void saveButtonClick(ActionEvent actionEvent) {
/* 115 */     String fontName = this.fontCombobox.getSelectedItem().toString();
/* 116 */     String fontType = this.fontTypeComboBox.getSelectedItem().toString();
/* 117 */     int fontSize = Integer.parseInt(this.fontSizeCombobox.getSelectedItem().toString());
/* 118 */     String terminalStyle = this.terminalStyleComboBox.getSelectedItem().toString();
/* 119 */     if (Db.updateSetingKV("Terminal-FontName", fontName) && Db.updateSetingKV("Terminal-FontType", fontType) && Db.updateSetingKV("Terminal-FontSize", String.valueOf(fontSize)) && Db.updateSetingKV("Terminal-FontStyle", terminalStyle)) {
/* 120 */       GOptionPane.showMessageDialog(this, "修改成功!", "提示", 1);
/*     */     } else {
/* 122 */       GOptionPane.showMessageDialog(this, "修改失败!", "提示", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 127 */     JFrame frame = new JFrame();
/*     */     
/* 129 */     frame.add(new SuperTerminalSeting());
/*     */ 
/*     */     
/* 132 */     frame.setSize(1200, 1200);
/*     */     
/* 134 */     frame.setVisible(true);
/* 135 */     frame.setDefaultCloseOperation(3);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\seting\SuperTerminalSeting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */