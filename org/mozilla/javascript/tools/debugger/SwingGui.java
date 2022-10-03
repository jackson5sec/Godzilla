/*     */ package org.mozilla.javascript.tools.debugger;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.ActiveEvent;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.MenuComponent;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.SecurityUtilities;
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
/*     */ public class SwingGui
/*     */   extends JFrame
/*     */   implements GuiCallback
/*     */ {
/*     */   private static final long serialVersionUID = -8217029773456711621L;
/*     */   Dim dim;
/*     */   private Runnable exitAction;
/*     */   private JDesktopPane desk;
/*     */   private ContextWindow context;
/*     */   private Menubar menubar;
/*     */   private JToolBar toolBar;
/*     */   private JSInternalConsole console;
/*     */   private JSplitPane split1;
/*     */   private JLabel statusBar;
/* 118 */   private final Map<String, JFrame> toplevels = Collections.synchronizedMap(new HashMap<String, JFrame>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private final Map<String, FileWindow> fileWindows = Collections.synchronizedMap(new HashMap<String, FileWindow>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileWindow currentWindow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JFileChooser dlg;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EventQueue awtEventQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SwingGui(Dim dim, String title) {
/* 148 */     super(title);
/* 149 */     this.dim = dim;
/* 150 */     init();
/* 151 */     dim.setGuiCallback(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Menubar getMenubar() {
/* 158 */     return this.menubar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExitAction(Runnable r) {
/* 166 */     this.exitAction = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSInternalConsole getConsole() {
/* 173 */     return this.console;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean b) {
/* 181 */     super.setVisible(b);
/* 182 */     if (b) {
/*     */       
/* 184 */       this.console.consoleTextArea.requestFocus();
/* 185 */       this.context.split.setDividerLocation(0.5D);
/*     */       try {
/* 187 */         this.console.setMaximum(true);
/* 188 */         this.console.setSelected(true);
/* 189 */         this.console.show();
/* 190 */         this.console.consoleTextArea.requestFocus();
/* 191 */       } catch (Exception exc) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addTopLevel(String key, JFrame frame) {
/* 200 */     if (frame != this) {
/* 201 */       this.toplevels.put(key, frame);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 209 */     this.menubar = new Menubar(this);
/* 210 */     setJMenuBar(this.menubar);
/* 211 */     this.toolBar = new JToolBar();
/*     */ 
/*     */ 
/*     */     
/* 215 */     String[] toolTips = { "Break (Pause)", "Go (F5)", "Step Into (F11)", "Step Over (F7)", "Step Out (F8)" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     int count = 0;
/* 221 */     JButton breakButton = new JButton("Break"), button = breakButton;
/* 222 */     button.setToolTipText("Break");
/* 223 */     button.setActionCommand("Break");
/* 224 */     button.addActionListener(this.menubar);
/* 225 */     button.setEnabled(true);
/* 226 */     button.setToolTipText(toolTips[count++]);
/*     */     
/* 228 */     JButton goButton = new JButton("Go");
/* 229 */     button.setToolTipText("Go");
/* 230 */     button.setActionCommand("Go");
/* 231 */     button.addActionListener(this.menubar);
/* 232 */     button.setEnabled(false);
/* 233 */     button.setToolTipText(toolTips[count++]);
/*     */     
/* 235 */     JButton stepIntoButton = new JButton("Step Into");
/* 236 */     button.setToolTipText("Step Into");
/* 237 */     button.setActionCommand("Step Into");
/* 238 */     button.addActionListener(this.menubar);
/* 239 */     button.setEnabled(false);
/* 240 */     button.setToolTipText(toolTips[count++]);
/*     */     
/* 242 */     JButton stepOverButton = new JButton("Step Over");
/* 243 */     button.setToolTipText("Step Over");
/* 244 */     button.setActionCommand("Step Over");
/* 245 */     button.setEnabled(false);
/* 246 */     button.addActionListener(this.menubar);
/* 247 */     button.setToolTipText(toolTips[count++]);
/*     */     
/* 249 */     JButton stepOutButton = new JButton("Step Out");
/* 250 */     button.setToolTipText("Step Out");
/* 251 */     button.setActionCommand("Step Out");
/* 252 */     button.setEnabled(false);
/* 253 */     button.addActionListener(this.menubar);
/* 254 */     button.setToolTipText(toolTips[count++]);
/*     */     
/* 256 */     Dimension dim = stepOverButton.getPreferredSize();
/* 257 */     breakButton.setPreferredSize(dim);
/* 258 */     breakButton.setMinimumSize(dim);
/* 259 */     breakButton.setMaximumSize(dim);
/* 260 */     breakButton.setSize(dim);
/* 261 */     goButton.setPreferredSize(dim);
/* 262 */     goButton.setMinimumSize(dim);
/* 263 */     goButton.setMaximumSize(dim);
/* 264 */     stepIntoButton.setPreferredSize(dim);
/* 265 */     stepIntoButton.setMinimumSize(dim);
/* 266 */     stepIntoButton.setMaximumSize(dim);
/* 267 */     stepOverButton.setPreferredSize(dim);
/* 268 */     stepOverButton.setMinimumSize(dim);
/* 269 */     stepOverButton.setMaximumSize(dim);
/* 270 */     stepOutButton.setPreferredSize(dim);
/* 271 */     stepOutButton.setMinimumSize(dim);
/* 272 */     stepOutButton.setMaximumSize(dim);
/* 273 */     this.toolBar.add(breakButton);
/* 274 */     this.toolBar.add(goButton);
/* 275 */     this.toolBar.add(stepIntoButton);
/* 276 */     this.toolBar.add(stepOverButton);
/* 277 */     this.toolBar.add(stepOutButton);
/*     */     
/* 279 */     JPanel contentPane = new JPanel();
/* 280 */     contentPane.setLayout(new BorderLayout());
/* 281 */     getContentPane().add(this.toolBar, "North");
/* 282 */     getContentPane().add(contentPane, "Center");
/* 283 */     this.desk = new JDesktopPane();
/* 284 */     this.desk.setPreferredSize(new Dimension(600, 300));
/* 285 */     this.desk.setMinimumSize(new Dimension(150, 50));
/* 286 */     this.desk.add(this.console = new JSInternalConsole("JavaScript Console"));
/* 287 */     this.context = new ContextWindow(this);
/* 288 */     this.context.setPreferredSize(new Dimension(600, 120));
/* 289 */     this.context.setMinimumSize(new Dimension(50, 50));
/*     */     
/* 291 */     this.split1 = new JSplitPane(0, this.desk, this.context);
/*     */     
/* 293 */     this.split1.setOneTouchExpandable(true);
/* 294 */     setResizeWeight(this.split1, 0.66D);
/* 295 */     contentPane.add(this.split1, "Center");
/* 296 */     this.statusBar = new JLabel();
/* 297 */     this.statusBar.setText("Thread: ");
/* 298 */     contentPane.add(this.statusBar, "South");
/* 299 */     this.dlg = new JFileChooser();
/*     */     
/* 301 */     FileFilter filter = new FileFilter()
/*     */       {
/*     */         public boolean accept(File f)
/*     */         {
/* 305 */           if (f.isDirectory()) {
/* 306 */             return true;
/*     */           }
/* 308 */           String n = f.getName();
/* 309 */           int i = n.lastIndexOf('.');
/* 310 */           if (i > 0 && i < n.length() - 1) {
/* 311 */             String ext = n.substring(i + 1).toLowerCase();
/* 312 */             if (ext.equals("js")) {
/* 313 */               return true;
/*     */             }
/*     */           } 
/* 316 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public String getDescription() {
/* 321 */           return "JavaScript Files (*.js)";
/*     */         }
/*     */       };
/* 324 */     this.dlg.addChoosableFileFilter(filter);
/* 325 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/* 328 */             SwingGui.this.exit();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void exit() {
/* 337 */     if (this.exitAction != null) {
/* 338 */       SwingUtilities.invokeLater(this.exitAction);
/*     */     }
/* 340 */     this.dim.setReturnValue(5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FileWindow getFileWindow(String url) {
/* 347 */     if (url == null || url.equals("<stdin>")) {
/* 348 */       return null;
/*     */     }
/* 350 */     return this.fileWindows.get(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getShortName(String url) {
/* 357 */     int lastSlash = url.lastIndexOf('/');
/* 358 */     if (lastSlash < 0) {
/* 359 */       lastSlash = url.lastIndexOf('\\');
/*     */     }
/* 361 */     String shortName = url;
/* 362 */     if (lastSlash >= 0 && lastSlash + 1 < url.length()) {
/* 363 */       shortName = url.substring(lastSlash + 1);
/*     */     }
/* 365 */     return shortName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeWindow(FileWindow w) {
/* 372 */     this.fileWindows.remove(w.getUrl());
/* 373 */     JMenu windowMenu = getWindowMenu();
/* 374 */     int count = windowMenu.getItemCount();
/* 375 */     JMenuItem lastItem = windowMenu.getItem(count - 1);
/* 376 */     String name = getShortName(w.getUrl());
/* 377 */     for (int i = 5; i < count; i++) {
/* 378 */       JMenuItem item = windowMenu.getItem(i);
/* 379 */       if (item != null) {
/* 380 */         String text = item.getText();
/*     */ 
/*     */         
/* 383 */         int pos = text.indexOf(' ');
/* 384 */         if (text.substring(pos + 1).equals(name)) {
/* 385 */           windowMenu.remove(item);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 391 */           if (count == 6) {
/*     */             
/* 393 */             windowMenu.remove(4); break;
/*     */           } 
/* 395 */           int j = i - 4;
/* 396 */           for (; i < count - 1; i++) {
/* 397 */             JMenuItem thisItem = windowMenu.getItem(i);
/* 398 */             if (thisItem != null) {
/*     */ 
/*     */               
/* 401 */               text = thisItem.getText();
/* 402 */               if (text.equals("More Windows...")) {
/*     */                 break;
/*     */               }
/* 405 */               pos = text.indexOf(' ');
/* 406 */               thisItem.setText((char)(48 + j) + " " + text.substring(pos + 1));
/*     */               
/* 408 */               thisItem.setMnemonic(48 + j);
/* 409 */               j++;
/*     */             } 
/*     */           } 
/*     */           
/* 413 */           if (count - 6 == 0 && lastItem != item && 
/* 414 */             lastItem.getText().equals("More Windows...")) {
/* 415 */             windowMenu.remove(lastItem);
/*     */           }
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 422 */     windowMenu.revalidate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void showStopLine(Dim.StackFrame frame) {
/* 429 */     String sourceName = frame.getUrl();
/* 430 */     if (sourceName == null || sourceName.equals("<stdin>")) {
/* 431 */       if (this.console.isVisible()) {
/* 432 */         this.console.show();
/*     */       }
/*     */     } else {
/* 435 */       showFileWindow(sourceName, -1);
/* 436 */       int lineNumber = frame.getLineNumber();
/* 437 */       FileWindow w = getFileWindow(sourceName);
/* 438 */       if (w != null) {
/* 439 */         setFilePosition(w, lineNumber);
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
/*     */   protected void showFileWindow(String sourceUrl, int lineNumber) {
/* 452 */     FileWindow w = getFileWindow(sourceUrl);
/* 453 */     if (w == null) {
/* 454 */       Dim.SourceInfo si = this.dim.sourceInfo(sourceUrl);
/* 455 */       createFileWindow(si, -1);
/* 456 */       w = getFileWindow(sourceUrl);
/*     */     } 
/* 458 */     if (lineNumber > -1) {
/* 459 */       int start = w.getPosition(lineNumber - 1);
/* 460 */       int end = w.getPosition(lineNumber) - 1;
/* 461 */       w.textArea.select(start);
/* 462 */       w.textArea.setCaretPosition(start);
/* 463 */       w.textArea.moveCaretPosition(end);
/*     */     } 
/*     */     try {
/* 466 */       if (w.isIcon()) {
/* 467 */         w.setIcon(false);
/*     */       }
/* 469 */       w.setVisible(true);
/* 470 */       w.moveToFront();
/* 471 */       w.setSelected(true);
/* 472 */       requestFocus();
/* 473 */       w.requestFocus();
/* 474 */       w.textArea.requestFocus();
/* 475 */     } catch (Exception exc) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createFileWindow(Dim.SourceInfo sourceInfo, int line) {
/* 483 */     boolean activate = true;
/*     */     
/* 485 */     String url = sourceInfo.url();
/* 486 */     FileWindow w = new FileWindow(this, sourceInfo);
/* 487 */     this.fileWindows.put(url, w);
/* 488 */     if (line != -1) {
/* 489 */       if (this.currentWindow != null) {
/* 490 */         this.currentWindow.setPosition(-1);
/*     */       }
/*     */       try {
/* 493 */         w.setPosition(w.textArea.getLineStartOffset(line - 1));
/* 494 */       } catch (BadLocationException exc) {
/*     */         try {
/* 496 */           w.setPosition(w.textArea.getLineStartOffset(0));
/* 497 */         } catch (BadLocationException ee) {
/* 498 */           w.setPosition(-1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 502 */     this.desk.add(w);
/* 503 */     if (line != -1) {
/* 504 */       this.currentWindow = w;
/*     */     }
/* 506 */     this.menubar.addFile(url);
/* 507 */     w.setVisible(true);
/*     */     
/* 509 */     if (activate) {
/*     */       try {
/* 511 */         w.setMaximum(true);
/* 512 */         w.setSelected(true);
/* 513 */         w.moveToFront();
/* 514 */       } catch (Exception exc) {}
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
/*     */ 
/*     */   
/*     */   protected boolean updateFileWindow(Dim.SourceInfo sourceInfo) {
/* 528 */     String fileName = sourceInfo.url();
/* 529 */     FileWindow w = getFileWindow(fileName);
/* 530 */     if (w != null) {
/* 531 */       w.updateText(sourceInfo);
/* 532 */       w.show();
/* 533 */       return true;
/*     */     } 
/* 535 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFilePosition(FileWindow w, int line) {
/* 543 */     boolean activate = true;
/* 544 */     JTextArea ta = w.textArea;
/*     */     try {
/* 546 */       if (line == -1) {
/* 547 */         w.setPosition(-1);
/* 548 */         if (this.currentWindow == w) {
/* 549 */           this.currentWindow = null;
/*     */         }
/*     */       } else {
/* 552 */         int loc = ta.getLineStartOffset(line - 1);
/* 553 */         if (this.currentWindow != null && this.currentWindow != w) {
/* 554 */           this.currentWindow.setPosition(-1);
/*     */         }
/* 556 */         w.setPosition(loc);
/* 557 */         this.currentWindow = w;
/*     */       } 
/* 559 */     } catch (BadLocationException exc) {}
/*     */ 
/*     */     
/* 562 */     if (activate) {
/* 563 */       if (w.isIcon()) {
/* 564 */         this.desk.getDesktopManager().deiconifyFrame(w);
/*     */       }
/* 566 */       this.desk.getDesktopManager().activateFrame(w);
/*     */       try {
/* 568 */         w.show();
/* 569 */         w.toFront();
/* 570 */         w.setSelected(true);
/* 571 */       } catch (Exception exc) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void enterInterruptImpl(Dim.StackFrame lastFrame, String threadTitle, String alertMessage) {
/* 581 */     this.statusBar.setText("Thread: " + threadTitle);
/*     */     
/* 583 */     showStopLine(lastFrame);
/*     */     
/* 585 */     if (alertMessage != null) {
/* 586 */       MessageDialogWrapper.showMessageDialog(this, alertMessage, "Exception in Script", 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 592 */     updateEnabled(true);
/*     */     
/* 594 */     Dim.ContextData contextData = lastFrame.contextData();
/*     */     
/* 596 */     JComboBox<String> ctx = this.context.context;
/* 597 */     List<String> toolTips = this.context.toolTips;
/* 598 */     this.context.disableUpdate();
/* 599 */     int frameCount = contextData.frameCount();
/* 600 */     ctx.removeAllItems();
/*     */ 
/*     */     
/* 603 */     ctx.setSelectedItem((Object)null);
/* 604 */     toolTips.clear();
/* 605 */     for (int i = 0; i < frameCount; i++) {
/* 606 */       Dim.StackFrame frame = contextData.getFrame(i);
/* 607 */       String url = frame.getUrl();
/* 608 */       int lineNumber = frame.getLineNumber();
/* 609 */       String shortName = url;
/* 610 */       if (url.length() > 20) {
/* 611 */         shortName = "..." + url.substring(url.length() - 17);
/*     */       }
/* 613 */       String location = "\"" + shortName + "\", line " + lineNumber;
/* 614 */       ctx.insertItemAt(location, i);
/* 615 */       location = "\"" + url + "\", line " + lineNumber;
/* 616 */       toolTips.add(location);
/*     */     } 
/* 618 */     this.context.enableUpdate();
/* 619 */     ctx.setSelectedIndex(0);
/* 620 */     ctx.setMinimumSize(new Dimension(50, (ctx.getMinimumSize()).height));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JMenu getWindowMenu() {
/* 627 */     return this.menubar.getMenu(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String chooseFile(String title) {
/* 634 */     this.dlg.setDialogTitle(title);
/* 635 */     File CWD = null;
/* 636 */     String dir = SecurityUtilities.getSystemProperty("user.dir");
/* 637 */     if (dir != null) {
/* 638 */       CWD = new File(dir);
/*     */     }
/* 640 */     if (CWD != null) {
/* 641 */       this.dlg.setCurrentDirectory(CWD);
/*     */     }
/* 643 */     int returnVal = this.dlg.showOpenDialog(this);
/* 644 */     if (returnVal == 0) {
/*     */       
/* 646 */       try { String result = this.dlg.getSelectedFile().getCanonicalPath();
/* 647 */         CWD = this.dlg.getSelectedFile().getParentFile();
/* 648 */         Properties props = System.getProperties();
/* 649 */         props.put("user.dir", CWD.getPath());
/* 650 */         System.setProperties(props);
/* 651 */         return result; }
/* 652 */       catch (IOException ignored) {  }
/* 653 */       catch (SecurityException ignored) {}
/*     */     }
/*     */     
/* 656 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JInternalFrame getSelectedFrame() {
/* 663 */     JInternalFrame[] frames = this.desk.getAllFrames();
/* 664 */     for (int i = 0; i < frames.length; i++) {
/* 665 */       if (frames[i].isShowing()) {
/* 666 */         return frames[i];
/*     */       }
/*     */     } 
/* 669 */     return frames[frames.length - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateEnabled(boolean interrupted) {
/* 677 */     ((Menubar)getJMenuBar()).updateEnabled(interrupted);
/* 678 */     for (int ci = 0, cc = this.toolBar.getComponentCount(); ci < cc; ci++) {
/*     */       boolean enableButton;
/* 680 */       if (ci == 0) {
/*     */         
/* 682 */         enableButton = !interrupted;
/*     */       } else {
/* 684 */         enableButton = interrupted;
/*     */       } 
/* 686 */       this.toolBar.getComponent(ci).setEnabled(enableButton);
/*     */     } 
/* 688 */     if (interrupted) {
/* 689 */       this.toolBar.setEnabled(true);
/*     */       
/* 691 */       int state = getExtendedState();
/* 692 */       if (state == 1) {
/* 693 */         setExtendedState(0);
/*     */       }
/* 695 */       toFront();
/* 696 */       this.context.setEnabled(true);
/*     */     } else {
/* 698 */       if (this.currentWindow != null) this.currentWindow.setPosition(-1); 
/* 699 */       this.context.setEnabled(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setResizeWeight(JSplitPane pane, double weight) {
/*     */     
/* 709 */     try { Method m = JSplitPane.class.getMethod("setResizeWeight", new Class[] { double.class });
/*     */       
/* 711 */       m.invoke(pane, new Object[] { new Double(weight) }); }
/* 712 */     catch (NoSuchMethodException exc) {  }
/* 713 */     catch (IllegalAccessException exc) {  }
/* 714 */     catch (InvocationTargetException exc) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String readFile(String fileName) {
/*     */     String str;
/*     */     try {
/* 724 */       Reader r = new FileReader(fileName);
/*     */       try {
/* 726 */         str = Kit.readReader(r);
/*     */       } finally {
/* 728 */         r.close();
/*     */       } 
/* 730 */     } catch (IOException ex) {
/* 731 */       MessageDialogWrapper.showMessageDialog(this, ex.getMessage(), "Error reading " + fileName, 0);
/*     */ 
/*     */ 
/*     */       
/* 735 */       str = null;
/*     */     } 
/* 737 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSourceText(Dim.SourceInfo sourceInfo) {
/* 746 */     RunProxy proxy = new RunProxy(this, 3);
/* 747 */     proxy.sourceInfo = sourceInfo;
/* 748 */     SwingUtilities.invokeLater(proxy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterInterrupt(Dim.StackFrame lastFrame, String threadTitle, String alertMessage) {
/* 757 */     if (SwingUtilities.isEventDispatchThread()) {
/* 758 */       enterInterruptImpl(lastFrame, threadTitle, alertMessage);
/*     */     } else {
/* 760 */       RunProxy proxy = new RunProxy(this, 4);
/* 761 */       proxy.lastFrame = lastFrame;
/* 762 */       proxy.threadTitle = threadTitle;
/* 763 */       proxy.alertMessage = alertMessage;
/* 764 */       SwingUtilities.invokeLater(proxy);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuiEventThread() {
/* 772 */     return SwingUtilities.isEventDispatchThread();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatchNextGuiEvent() throws InterruptedException {
/* 779 */     EventQueue queue = this.awtEventQueue;
/* 780 */     if (queue == null) {
/* 781 */       queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
/* 782 */       this.awtEventQueue = queue;
/*     */     } 
/* 784 */     AWTEvent event = queue.getNextEvent();
/* 785 */     if (event instanceof ActiveEvent) {
/* 786 */       ((ActiveEvent)event).dispatch();
/*     */     } else {
/* 788 */       Object source = event.getSource();
/* 789 */       if (source instanceof Component) {
/* 790 */         Component comp = (Component)source;
/* 791 */         comp.dispatchEvent(event);
/* 792 */       } else if (source instanceof MenuComponent) {
/* 793 */         ((MenuComponent)source).dispatchEvent(event);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 804 */     String cmd = e.getActionCommand();
/* 805 */     int returnValue = -1;
/* 806 */     if (cmd.equals("Cut") || cmd.equals("Copy") || cmd.equals("Paste")) {
/* 807 */       JInternalFrame f = getSelectedFrame();
/* 808 */       if (f != null && f instanceof ActionListener) {
/* 809 */         ((ActionListener)f).actionPerformed(e);
/*     */       }
/* 811 */     } else if (cmd.equals("Step Over")) {
/* 812 */       returnValue = 0;
/* 813 */     } else if (cmd.equals("Step Into")) {
/* 814 */       returnValue = 1;
/* 815 */     } else if (cmd.equals("Step Out")) {
/* 816 */       returnValue = 2;
/* 817 */     } else if (cmd.equals("Go")) {
/* 818 */       returnValue = 3;
/* 819 */     } else if (cmd.equals("Break")) {
/* 820 */       this.dim.setBreak();
/* 821 */     } else if (cmd.equals("Exit")) {
/* 822 */       exit();
/* 823 */     } else if (cmd.equals("Open")) {
/* 824 */       String fileName = chooseFile("Select a file to compile");
/* 825 */       if (fileName != null) {
/* 826 */         String text = readFile(fileName);
/* 827 */         if (text != null) {
/* 828 */           RunProxy proxy = new RunProxy(this, 1);
/* 829 */           proxy.fileName = fileName;
/* 830 */           proxy.text = text;
/* 831 */           (new Thread(proxy)).start();
/*     */         } 
/*     */       } 
/* 834 */     } else if (cmd.equals("Load")) {
/* 835 */       String fileName = chooseFile("Select a file to execute");
/* 836 */       if (fileName != null) {
/* 837 */         String text = readFile(fileName);
/* 838 */         if (text != null) {
/* 839 */           RunProxy proxy = new RunProxy(this, 2);
/* 840 */           proxy.fileName = fileName;
/* 841 */           proxy.text = text;
/* 842 */           (new Thread(proxy)).start();
/*     */         } 
/*     */       } 
/* 845 */     } else if (cmd.equals("More Windows...")) {
/* 846 */       MoreWindows dlg = new MoreWindows(this, this.fileWindows, "Window", "Files");
/*     */       
/* 848 */       dlg.showDialog(this);
/* 849 */     } else if (cmd.equals("Console")) {
/* 850 */       if (this.console.isIcon()) {
/* 851 */         this.desk.getDesktopManager().deiconifyFrame(this.console);
/*     */       }
/* 853 */       this.console.show();
/* 854 */       this.desk.getDesktopManager().activateFrame(this.console);
/* 855 */       this.console.consoleTextArea.requestFocus();
/* 856 */     } else if (!cmd.equals("Cut") && 
/* 857 */       !cmd.equals("Copy") && 
/* 858 */       !cmd.equals("Paste")) {
/* 859 */       if (cmd.equals("Go to function...")) {
/* 860 */         FindFunction dlg = new FindFunction(this, "Go to function", "Function");
/*     */         
/* 862 */         dlg.showDialog(this);
/* 863 */       } else if (cmd.equals("Tile")) {
/* 864 */         JInternalFrame[] frames = this.desk.getAllFrames();
/* 865 */         int count = frames.length;
/*     */         
/* 867 */         int cols = (int)Math.sqrt(count), rows = cols;
/* 868 */         if (rows * cols < count) {
/* 869 */           cols++;
/* 870 */           if (rows * cols < count) {
/* 871 */             rows++;
/*     */           }
/*     */         } 
/* 874 */         Dimension size = this.desk.getSize();
/* 875 */         int w = size.width / cols;
/* 876 */         int h = size.height / rows;
/* 877 */         int x = 0;
/* 878 */         int y = 0;
/* 879 */         for (int i = 0; i < rows; i++) {
/* 880 */           for (int j = 0; j < cols; j++) {
/* 881 */             int index = i * cols + j;
/* 882 */             if (index >= frames.length) {
/*     */               break;
/*     */             }
/* 885 */             JInternalFrame f = frames[index];
/*     */             try {
/* 887 */               f.setIcon(false);
/* 888 */               f.setMaximum(false);
/* 889 */             } catch (Exception exc) {}
/*     */             
/* 891 */             this.desk.getDesktopManager().setBoundsForFrame(f, x, y, w, h);
/*     */             
/* 893 */             x += w;
/*     */           } 
/* 895 */           y += h;
/* 896 */           x = 0;
/*     */         } 
/* 898 */       } else if (cmd.equals("Cascade")) {
/* 899 */         JInternalFrame[] frames = this.desk.getAllFrames();
/* 900 */         int count = frames.length;
/*     */         
/* 902 */         int y = 0, x = y;
/* 903 */         int h = this.desk.getHeight();
/* 904 */         int d = h / count;
/* 905 */         if (d > 30) d = 30; 
/* 906 */         for (int i = count - 1; i >= 0; i--, x += d, y += d) {
/* 907 */           JInternalFrame f = frames[i];
/*     */           try {
/* 909 */             f.setIcon(false);
/* 910 */             f.setMaximum(false);
/* 911 */           } catch (Exception exc) {}
/*     */           
/* 913 */           Dimension dimen = f.getPreferredSize();
/* 914 */           int w = dimen.width;
/* 915 */           h = dimen.height;
/* 916 */           this.desk.getDesktopManager().setBoundsForFrame(f, x, y, w, h);
/*     */         } 
/*     */       } else {
/* 919 */         Object obj = getFileWindow(cmd);
/* 920 */         if (obj != null) {
/* 921 */           FileWindow w = (FileWindow)obj;
/*     */           try {
/* 923 */             if (w.isIcon()) {
/* 924 */               w.setIcon(false);
/*     */             }
/* 926 */             w.setVisible(true);
/* 927 */             w.moveToFront();
/* 928 */             w.setSelected(true);
/* 929 */           } catch (Exception exc) {}
/*     */         } 
/*     */       } 
/*     */     } 
/* 933 */     if (returnValue != -1) {
/* 934 */       updateEnabled(false);
/* 935 */       this.dim.setReturnValue(returnValue);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\SwingGui.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */