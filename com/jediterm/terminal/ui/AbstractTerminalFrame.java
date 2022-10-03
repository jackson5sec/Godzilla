/*     */ package com.jediterm.terminal.ui;
/*     */ import com.jediterm.terminal.RequestOrigin;
/*     */ import com.jediterm.terminal.TabbedTerminalWidget;
/*     */ import com.jediterm.terminal.TtyConnector;
/*     */ import com.jediterm.terminal.debug.BufferPanel;
/*     */ import com.jediterm.terminal.ui.settings.TabbedSettingsProvider;
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public abstract class AbstractTerminalFrame {
/*  24 */   public static final Logger LOG = Logger.getLogger(AbstractTerminalFrame.class);
/*     */   
/*     */   private JFrame myBufferFrame;
/*     */   
/*     */   private TerminalWidget myTerminal;
/*     */   
/*  30 */   private AbstractAction myOpenAction = new AbstractAction("New Session") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  32 */         AbstractTerminalFrame.this.openSession(AbstractTerminalFrame.this.myTerminal);
/*     */       }
/*     */     };
/*     */   
/*  36 */   private AbstractAction myShowBuffersAction = new AbstractAction("Show buffers") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  38 */         if (AbstractTerminalFrame.this.myBufferFrame == null) {
/*  39 */           AbstractTerminalFrame.this.showBuffers();
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*  44 */   private AbstractAction myDumpDimension = new AbstractAction("Dump terminal dimension") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  46 */         AbstractTerminalFrame.LOG.info(AbstractTerminalFrame.this.myTerminal.getTerminalDisplay().getColumnCount() + "x" + AbstractTerminalFrame.this
/*  47 */             .myTerminal.getTerminalDisplay().getRowCount());
/*     */       }
/*     */     };
/*     */   
/*  51 */   private AbstractAction myDumpSelection = new AbstractAction("Dump selection")
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/*  54 */         Pair<Point, Point> points = AbstractTerminalFrame.this.myTerminal.getTerminalDisplay().getSelection().pointsForRun(AbstractTerminalFrame.this.myTerminal.getTerminalDisplay().getColumnCount());
/*  55 */         AbstractTerminalFrame.LOG.info(AbstractTerminalFrame.this.myTerminal.getTerminalDisplay().getSelection() + " : '" + 
/*  56 */             SelectionUtil.getSelectionText((Point)points.first, (Point)points.second, AbstractTerminalFrame.this.myTerminal.getCurrentSession().getTerminalTextBuffer()) + "'");
/*     */       }
/*     */     };
/*     */   
/*  60 */   private AbstractAction myDumpCursorPosition = new AbstractAction("Dump cursor position") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  62 */         AbstractTerminalFrame.LOG.info(AbstractTerminalFrame.this.myTerminal.getCurrentSession().getTerminal().getCursorX() + "x" + AbstractTerminalFrame.this
/*  63 */             .myTerminal.getCurrentSession().getTerminal().getCursorY());
/*     */       }
/*     */     };
/*     */   
/*  67 */   private AbstractAction myCursor0x0 = new AbstractAction("1x1") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  69 */         AbstractTerminalFrame.this.myTerminal.getCurrentSession().getTerminal().cursorPosition(1, 1);
/*     */       }
/*     */     };
/*     */   
/*  73 */   private AbstractAction myCursor10x10 = new AbstractAction("10x10") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  75 */         AbstractTerminalFrame.this.myTerminal.getCurrentSession().getTerminal().cursorPosition(10, 10);
/*     */       }
/*     */     };
/*     */   
/*  79 */   private AbstractAction myCursor80x24 = new AbstractAction("80x24") {
/*     */       public void actionPerformed(ActionEvent e) {
/*  81 */         AbstractTerminalFrame.this.myTerminal.getCurrentSession().getTerminal().cursorPosition(80, 24);
/*     */       }
/*     */     };
/*     */   
/*     */   private JMenuBar getJMenuBar() {
/*  86 */     JMenuBar mb = new JMenuBar();
/*  87 */     JMenu m = new JMenu("File");
/*     */     
/*  89 */     m.add(this.myOpenAction);
/*  90 */     mb.add(m);
/*  91 */     JMenu dm = new JMenu("Debug");
/*     */     
/*  93 */     JMenu logLevel = new JMenu("Set log level ...");
/*  94 */     Level[] levels = { Level.ALL, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL, Level.OFF };
/*  95 */     for (Level l : levels) {
/*  96 */       logLevel.add(new AbstractAction(l.toString())
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/*  99 */               Logger.getRootLogger().setLevel(l);
/*     */             }
/*     */           });
/*     */     } 
/* 103 */     dm.add(logLevel);
/* 104 */     dm.addSeparator();
/*     */     
/* 106 */     dm.add(this.myShowBuffersAction);
/* 107 */     dm.addSeparator();
/* 108 */     dm.add(this.myDumpDimension);
/* 109 */     dm.add(this.myDumpSelection);
/* 110 */     dm.add(this.myDumpCursorPosition);
/*     */     
/* 112 */     JMenu cursorPosition = new JMenu("Set cursor position ...");
/* 113 */     cursorPosition.add(this.myCursor0x0);
/* 114 */     cursorPosition.add(this.myCursor10x10);
/* 115 */     cursorPosition.add(this.myCursor80x24);
/* 116 */     dm.add(cursorPosition);
/* 117 */     mb.add(dm);
/*     */     
/* 119 */     return mb;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected JediTermWidget openSession(TerminalWidget terminal) {
/* 124 */     if (terminal.canOpenSession()) {
/* 125 */       return openSession(terminal, createTtyConnector());
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */   
/*     */   public JediTermWidget openSession(TerminalWidget terminal, TtyConnector ttyConnector) {
/* 131 */     JediTermWidget session = terminal.createTerminalSession(ttyConnector);
/* 132 */     session.start();
/* 133 */     return session;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractTerminalFrame() {
/* 139 */     this.myTerminal = createTabbedTerminalWidget();
/*     */     
/* 141 */     final JFrame frame = new JFrame("JediTerm");
/*     */     
/* 143 */     frame.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/* 146 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/* 150 */     JMenuBar mb = getJMenuBar();
/* 151 */     frame.setJMenuBar(mb);
/* 152 */     sizeFrameForTerm(frame);
/* 153 */     frame.getContentPane().add("Center", this.myTerminal.getComponent());
/*     */     
/* 155 */     frame.pack();
/* 156 */     frame.setLocationByPlatform(true);
/* 157 */     frame.setVisible(true);
/*     */     
/* 159 */     frame.setResizable(true);
/*     */     
/* 161 */     this.myTerminal.setTerminalPanelListener(new TerminalPanelListener() {
/*     */           public void onPanelResize(@NotNull RequestOrigin origin) {
/* 163 */             if (origin == null) $$$reportNull$$$0(0);  if (origin == RequestOrigin.Remote) {
/* 164 */               AbstractTerminalFrame.this.sizeFrameForTerm(frame);
/*     */             }
/* 166 */             frame.pack();
/*     */           }
/*     */ 
/*     */           
/*     */           public void onSessionChanged(TerminalSession currentSession) {
/* 171 */             frame.setTitle(currentSession.getSessionName());
/*     */           }
/*     */ 
/*     */           
/*     */           public void onTitleChanged(String title) {
/* 176 */             frame.setTitle(AbstractTerminalFrame.this.myTerminal.getCurrentSession().getSessionName());
/*     */           }
/*     */         });
/*     */     
/* 180 */     openSession(this.myTerminal);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   protected AbstractTabbedTerminalWidget createTabbedTerminalWidget() {
/* 185 */     return (AbstractTabbedTerminalWidget)new TabbedTerminalWidget((TabbedSettingsProvider)new DefaultTabbedSettingsProvider(), this::openSession)
/*     */       {
/*     */         public JediTermWidget createInnerTerminalWidget() {
/* 188 */           return AbstractTerminalFrame.this.createTerminalWidget(getSettingsProvider());
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected JediTermWidget createTerminalWidget(@NotNull TabbedSettingsProvider settingsProvider) {
/* 194 */     if (settingsProvider == null) $$$reportNull$$$0(0);  return new JediTermWidget((SettingsProvider)settingsProvider);
/*     */   }
/*     */   
/*     */   private void sizeFrameForTerm(final JFrame frame) {
/* 198 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run() {
/* 201 */             Dimension d = AbstractTerminalFrame.this.myTerminal.getPreferredSize();
/*     */             
/* 203 */             d.width += frame.getWidth() - frame.getContentPane().getWidth();
/* 204 */             d.height += frame.getHeight() - frame.getContentPane().getHeight();
/* 205 */             frame.setSize(d);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void showBuffers() {
/* 211 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 213 */             AbstractTerminalFrame.this.myBufferFrame = new JFrame("buffers");
/* 214 */             BufferPanel bufferPanel = new BufferPanel(AbstractTerminalFrame.this.myTerminal.getCurrentSession());
/*     */             
/* 216 */             AbstractTerminalFrame.this.myBufferFrame.getContentPane().add((Component)bufferPanel);
/* 217 */             AbstractTerminalFrame.this.myBufferFrame.pack();
/* 218 */             AbstractTerminalFrame.this.myBufferFrame.setLocationByPlatform(true);
/* 219 */             AbstractTerminalFrame.this.myBufferFrame.setVisible(true);
/* 220 */             AbstractTerminalFrame.this.myBufferFrame.setSize(800, 600);
/*     */             
/* 222 */             AbstractTerminalFrame.this.myBufferFrame.addWindowListener(new WindowAdapter()
/*     */                 {
/*     */                   public void windowClosing(WindowEvent e) {
/* 225 */                     AbstractTerminalFrame.this.myBufferFrame = null;
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public abstract TtyConnector createTtyConnector();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\AbstractTerminalFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */