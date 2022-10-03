/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import com.kitfox.svg.SVGCache;
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGDisplayPanel;
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGUniverse;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.filechooser.FileFilter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGViewer
/*     */   extends JFrame
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   final JFileChooser fileChooser;
/*     */   private JCheckBoxMenuItem CheckBoxMenuItem_anonInputStream;
/*     */   private JCheckBoxMenuItem cmCheck_verbose;
/*  73 */   SVGDisplayPanel svgDisplayPanel = new SVGDisplayPanel();
/*     */   private JMenuItem cm_800x600;
/*     */   private JMenuItem cm_about;
/*     */   private JMenuItem cm_loadFile;
/*     */   
/*     */   public SVGViewer() {
/*  79 */     JFileChooser fc = null;
/*     */     
/*     */     try {
/*  82 */       fc = new JFileChooser();
/*  83 */       fc.setFileFilter(new FileFilter()
/*     */           {
/*  85 */             final Matcher matchLevelFile = Pattern.compile(".*\\.svg[z]?").matcher("");
/*     */ 
/*     */ 
/*     */             
/*     */             public boolean accept(File file) {
/*  90 */               if (file.isDirectory()) return true;
/*     */               
/*  92 */               this.matchLevelFile.reset(file.getName());
/*  93 */               return this.matchLevelFile.matches();
/*     */             }
/*     */             
/*     */             public String getDescription() {
/*  97 */               return "SVG file (*.svg, *.svgz)";
/*     */             }
/*     */           });
/*     */     }
/* 101 */     catch (AccessControlException accessControlException) {}
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.fileChooser = fc;
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
/* 125 */     initComponents();
/*     */     
/* 127 */     setSize(800, 600);
/*     */     
/* 129 */     this.svgDisplayPanel.setBgColor(Color.white);
/*     */     
/* 131 */     this.svgDisplayPanel.setPreferredSize(getSize());
/* 132 */     this.panel_svgArea.add((Component)this.svgDisplayPanel, "Center");
/*     */   }
/*     */   private JMenuItem cm_loadUrl;
/*     */   
/*     */   private void loadURL(URL url) {
/*     */     URI uri;
/* 138 */     boolean verbose = this.cmCheck_verbose.isSelected();
/*     */ 
/*     */     
/* 141 */     SVGUniverse universe = SVGCache.getSVGUniverse();
/* 142 */     SVGDiagram diagram = null;
/*     */ 
/*     */     
/* 145 */     if (!this.CheckBoxMenuItem_anonInputStream.isSelected()) {
/*     */ 
/*     */       
/* 148 */       uri = universe.loadSVG(url);
/*     */       
/* 150 */       if (verbose) System.err.println("Loading document " + uri.toString());
/*     */       
/* 152 */       diagram = universe.getDiagram(uri);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 159 */         InputStream is = url.openStream();
/* 160 */         uri = universe.loadSVG(is, "defaultName");
/*     */         
/* 162 */         if (verbose) System.err.println("Loading document " + uri.toString());
/*     */       
/* 164 */       } catch (Exception e) {
/*     */         
/* 166 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     diagram = universe.getDiagram(uri);
/*     */     
/* 184 */     this.svgDisplayPanel.setDiagram(diagram);
/* 185 */     repaint();
/*     */   }
/*     */   
/*     */   private JMenuBar jMenuBar1;
/*     */   private JMenu menu_file;
/*     */   private JMenu menu_help;
/*     */   private JMenu menu_window;
/*     */   private JPanel panel_svgArea;
/*     */   private JScrollPane scrollPane_svgArea;
/*     */   
/*     */   private void initComponents() {
/* 196 */     this.scrollPane_svgArea = new JScrollPane();
/* 197 */     this.panel_svgArea = new JPanel();
/* 198 */     this.jMenuBar1 = new JMenuBar();
/* 199 */     this.menu_file = new JMenu();
/* 200 */     this.cm_loadFile = new JMenuItem();
/* 201 */     this.cm_loadUrl = new JMenuItem();
/* 202 */     this.menu_window = new JMenu();
/* 203 */     this.cm_800x600 = new JMenuItem();
/* 204 */     this.CheckBoxMenuItem_anonInputStream = new JCheckBoxMenuItem();
/* 205 */     this.cmCheck_verbose = new JCheckBoxMenuItem();
/* 206 */     this.menu_help = new JMenu();
/* 207 */     this.cm_about = new JMenuItem();
/*     */     
/* 209 */     setTitle("SVG Viewer - Salamander Project");
/* 210 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           
/*     */           public void windowClosing(WindowEvent evt)
/*     */           {
/* 215 */             SVGViewer.this.exitForm(evt);
/*     */           }
/*     */         });
/*     */     
/* 219 */     this.panel_svgArea.setLayout(new BorderLayout());
/*     */     
/* 221 */     this.panel_svgArea.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           
/*     */           public void mousePressed(MouseEvent evt)
/*     */           {
/* 226 */             SVGViewer.this.panel_svgAreaMousePressed(evt);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseReleased(MouseEvent evt) {
/* 231 */             SVGViewer.this.panel_svgAreaMouseReleased(evt);
/*     */           }
/*     */         });
/*     */     
/* 235 */     this.scrollPane_svgArea.setViewportView(this.panel_svgArea);
/*     */     
/* 237 */     getContentPane().add(this.scrollPane_svgArea, "Center");
/*     */     
/* 239 */     this.menu_file.setMnemonic('f');
/* 240 */     this.menu_file.setText("File");
/* 241 */     this.cm_loadFile.setMnemonic('l');
/* 242 */     this.cm_loadFile.setText("Load File...");
/* 243 */     this.cm_loadFile.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 247 */             SVGViewer.this.cm_loadFileActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 251 */     this.menu_file.add(this.cm_loadFile);
/*     */     
/* 253 */     this.cm_loadUrl.setText("Load URL...");
/* 254 */     this.cm_loadUrl.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 258 */             SVGViewer.this.cm_loadUrlActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 262 */     this.menu_file.add(this.cm_loadUrl);
/*     */     
/* 264 */     this.jMenuBar1.add(this.menu_file);
/*     */     
/* 266 */     this.menu_window.setText("Window");
/* 267 */     this.cm_800x600.setText("800 x 600");
/* 268 */     this.cm_800x600.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 272 */             SVGViewer.this.cm_800x600ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 276 */     this.menu_window.add(this.cm_800x600);
/*     */     
/* 278 */     this.CheckBoxMenuItem_anonInputStream.setText("Anonymous Input Stream");
/* 279 */     this.menu_window.add(this.CheckBoxMenuItem_anonInputStream);
/*     */     
/* 281 */     this.cmCheck_verbose.setText("Verbose");
/* 282 */     this.cmCheck_verbose.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 286 */             SVGViewer.this.cmCheck_verboseActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 290 */     this.menu_window.add(this.cmCheck_verbose);
/*     */     
/* 292 */     this.jMenuBar1.add(this.menu_window);
/*     */     
/* 294 */     this.menu_help.setText("Help");
/* 295 */     this.cm_about.setText("About...");
/* 296 */     this.cm_about.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 300 */             SVGViewer.this.cm_aboutActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 304 */     this.menu_help.add(this.cm_about);
/*     */     
/* 306 */     this.jMenuBar1.add(this.menu_help);
/*     */     
/* 308 */     setJMenuBar(this.jMenuBar1);
/*     */     
/* 310 */     pack();
/*     */   }
/*     */ 
/*     */   
/*     */   private void cm_loadUrlActionPerformed(ActionEvent evt) {
/* 315 */     String urlStrn = JOptionPane.showInputDialog(this, "Enter URL of SVG file");
/* 316 */     if (urlStrn == null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 320 */       URL url = new URL(URLEncoder.encode(urlStrn, "UTF-8"));
/* 321 */       loadURL(url);
/*     */     }
/* 323 */     catch (Exception e) {
/*     */       
/* 325 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void panel_svgAreaMouseReleased(MouseEvent evt) {
/*     */     List<List<SVGElement>> pickedElements;
/* 332 */     SVGDiagram diagram = this.svgDisplayPanel.getDiagram();
/*     */ 
/*     */     
/*     */     try {
/* 336 */       pickedElements = diagram.pick(new Point(evt.getX(), evt.getY()), null);
/*     */     }
/* 338 */     catch (SVGException e) {
/*     */       
/* 340 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, (Throwable)e);
/*     */       
/*     */       return;
/*     */     } 
/* 344 */     System.out.println("Pick results:");
/* 345 */     for (List<SVGElement> path : pickedElements) {
/* 346 */       System.out.print("  Path: ");
/*     */       
/* 348 */       for (SVGElement ele : path) {
/* 349 */         System.out.print("" + ele.getId() + "(" + ele.getClass().getName() + ") ");
/*     */       }
/* 351 */       System.out.println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void panel_svgAreaMousePressed(MouseEvent evt) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private void cmCheck_verboseActionPerformed(ActionEvent evt) {
/* 362 */     SVGCache.getSVGUniverse().setVerbose(this.cmCheck_verbose.isSelected());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void cm_aboutActionPerformed(ActionEvent evt) {
/* 368 */     VersionDialog dlg = new VersionDialog(this, true, this.cmCheck_verbose.isSelected());
/* 369 */     dlg.setVisible(true);
/*     */   }
/*     */   
/*     */   private void cm_800x600ActionPerformed(ActionEvent evt) {
/* 373 */     setSize(800, 600);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void cm_loadFileActionPerformed(ActionEvent evt) {
/*     */     try {
/* 380 */       int retVal = this.fileChooser.showOpenDialog(this);
/* 381 */       if (retVal == 0)
/*     */       {
/* 383 */         File chosenFile = this.fileChooser.getSelectedFile();
/*     */         
/* 385 */         URL url = chosenFile.toURI().toURL();
/*     */         
/* 387 */         loadURL(url);
/*     */       }
/*     */     
/* 390 */     } catch (Exception e) {
/*     */       
/* 392 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void exitForm(WindowEvent evt) {
/* 401 */     System.exit(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 408 */     (new SVGViewer()).setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\SVGViewer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */