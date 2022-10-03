/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGDisplayPanel;
/*     */ import com.kitfox.svg.SVGElement;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import com.kitfox.svg.SVGUniverse;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.geom.Point2D;
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
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGPlayer
/*     */   extends JFrame
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*  73 */   SVGDisplayPanel svgDisplayPanel = new SVGDisplayPanel(); final PlayerDialog playerDialog; SVGUniverse universe; final JFileChooser fileChooser;
/*     */   private JCheckBoxMenuItem CheckBoxMenuItem_anonInputStream;
/*     */   private JCheckBoxMenuItem cmCheck_verbose;
/*     */   private JMenuItem cm_800x600;
/*     */   private JMenuItem cm_about;
/*     */   private JMenuItem cm_loadFile;
/*     */   private JMenuItem cm_loadUrl;
/*     */   private JMenuItem cm_player;
/*     */   
/*     */   public SVGPlayer() {
/*  83 */     JFileChooser fc = null;
/*     */     
/*     */     try {
/*  86 */       fc = new JFileChooser();
/*  87 */       fc.setFileFilter(new FileFilter()
/*     */           {
/*  89 */             final Matcher matchLevelFile = Pattern.compile(".*\\.svg[z]?").matcher("");
/*     */ 
/*     */ 
/*     */             
/*     */             public boolean accept(File file) {
/*  94 */               if (file.isDirectory()) return true;
/*     */               
/*  96 */               this.matchLevelFile.reset(file.getName());
/*  97 */               return this.matchLevelFile.matches();
/*     */             }
/*     */             
/*     */             public String getDescription() {
/* 101 */               return "SVG file (*.svg, *.svgz)";
/*     */             }
/*     */           });
/*     */     }
/* 105 */     catch (AccessControlException accessControlException) {}
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.fileChooser = fc;
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
/* 129 */     initComponents();
/*     */     
/* 131 */     setSize(800, 600);
/*     */     
/* 133 */     this.svgDisplayPanel.setBgColor(Color.white);
/* 134 */     this.svgDisplayPanel.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           
/*     */           public void mouseClicked(MouseEvent evt)
/*     */           {
/* 139 */             SVGDiagram diagram = SVGPlayer.this.svgDisplayPanel.getDiagram();
/* 140 */             if (diagram == null)
/*     */               return; 
/* 142 */             System.out.println("Picking at cursor (" + evt.getX() + ", " + evt.getY() + ")");
/*     */             
/*     */             try {
/* 145 */               List<List<SVGElement>> paths = diagram.pick(new Point2D.Float(evt.getX(), evt.getY()), null);
/* 146 */               for (int i = 0; i < paths.size(); i++)
/*     */               {
/* 148 */                 System.out.println(SVGPlayer.this.pathToString(paths.get(i)));
/*     */               }
/*     */             }
/* 151 */             catch (SVGException ex) {
/*     */               
/* 153 */               Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not pick", (Throwable)ex);
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 160 */     this.svgDisplayPanel.setPreferredSize(getSize());
/* 161 */     this.scrollPane_svgArea.setViewportView((Component)this.svgDisplayPanel);
/*     */     
/* 163 */     this.playerDialog = new PlayerDialog(this);
/*     */   } private JMenuBar jMenuBar1;
/*     */   private JSeparator jSeparator2;
/*     */   
/*     */   private String pathToString(List<SVGElement> path) {
/* 168 */     if (path.size() == 0) return "";
/*     */     
/* 170 */     StringBuffer sb = new StringBuffer();
/* 171 */     sb.append(path.get(0));
/* 172 */     for (int i = 1; i < path.size(); i++) {
/*     */       
/* 174 */       sb.append("/");
/* 175 */       sb.append(((SVGElement)path.get(i)).getId());
/*     */     } 
/* 177 */     return sb.toString();
/*     */   }
/*     */   private JMenu menu_file; private JMenu menu_help; private JMenu menu_window;
/*     */   private JScrollPane scrollPane_svgArea;
/*     */   
/*     */   public void updateTime(double curTime) {
/*     */     try {
/* 184 */       if (this.universe != null)
/*     */       {
/* 186 */         this.universe.setCurTime(curTime);
/* 187 */         this.universe.updateTime();
/*     */         
/* 189 */         repaint();
/*     */       }
/*     */     
/* 192 */     } catch (Exception e) {
/*     */       
/* 194 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadURL(URL url) {
/* 200 */     boolean verbose = this.cmCheck_verbose.isSelected();
/*     */     
/* 202 */     this.universe = new SVGUniverse();
/* 203 */     this.universe.setVerbose(verbose);
/* 204 */     SVGDiagram diagram = null;
/*     */     
/* 206 */     if (!this.CheckBoxMenuItem_anonInputStream.isSelected()) {
/*     */ 
/*     */       
/* 209 */       URI uri = this.universe.loadSVG(url);
/*     */       
/* 211 */       if (verbose) System.err.println(uri.toString());
/*     */       
/* 213 */       diagram = this.universe.getDiagram(uri);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 220 */         InputStream is = url.openStream();
/* 221 */         URI uri = this.universe.loadSVG(is, "defaultName");
/*     */         
/* 223 */         if (verbose) System.err.println(uri.toString());
/*     */         
/* 225 */         diagram = this.universe.getDiagram(uri);
/*     */       }
/* 227 */       catch (Exception e) {
/*     */         
/* 229 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */       } 
/*     */     } 
/*     */     
/* 233 */     this.svgDisplayPanel.setDiagram(diagram);
/* 234 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initComponents() {
/* 245 */     this.scrollPane_svgArea = new JScrollPane();
/* 246 */     this.jMenuBar1 = new JMenuBar();
/* 247 */     this.menu_file = new JMenu();
/* 248 */     this.cm_loadFile = new JMenuItem();
/* 249 */     this.cm_loadUrl = new JMenuItem();
/* 250 */     this.menu_window = new JMenu();
/* 251 */     this.cm_player = new JMenuItem();
/* 252 */     this.jSeparator2 = new JSeparator();
/* 253 */     this.cm_800x600 = new JMenuItem();
/* 254 */     this.CheckBoxMenuItem_anonInputStream = new JCheckBoxMenuItem();
/* 255 */     this.cmCheck_verbose = new JCheckBoxMenuItem();
/* 256 */     this.menu_help = new JMenu();
/* 257 */     this.cm_about = new JMenuItem();
/*     */     
/* 259 */     setTitle("SVG Player - Salamander Project");
/* 260 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           
/*     */           public void windowClosing(WindowEvent evt)
/*     */           {
/* 265 */             SVGPlayer.this.exitForm(evt);
/*     */           }
/*     */         });
/*     */     
/* 269 */     getContentPane().add(this.scrollPane_svgArea, "Center");
/*     */     
/* 271 */     this.menu_file.setMnemonic('f');
/* 272 */     this.menu_file.setText("File");
/* 273 */     this.cm_loadFile.setMnemonic('l');
/* 274 */     this.cm_loadFile.setText("Load File...");
/* 275 */     this.cm_loadFile.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 279 */             SVGPlayer.this.cm_loadFileActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 283 */     this.menu_file.add(this.cm_loadFile);
/*     */     
/* 285 */     this.cm_loadUrl.setText("Load URL...");
/* 286 */     this.cm_loadUrl.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 290 */             SVGPlayer.this.cm_loadUrlActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 294 */     this.menu_file.add(this.cm_loadUrl);
/*     */     
/* 296 */     this.jMenuBar1.add(this.menu_file);
/*     */     
/* 298 */     this.menu_window.setText("Window");
/* 299 */     this.cm_player.setText("Player");
/* 300 */     this.cm_player.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 304 */             SVGPlayer.this.cm_playerActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 308 */     this.menu_window.add(this.cm_player);
/*     */     
/* 310 */     this.menu_window.add(this.jSeparator2);
/*     */     
/* 312 */     this.cm_800x600.setText("800 x 600");
/* 313 */     this.cm_800x600.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 317 */             SVGPlayer.this.cm_800x600ActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 321 */     this.menu_window.add(this.cm_800x600);
/*     */     
/* 323 */     this.CheckBoxMenuItem_anonInputStream.setText("Anonymous Input Stream");
/* 324 */     this.menu_window.add(this.CheckBoxMenuItem_anonInputStream);
/*     */     
/* 326 */     this.cmCheck_verbose.setText("Verbose");
/* 327 */     this.cmCheck_verbose.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 331 */             SVGPlayer.this.cmCheck_verboseActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 335 */     this.menu_window.add(this.cmCheck_verbose);
/*     */     
/* 337 */     this.jMenuBar1.add(this.menu_window);
/*     */     
/* 339 */     this.menu_help.setText("Help");
/* 340 */     this.cm_about.setText("About...");
/* 341 */     this.cm_about.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent evt)
/*     */           {
/* 345 */             SVGPlayer.this.cm_aboutActionPerformed(evt);
/*     */           }
/*     */         });
/*     */     
/* 349 */     this.menu_help.add(this.cm_about);
/*     */     
/* 351 */     this.jMenuBar1.add(this.menu_help);
/*     */     
/* 353 */     setJMenuBar(this.jMenuBar1);
/*     */     
/* 355 */     pack();
/*     */   }
/*     */ 
/*     */   
/*     */   private void cm_loadUrlActionPerformed(ActionEvent evt) {
/* 360 */     String urlStrn = JOptionPane.showInputDialog(this, "Enter URL of SVG file");
/* 361 */     if (urlStrn == null) {
/*     */       return;
/*     */     }
/*     */     try {
/* 365 */       URL url = new URL(URLEncoder.encode(urlStrn, "UTF-8"));
/* 366 */       loadURL(url);
/*     */     }
/* 368 */     catch (Exception e) {
/*     */       
/* 370 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cmCheck_verboseActionPerformed(ActionEvent evt) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private void cm_playerActionPerformed(ActionEvent evt) {
/* 382 */     this.playerDialog.setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void cm_aboutActionPerformed(ActionEvent evt) {
/* 387 */     VersionDialog dia = new VersionDialog(this, true, this.cmCheck_verbose.isSelected());
/* 388 */     dia.setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void cm_800x600ActionPerformed(ActionEvent evt) {
/* 393 */     setSize(800, 600);
/*     */   }
/*     */ 
/*     */   
/*     */   private void cm_loadFileActionPerformed(ActionEvent evt) {
/* 398 */     boolean verbose = this.cmCheck_verbose.isSelected();
/*     */ 
/*     */     
/*     */     try {
/* 402 */       int retVal = this.fileChooser.showOpenDialog(this);
/* 403 */       if (retVal == 0)
/*     */       {
/* 405 */         File chosenFile = this.fileChooser.getSelectedFile();
/*     */         
/* 407 */         URL url = chosenFile.toURI().toURL();
/*     */         
/* 409 */         loadURL(url);
/*     */       }
/*     */     
/* 412 */     } catch (Exception e) {
/*     */       
/* 414 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void exitForm(WindowEvent evt) {
/* 421 */     System.exit(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 428 */     (new SVGPlayer()).setVisible(true);
/*     */   }
/*     */   
/*     */   public void updateTime(double curTime, double timeStep, int playState) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\SVGPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */