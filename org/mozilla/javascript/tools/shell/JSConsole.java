/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import org.mozilla.javascript.SecurityUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSConsole
/*     */   extends JFrame
/*     */   implements ActionListener
/*     */ {
/*     */   static final long serialVersionUID = 2551225560631876300L;
/*     */   private File CWD;
/*     */   private JFileChooser dlg;
/*     */   private ConsoleTextArea consoleTextArea;
/*     */   
/*     */   public String chooseFile() {
/*  36 */     if (this.CWD == null) {
/*  37 */       String dir = SecurityUtilities.getSystemProperty("user.dir");
/*  38 */       if (dir != null) {
/*  39 */         this.CWD = new File(dir);
/*     */       }
/*     */     } 
/*  42 */     if (this.CWD != null) {
/*  43 */       this.dlg.setCurrentDirectory(this.CWD);
/*     */     }
/*  45 */     this.dlg.setDialogTitle("Select a file to load");
/*  46 */     int returnVal = this.dlg.showOpenDialog(this);
/*  47 */     if (returnVal == 0) {
/*  48 */       String result = this.dlg.getSelectedFile().getPath();
/*  49 */       this.CWD = new File(this.dlg.getSelectedFile().getParent());
/*  50 */       return result;
/*     */     } 
/*  52 */     return null;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  56 */     new JSConsole(args);
/*     */   }
/*     */   
/*     */   public void createFileChooser() {
/*  60 */     this.dlg = new JFileChooser();
/*  61 */     FileFilter filter = new FileFilter()
/*     */       {
/*     */         public boolean accept(File f)
/*     */         {
/*  65 */           if (f.isDirectory()) {
/*  66 */             return true;
/*     */           }
/*  68 */           String name = f.getName();
/*  69 */           int i = name.lastIndexOf('.');
/*  70 */           if (i > 0 && i < name.length() - 1) {
/*  71 */             String ext = name.substring(i + 1).toLowerCase();
/*  72 */             if (ext.equals("js")) {
/*  73 */               return true;
/*     */             }
/*     */           } 
/*  76 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public String getDescription() {
/*  81 */           return "JavaScript Files (*.js)";
/*     */         }
/*     */       };
/*  84 */     this.dlg.addChoosableFileFilter(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSConsole(String[] args) {
/*  89 */     super("Rhino JavaScript Console");
/*  90 */     JMenuBar menubar = new JMenuBar();
/*  91 */     createFileChooser();
/*  92 */     String[] fileItems = { "Load...", "Exit" };
/*  93 */     String[] fileCmds = { "Load", "Exit" };
/*  94 */     char[] fileShortCuts = { 'L', 'X' };
/*  95 */     String[] editItems = { "Cut", "Copy", "Paste" };
/*  96 */     char[] editShortCuts = { 'T', 'C', 'P' };
/*  97 */     String[] plafItems = { "Metal", "Windows", "Motif" };
/*  98 */     boolean[] plafState = { true, false, false };
/*  99 */     JMenu fileMenu = new JMenu("File");
/* 100 */     fileMenu.setMnemonic('F');
/* 101 */     JMenu editMenu = new JMenu("Edit");
/* 102 */     editMenu.setMnemonic('E');
/* 103 */     JMenu plafMenu = new JMenu("Platform");
/* 104 */     plafMenu.setMnemonic('P'); int i;
/* 105 */     for (i = 0; i < fileItems.length; i++) {
/* 106 */       JMenuItem item = new JMenuItem(fileItems[i], fileShortCuts[i]);
/*     */       
/* 108 */       item.setActionCommand(fileCmds[i]);
/* 109 */       item.addActionListener(this);
/* 110 */       fileMenu.add(item);
/*     */     } 
/* 112 */     for (i = 0; i < editItems.length; i++) {
/* 113 */       JMenuItem item = new JMenuItem(editItems[i], editShortCuts[i]);
/*     */       
/* 115 */       item.addActionListener(this);
/* 116 */       editMenu.add(item);
/*     */     } 
/* 118 */     ButtonGroup group = new ButtonGroup();
/* 119 */     for (int j = 0; j < plafItems.length; j++) {
/* 120 */       JRadioButtonMenuItem item = new JRadioButtonMenuItem(plafItems[j], plafState[j]);
/*     */       
/* 122 */       group.add(item);
/* 123 */       item.addActionListener(this);
/* 124 */       plafMenu.add(item);
/*     */     } 
/* 126 */     menubar.add(fileMenu);
/* 127 */     menubar.add(editMenu);
/* 128 */     menubar.add(plafMenu);
/* 129 */     setJMenuBar(menubar);
/* 130 */     this.consoleTextArea = new ConsoleTextArea(args);
/* 131 */     JScrollPane scroller = new JScrollPane(this.consoleTextArea);
/* 132 */     setContentPane(scroller);
/* 133 */     this.consoleTextArea.setRows(24);
/* 134 */     this.consoleTextArea.setColumns(80);
/* 135 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/* 138 */             System.exit(0);
/*     */           }
/*     */         });
/* 141 */     pack();
/* 142 */     setVisible(true);
/*     */ 
/*     */ 
/*     */     
/* 146 */     Main.setIn(this.consoleTextArea.getIn());
/* 147 */     Main.setOut(this.consoleTextArea.getOut());
/* 148 */     Main.setErr(this.consoleTextArea.getErr());
/* 149 */     Main.main(args);
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 153 */     String cmd = e.getActionCommand();
/* 154 */     String plaf_name = null;
/* 155 */     if (cmd.equals("Load")) {
/* 156 */       String f = chooseFile();
/* 157 */       if (f != null) {
/* 158 */         f = f.replace('\\', '/');
/* 159 */         this.consoleTextArea.eval("load(\"" + f + "\");");
/*     */       } 
/* 161 */     } else if (cmd.equals("Exit")) {
/* 162 */       System.exit(0);
/* 163 */     } else if (cmd.equals("Cut")) {
/* 164 */       this.consoleTextArea.cut();
/* 165 */     } else if (cmd.equals("Copy")) {
/* 166 */       this.consoleTextArea.copy();
/* 167 */     } else if (cmd.equals("Paste")) {
/* 168 */       this.consoleTextArea.paste();
/*     */     } else {
/* 170 */       if (cmd.equals("Metal")) {
/* 171 */         plaf_name = "javax.swing.plaf.metal.MetalLookAndFeel";
/* 172 */       } else if (cmd.equals("Windows")) {
/* 173 */         plaf_name = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
/* 174 */       } else if (cmd.equals("Motif")) {
/* 175 */         plaf_name = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
/*     */       } 
/* 177 */       if (plaf_name != null)
/*     */         try {
/* 179 */           UIManager.setLookAndFeel(plaf_name);
/* 180 */           SwingUtilities.updateComponentTreeUI(this);
/* 181 */           this.consoleTextArea.postUpdateUI();
/*     */ 
/*     */           
/* 184 */           createFileChooser();
/* 185 */         } catch (Exception exc) {
/* 186 */           JOptionPane.showMessageDialog(this, exc.getMessage(), "Platform", 0);
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\JSConsole.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */