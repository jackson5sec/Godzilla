/*     */ package org.fife.rsta.ac.demo;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import org.fife.rsta.ac.AbstractSourceTree;
/*     */ import org.fife.rsta.ac.LanguageSupport;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.java.JavaLanguageSupport;
/*     */ import org.fife.rsta.ac.java.tree.JavaOutlineTree;
/*     */ import org.fife.rsta.ac.js.tree.JavaScriptOutlineTree;
/*     */ import org.fife.rsta.ac.xml.tree.XmlOutlineTree;
/*     */ import org.fife.ui.rsyntaxtextarea.ErrorStrip;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.RTextScrollPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DemoRootPane
/*     */   extends JRootPane
/*     */   implements HyperlinkListener, SyntaxConstants, Actions
/*     */ {
/*     */   private JScrollPane treeSP;
/*     */   private AbstractSourceTree tree;
/*     */   private RSyntaxTextArea textArea;
/*     */   
/*     */   public DemoRootPane() {
/*  73 */     LanguageSupportFactory lsf = LanguageSupportFactory.get();
/*  74 */     LanguageSupport support = lsf.getSupportFor("text/java");
/*  75 */     JavaLanguageSupport jls = (JavaLanguageSupport)support;
/*     */ 
/*     */     
/*     */     try {
/*  79 */       jls.getJarManager().addCurrentJreClassFileSource();
/*  80 */     } catch (IOException ioe) {
/*  81 */       ioe.printStackTrace();
/*     */     } 
/*     */ 
/*     */     
/*  85 */     JTree dummy = new JTree((TreeNode)null);
/*  86 */     this.treeSP = new JScrollPane(dummy);
/*     */     
/*  88 */     this.textArea = createTextArea();
/*  89 */     setText("CExample.txt", "text/c");
/*  90 */     RTextScrollPane scrollPane = new RTextScrollPane((RTextArea)this.textArea, true);
/*  91 */     scrollPane.setIconRowHeaderEnabled(true);
/*  92 */     scrollPane.getGutter().setBookmarkingEnabled(true);
/*     */     
/*  94 */     JSplitPane sp = new JSplitPane(1, this.treeSP, (Component)scrollPane);
/*     */     
/*  96 */     SwingUtilities.invokeLater(() -> sp.setDividerLocation(0.25D));
/*  97 */     sp.setContinuousLayout(true);
/*     */ 
/*     */     
/* 100 */     setJMenuBar(createMenuBar());
/*     */     
/* 102 */     ErrorStrip errorStrip = new ErrorStrip(this.textArea);
/*     */     
/* 104 */     JPanel cp = new JPanel(new BorderLayout());
/* 105 */     cp.add(sp);
/* 106 */     cp.add((Component)errorStrip, "After");
/* 107 */     setContentPane(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addItem(Action a, ButtonGroup bg, JMenu menu) {
/* 112 */     JRadioButtonMenuItem item = new JRadioButtonMenuItem(a);
/* 113 */     bg.add(item);
/* 114 */     menu.add(item);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private JMenuBar createMenuBar() {
/* 120 */     JMenuBar mb = new JMenuBar();
/*     */     
/* 122 */     JMenu menu = new JMenu("File");
/* 123 */     menu.add(new JMenuItem(new Actions.OpenAction(this)));
/* 124 */     menu.addSeparator();
/* 125 */     menu.add(new JMenuItem(new Actions.ExitAction()));
/* 126 */     mb.add(menu);
/*     */     
/* 128 */     menu = new JMenu("Language");
/* 129 */     ButtonGroup bg = new ButtonGroup();
/* 130 */     addItem(new Actions.StyleAction(this, "C", "CExample.txt", "text/c"), bg, menu);
/* 131 */     addItem(new Actions.StyleAction(this, "CSS", "CssExample.txt", "text/css"), bg, menu);
/* 132 */     addItem(new Actions.StyleAction(this, "Groovy", "GroovyExample.txt", "text/groovy"), bg, menu);
/* 133 */     addItem(new Actions.StyleAction(this, "Java", "JavaExample.txt", "text/java"), bg, menu);
/* 134 */     addItem(new Actions.StyleAction(this, "JavaScript", "JSExample.txt", "text/javascript"), bg, menu);
/* 135 */     addItem(new Actions.StyleAction(this, "JSP", "JspExample.txt", "text/jsp"), bg, menu);
/* 136 */     addItem(new Actions.StyleAction(this, "Less", "LessExample.txt", "text/less"), bg, menu);
/* 137 */     addItem(new Actions.StyleAction(this, "Perl", "PerlExample.txt", "text/perl"), bg, menu);
/* 138 */     addItem(new Actions.StyleAction(this, "HTML", "HtmlExample.txt", "text/html"), bg, menu);
/* 139 */     addItem(new Actions.StyleAction(this, "PHP", "PhpExample.txt", "text/php"), bg, menu);
/* 140 */     addItem(new Actions.StyleAction(this, "sh", "ShellExample.txt", "text/unix"), bg, menu);
/* 141 */     addItem(new Actions.StyleAction(this, "TypeScript", "TypeScriptExample.txt", "text/typescript"), bg, menu);
/* 142 */     addItem(new Actions.StyleAction(this, "XML", "XMLExample.txt", "text/xml"), bg, menu);
/* 143 */     menu.getItem(0).setSelected(true);
/* 144 */     mb.add(menu);
/*     */     
/* 146 */     menu = new JMenu("LookAndFeel");
/* 147 */     bg = new ButtonGroup();
/* 148 */     UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
/* 149 */     for (UIManager.LookAndFeelInfo info : infos) {
/* 150 */       addItem(new Actions.LookAndFeelAction(this, info), bg, menu);
/*     */     }
/* 152 */     mb.add(menu);
/*     */     
/* 154 */     menu = new JMenu("View");
/* 155 */     menu.add(new JCheckBoxMenuItem(new Actions.ToggleLayeredHighlightsAction(this)));
/* 156 */     mb.add(menu);
/*     */     
/* 158 */     menu = new JMenu("Help");
/* 159 */     menu.add(new JMenuItem(new Actions.AboutAction(this)));
/* 160 */     mb.add(menu);
/*     */     
/* 162 */     return mb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RSyntaxTextArea createTextArea() {
/* 173 */     RSyntaxTextArea textArea = new RSyntaxTextArea(25, 80);
/* 174 */     LanguageSupportFactory.get().register(textArea);
/* 175 */     textArea.setCaretPosition(0);
/* 176 */     textArea.addHyperlinkListener(this);
/* 177 */     textArea.requestFocusInWindow();
/* 178 */     textArea.setMarkOccurrences(true);
/* 179 */     textArea.setCodeFoldingEnabled(true);
/* 180 */     textArea.setTabsEmulated(true);
/* 181 */     textArea.setTabSize(3);
/*     */ 
/*     */ 
/*     */     
/* 185 */     ToolTipManager.sharedInstance().registerComponent((JComponent)textArea);
/* 186 */     return textArea;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void focusTextArea() {
/* 194 */     this.textArea.requestFocusInWindow();
/*     */   }
/*     */ 
/*     */   
/*     */   RSyntaxTextArea getTextArea() {
/* 199 */     return this.textArea;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void hyperlinkUpdate(HyperlinkEvent e) {
/* 210 */     if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
/* 211 */       URL url = e.getURL();
/* 212 */       if (url == null) {
/* 213 */         UIManager.getLookAndFeel().provideErrorFeedback(null);
/*     */       } else {
/*     */         
/* 216 */         JOptionPane.showMessageDialog(this, "URL clicked:\n" + url
/* 217 */             .toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void openFile(File file) {
/*     */     try {
/* 231 */       BufferedReader r = new BufferedReader(new FileReader(file));
/* 232 */       this.textArea.read(r, null);
/* 233 */       this.textArea.setCaretPosition(0);
/* 234 */       r.close();
/* 235 */     } catch (IOException ioe) {
/* 236 */       ioe.printStackTrace();
/* 237 */       UIManager.getLookAndFeel().provideErrorFeedback(this);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void refreshSourceTree() {
/* 249 */     if (this.tree != null) {
/* 250 */       this.tree.uninstall();
/*     */     }
/*     */     
/* 253 */     String language = this.textArea.getSyntaxEditingStyle();
/* 254 */     if ("text/java".equals(language)) {
/* 255 */       this.tree = (AbstractSourceTree)new JavaOutlineTree();
/*     */     }
/* 257 */     else if ("text/javascript".equals(language)) {
/* 258 */       this.tree = (AbstractSourceTree)new JavaScriptOutlineTree();
/*     */     }
/* 260 */     else if ("text/xml".equals(language)) {
/* 261 */       this.tree = (AbstractSourceTree)new XmlOutlineTree();
/*     */     } else {
/*     */       
/* 264 */       this.tree = null;
/*     */     } 
/*     */     
/* 267 */     if (this.tree != null) {
/* 268 */       this.tree.listenTo(this.textArea);
/* 269 */       this.treeSP.setViewportView((Component)this.tree);
/*     */     } else {
/*     */       
/* 272 */       JTree dummy = new JTree((TreeNode)null);
/* 273 */       this.treeSP.setViewportView(dummy);
/*     */     } 
/* 275 */     this.treeSP.revalidate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setText(String resource, String style) {
/* 288 */     this.textArea.setSyntaxEditingStyle(style);
/*     */     
/* 290 */     ClassLoader cl = getClass().getClassLoader();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 295 */       BufferedReader r = new BufferedReader(new InputStreamReader(cl.getResourceAsStream("examples/" + resource), StandardCharsets.UTF_8));
/* 296 */       this.textArea.read(r, null);
/* 297 */       r.close();
/* 298 */       this.textArea.setCaretPosition(0);
/* 299 */       this.textArea.discardAllEdits();
/*     */       
/* 301 */       refreshSourceTree();
/*     */     }
/* 303 */     catch (RuntimeException re) {
/* 304 */       throw re;
/* 305 */     } catch (Exception e) {
/* 306 */       this.textArea.setText("Type here to see syntax highlighting");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\DemoRootPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */