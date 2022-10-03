/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
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
/*     */ public class ParameterizedCompletionChoicesWindow
/*     */   extends JWindow
/*     */ {
/*     */   private AutoCompletion ac;
/*     */   private JList<Completion> list;
/*     */   private DefaultListModel<Completion> model;
/*     */   private List<List<Completion>> choicesListList;
/*     */   private JScrollPane sp;
/*  72 */   private static final Comparator<Completion> SORT_BY_RELEVANCE_COMPARATOR = new SortByRelevanceComparator();
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
/*     */   public ParameterizedCompletionChoicesWindow(Window parent, AutoCompletion ac, final ParameterizedCompletionContext context) {
/*  87 */     super(parent);
/*  88 */     this.ac = ac;
/*  89 */     ComponentOrientation o = ac.getTextComponentOrientation();
/*     */     
/*  91 */     this.model = new DefaultListModel<>();
/*  92 */     this.list = new JList<>(this.model);
/*  93 */     if (ac.getParamChoicesRenderer() != null) {
/*  94 */       this.list.setCellRenderer(ac.getParamChoicesRenderer());
/*     */     }
/*  96 */     this.list.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  99 */             if (e.getClickCount() == 2) {
/* 100 */               context.insertSelectedChoice();
/*     */             }
/*     */           }
/*     */         });
/* 104 */     this.sp = new JScrollPane(this.list);
/*     */     
/* 106 */     setContentPane(this.sp);
/* 107 */     applyComponentOrientation(o);
/* 108 */     setFocusableWindowState(false);
/*     */ 
/*     */     
/* 111 */     PopupWindowDecorator decorator = PopupWindowDecorator.get();
/* 112 */     if (decorator != null) {
/* 113 */       decorator.decorate(this);
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
/*     */   public String getSelectedChoice() {
/* 126 */     Completion c = this.list.getSelectedValue();
/* 127 */     return (c == null) ? null : c.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incSelection(int amount) {
/* 137 */     int selection = this.list.getSelectedIndex();
/* 138 */     selection += amount;
/* 139 */     if (selection < 0) {
/*     */       
/* 141 */       selection = this.model.getSize() - 1;
/*     */     } else {
/*     */       
/* 144 */       selection %= this.model.getSize();
/*     */     } 
/* 146 */     this.list.setSelectedIndex(selection);
/* 147 */     this.list.ensureIndexIsVisible(selection);
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
/*     */   public void initialize(ParameterizedCompletion pc) {
/* 160 */     CompletionProvider provider = pc.getProvider();
/* 161 */     ParameterChoicesProvider pcp = provider.getParameterChoicesProvider();
/* 162 */     if (pcp == null) {
/* 163 */       this.choicesListList = null;
/*     */       
/*     */       return;
/*     */     } 
/* 167 */     int paramCount = pc.getParamCount();
/* 168 */     this.choicesListList = new ArrayList<>(paramCount);
/* 169 */     JTextComponent tc = this.ac.getTextComponent();
/*     */     
/* 171 */     for (int i = 0; i < paramCount; i++) {
/* 172 */       ParameterizedCompletion.Parameter param = pc.getParam(i);
/* 173 */       List<Completion> choices = pcp.getParameterChoices(tc, param);
/* 174 */       this.choicesListList.add(choices);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationRelativeTo(Rectangle r) {
/* 192 */     Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);
/*     */ 
/*     */ 
/*     */     
/* 196 */     int y = r.y + r.height + 5;
/*     */ 
/*     */ 
/*     */     
/* 200 */     int x = r.x;
/* 201 */     if (x < screenBounds.x) {
/* 202 */       x = screenBounds.x;
/*     */     }
/* 204 */     else if (x + getWidth() > screenBounds.x + screenBounds.width) {
/* 205 */       x = screenBounds.x + screenBounds.width - getWidth();
/*     */     } 
/*     */     
/* 208 */     setLocation(x, y);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameter(int param, String prefix) {
/* 225 */     this.model.clear();
/* 226 */     List<Completion> temp = new ArrayList<>();
/*     */     
/* 228 */     if (this.choicesListList != null && param >= 0 && param < this.choicesListList.size()) {
/*     */       
/* 230 */       List<Completion> choices = this.choicesListList.get(param);
/* 231 */       if (choices != null) {
/* 232 */         for (Completion completion : choices) {
/* 233 */           String choice = completion.getReplacementText();
/* 234 */           if (prefix == null || Util.startsWithIgnoreCase(choice, prefix)) {
/* 235 */             temp.add(completion);
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 241 */       Comparator<Completion> c = null;
/*     */       
/* 243 */       c = SORT_BY_RELEVANCE_COMPARATOR;
/*     */       
/* 245 */       temp.sort(c);
/* 246 */       for (Completion completion : temp) {
/* 247 */         this.model.addElement(completion);
/*     */       }
/*     */       
/* 250 */       int visibleRowCount = Math.min(this.model.size(), 10);
/* 251 */       this.list.setVisibleRowCount(visibleRowCount);
/*     */ 
/*     */       
/* 254 */       if (visibleRowCount == 0 && isVisible()) {
/* 255 */         setVisible(false);
/*     */       }
/* 257 */       else if (visibleRowCount > 0) {
/* 258 */         Dimension size = getPreferredSize();
/* 259 */         if (size.width < 150) {
/* 260 */           setSize(150, size.height);
/*     */         } else {
/*     */           
/* 263 */           pack();
/*     */         } 
/*     */         
/* 266 */         if (this.sp.getVerticalScrollBar() != null && this.sp
/* 267 */           .getVerticalScrollBar().isVisible()) {
/* 268 */           size = getSize();
/* 269 */           int w = size.width + this.sp.getVerticalScrollBar().getWidth() + 5;
/* 270 */           setSize(w, size.height);
/*     */         } 
/* 272 */         this.list.setSelectedIndex(0);
/* 273 */         this.list.ensureIndexIsVisible(0);
/* 274 */         if (!isVisible()) {
/* 275 */           setVisible(true);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 282 */       setVisible(false);
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
/*     */   public void setVisible(boolean visible) {
/* 295 */     if (visible != isVisible()) {
/*     */       
/* 297 */       if (visible && this.model.size() == 0) {
/*     */         return;
/*     */       }
/* 300 */       super.setVisible(visible);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 309 */     SwingUtilities.updateComponentTreeUI(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ParameterizedCompletionChoicesWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */