/*      */ package com.formdev.flatlaf.extras;
/*      */ import com.formdev.flatlaf.icons.FlatAbstractIcon;
/*      */ import com.formdev.flatlaf.ui.FlatLineBorder;
/*      */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*      */ import com.formdev.flatlaf.util.GrayFilter;
/*      */ import com.formdev.flatlaf.util.HSLColor;
/*      */ import com.formdev.flatlaf.util.ScaledEmptyBorder;
/*      */ import com.formdev.flatlaf.util.UIScale;
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.GraphicsDevice;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.datatransfer.StringSelection;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.prefs.Preferences;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SortOrder;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ 
/*      */ public class FlatUIDefaultsInspector {
/*      */   private static final int KEY_MODIFIERS_MASK = 960;
/*   77 */   private final PropertyChangeListener lafListener = this::lafChanged; private static JFrame inspectorFrame;
/*   78 */   private final PropertyChangeListener lafDefaultsListener = this::lafDefaultsChanged; private boolean refreshPending; private Properties derivedColorKeys; private JPanel panel; private JPanel filterPanel; private JLabel flterLabel; private JTextField filterField;
/*      */   private JLabel valueTypeLabel;
/*      */   private JComboBox<String> valueTypeField;
/*      */   private JScrollPane scrollPane;
/*      */   private JTable table;
/*      */   private JPopupMenu tablePopupMenu;
/*      */   private JMenuItem copyKeyMenuItem;
/*      */   private JMenuItem copyValueMenuItem;
/*      */   private JMenuItem copyKeyAndValueMenuItem;
/*      */   
/*      */   public static void install(String activationKeys) {
/*   89 */     KeyStroke keyStroke = KeyStroke.getKeyStroke(activationKeys);
/*   90 */     Toolkit.getDefaultToolkit().addAWTEventListener(e -> { if (e.getID() == 402 && ((KeyEvent)e).getKeyCode() == keyStroke.getKeyCode() && (((KeyEvent)e).getModifiersEx() & 0x3C0) == (keyStroke.getModifiers() & 0x3C0)) show();  }8L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void show() {
/*  101 */     if (inspectorFrame != null) {
/*  102 */       ensureOnScreen(inspectorFrame);
/*  103 */       inspectorFrame.toFront();
/*      */       
/*      */       return;
/*      */     } 
/*  107 */     inspectorFrame = (new FlatUIDefaultsInspector()).createFrame();
/*  108 */     inspectorFrame.setVisible(true);
/*      */   }
/*      */   
/*      */   public static void hide() {
/*  112 */     if (inspectorFrame != null) {
/*  113 */       inspectorFrame.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static JComponent createInspectorPanel() {
/*  120 */     return (new FlatUIDefaultsInspector()).panel;
/*      */   }
/*      */   
/*      */   private FlatUIDefaultsInspector() {
/*  124 */     initComponents();
/*      */     
/*  126 */     this.panel.setBorder((Border)new ScaledEmptyBorder(10, 10, 10, 10));
/*  127 */     this.filterPanel.setBorder((Border)new ScaledEmptyBorder(0, 0, 10, 0));
/*      */ 
/*      */     
/*  130 */     this.filterField.getDocument().addDocumentListener(new DocumentListener()
/*      */         {
/*      */           public void removeUpdate(DocumentEvent e) {
/*  133 */             FlatUIDefaultsInspector.this.filterChanged();
/*      */           }
/*      */           
/*      */           public void insertUpdate(DocumentEvent e) {
/*  137 */             FlatUIDefaultsInspector.this.filterChanged();
/*      */           }
/*      */           
/*      */           public void changedUpdate(DocumentEvent e) {
/*  141 */             FlatUIDefaultsInspector.this.filterChanged();
/*      */           }
/*      */         });
/*  144 */     delegateKey(38, "unitScrollUp");
/*  145 */     delegateKey(40, "unitScrollDown");
/*  146 */     delegateKey(33, "scrollUp");
/*  147 */     delegateKey(34, "scrollDown");
/*      */ 
/*      */     
/*  150 */     this.table.setModel(new ItemsTableModel(getUIDefaultsItems()));
/*  151 */     this.table.setDefaultRenderer(String.class, new KeyRenderer());
/*  152 */     this.table.setDefaultRenderer(Item.class, new ValueRenderer());
/*  153 */     this.table.getRowSorter().setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
/*      */ 
/*      */ 
/*      */     
/*  157 */     Preferences prefs = getPrefs();
/*  158 */     TableColumnModel columnModel = this.table.getColumnModel();
/*  159 */     columnModel.getColumn(0).setPreferredWidth(prefs.getInt("column1width", 100));
/*  160 */     columnModel.getColumn(1).setPreferredWidth(prefs.getInt("column2width", 100));
/*      */     
/*  162 */     PropertyChangeListener columnWidthListener = e -> {
/*      */         if ("width".equals(e.getPropertyName())) {
/*      */           prefs.putInt("column1width", columnModel.getColumn(0).getWidth());
/*      */           prefs.putInt("column2width", columnModel.getColumn(1).getWidth());
/*      */         } 
/*      */       };
/*  168 */     columnModel.getColumn(0).addPropertyChangeListener(columnWidthListener);
/*  169 */     columnModel.getColumn(1).addPropertyChangeListener(columnWidthListener);
/*      */ 
/*      */     
/*  172 */     String filter = prefs.get("filter", "");
/*  173 */     String valueType = prefs.get("valueType", null);
/*  174 */     if (filter != null && !filter.isEmpty())
/*  175 */       this.filterField.setText(filter); 
/*  176 */     if (valueType != null) {
/*  177 */       this.valueTypeField.setSelectedItem(valueType);
/*      */     }
/*  179 */     this.panel.addPropertyChangeListener("ancestor", e -> {
/*      */           if (e.getNewValue() != null) {
/*      */             UIManager.addPropertyChangeListener(this.lafListener);
/*      */             
/*      */             UIManager.getDefaults().addPropertyChangeListener(this.lafDefaultsListener);
/*      */           } else {
/*      */             UIManager.removePropertyChangeListener(this.lafListener);
/*      */             
/*      */             UIManager.getDefaults().removePropertyChangeListener(this.lafDefaultsListener);
/*      */           } 
/*      */         });
/*  190 */     this.panel.registerKeyboardAction(e -> refresh(), 
/*      */         
/*  192 */         KeyStroke.getKeyStroke(116, 0, false), 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private JFrame createFrame() {
/*  197 */     final JFrame frame = new JFrame();
/*  198 */     frame.setTitle("UI Defaults Inspector");
/*  199 */     frame.setDefaultCloseOperation(2);
/*  200 */     frame.addWindowListener(new WindowAdapter()
/*      */         {
/*      */           public void windowClosed(WindowEvent e) {
/*  203 */             FlatUIDefaultsInspector.inspectorFrame = null;
/*      */           }
/*      */           
/*      */           public void windowClosing(WindowEvent e) {
/*  207 */             FlatUIDefaultsInspector.this.saveWindowBounds(frame);
/*      */           }
/*      */           
/*      */           public void windowDeactivated(WindowEvent e) {
/*  211 */             FlatUIDefaultsInspector.this.saveWindowBounds(frame);
/*      */           }
/*      */         });
/*      */     
/*  215 */     updateWindowTitle(frame);
/*      */     
/*  217 */     frame.getContentPane().add(this.panel, "Center");
/*      */ 
/*      */     
/*  220 */     Preferences prefs = getPrefs();
/*  221 */     int x = prefs.getInt("x", -1);
/*  222 */     int y = prefs.getInt("y", -1);
/*  223 */     int width = prefs.getInt("width", UIScale.scale(600));
/*  224 */     int height = prefs.getInt("height", UIScale.scale(800));
/*  225 */     frame.setSize(width, height);
/*  226 */     if (x != -1 && y != -1) {
/*  227 */       frame.setLocation(x, y);
/*  228 */       ensureOnScreen(frame);
/*      */     } else {
/*  230 */       frame.setLocationRelativeTo((Component)null);
/*      */     } 
/*      */     
/*  233 */     ((JComponent)frame.getContentPane()).registerKeyboardAction(e -> frame.dispose(), 
/*      */         
/*  235 */         KeyStroke.getKeyStroke(27, 0, false), 1);
/*      */ 
/*      */     
/*  238 */     return frame;
/*      */   }
/*      */   
/*      */   private void delegateKey(int keyCode, final String actionKey) {
/*  242 */     KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);
/*  243 */     String actionMapKey = "delegate-" + actionKey;
/*      */     
/*  245 */     this.filterField.getInputMap().put(keyStroke, actionMapKey);
/*  246 */     this.filterField.getActionMap().put(actionMapKey, new AbstractAction()
/*      */         {
/*      */           public void actionPerformed(ActionEvent e) {
/*  249 */             Action action = FlatUIDefaultsInspector.this.scrollPane.getActionMap().get(actionKey);
/*  250 */             if (action != null) {
/*  251 */               action.actionPerformed(new ActionEvent(FlatUIDefaultsInspector.this.scrollPane, e
/*  252 */                     .getID(), actionKey, e.getWhen(), e.getModifiers()));
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private static void ensureOnScreen(JFrame frame) {
/*  259 */     Rectangle frameBounds = frame.getBounds();
/*  260 */     boolean onScreen = false;
/*  261 */     for (GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
/*  262 */       GraphicsConfiguration gc = screen.getDefaultConfiguration();
/*  263 */       Rectangle screenBounds = FlatUIUtils.subtractInsets(gc.getBounds(), 
/*  264 */           Toolkit.getDefaultToolkit().getScreenInsets(gc));
/*  265 */       if (frameBounds.intersects(screenBounds)) {
/*  266 */         onScreen = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  271 */     if (!onScreen)
/*  272 */       frame.setLocationRelativeTo((Component)null); 
/*      */   }
/*      */   
/*      */   private void lafChanged(PropertyChangeEvent e) {
/*  276 */     if ("lookAndFeel".equals(e.getPropertyName()))
/*  277 */       refresh(); 
/*      */   }
/*      */   
/*      */   private void lafDefaultsChanged(PropertyChangeEvent e) {
/*  281 */     if (this.refreshPending) {
/*      */       return;
/*      */     }
/*  284 */     this.refreshPending = true;
/*  285 */     EventQueue.invokeLater(() -> {
/*      */           refresh();
/*      */           this.refreshPending = false;
/*      */         });
/*      */   }
/*      */   
/*      */   private void refresh() {
/*  292 */     ItemsTableModel model = (ItemsTableModel)this.table.getModel();
/*  293 */     model.setItems(getUIDefaultsItems());
/*      */     
/*  295 */     JFrame frame = (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this.panel);
/*  296 */     if (frame != null)
/*  297 */       updateWindowTitle(frame); 
/*      */   }
/*      */   
/*      */   private Item[] getUIDefaultsItems() {
/*  301 */     UIDefaults defaults = UIManager.getDefaults();
/*  302 */     UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
/*      */     
/*  304 */     Set<Map.Entry<Object, Object>> defaultsSet = defaults.entrySet();
/*  305 */     ArrayList<Item> items = new ArrayList<>(defaultsSet.size());
/*  306 */     HashSet<Object> keys = new HashSet(defaultsSet.size());
/*  307 */     Color[] pBaseColor = new Color[1];
/*  308 */     for (Map.Entry<Object, Object> e : defaultsSet) {
/*  309 */       Object key = e.getKey();
/*      */ 
/*      */       
/*  312 */       if (!(key instanceof String)) {
/*      */         continue;
/*      */       }
/*      */       
/*  316 */       Object value = defaults.get(key);
/*  317 */       if (value instanceof Class) {
/*      */         continue;
/*      */       }
/*      */       
/*  321 */       if (!keys.add(key)) {
/*      */         continue;
/*      */       }
/*      */       
/*  325 */       if (value instanceof com.formdev.flatlaf.util.DerivedColor) {
/*  326 */         Color resolvedColor = resolveDerivedColor(defaults, (String)key, (Color)value, pBaseColor);
/*  327 */         if (resolvedColor != value) {
/*  328 */           value = new Color[] { resolvedColor, pBaseColor[0], (Color)value };
/*      */         }
/*      */       } 
/*      */       
/*  332 */       Object lafValue = null;
/*  333 */       if (defaults.containsKey(key)) {
/*  334 */         lafValue = lafDefaults.get(key);
/*      */       }
/*      */       
/*  337 */       items.add(new Item(String.valueOf(key), value, lafValue));
/*      */     } 
/*      */     
/*  340 */     return items.<Item>toArray(new Item[items.size()]);
/*      */   }
/*      */   
/*      */   private Color resolveDerivedColor(UIDefaults defaults, String key, Color color, Color[] pBaseColor) {
/*  344 */     if (pBaseColor != null) {
/*  345 */       pBaseColor[0] = null;
/*      */     }
/*  347 */     if (!(color instanceof com.formdev.flatlaf.util.DerivedColor)) {
/*  348 */       return color;
/*      */     }
/*  350 */     if (this.derivedColorKeys == null) {
/*  351 */       this.derivedColorKeys = loadDerivedColorKeys();
/*      */     }
/*  353 */     Object baseKey = this.derivedColorKeys.get(key);
/*  354 */     if (baseKey == null) {
/*  355 */       return color;
/*      */     }
/*      */     
/*  358 */     if ("null".equals(baseKey)) {
/*  359 */       return color;
/*      */     }
/*  361 */     Color baseColor = defaults.getColor(baseKey);
/*  362 */     if (baseColor == null) {
/*  363 */       return color;
/*      */     }
/*  365 */     if (baseColor instanceof com.formdev.flatlaf.util.DerivedColor) {
/*  366 */       baseColor = resolveDerivedColor(defaults, (String)baseKey, baseColor, null);
/*      */     }
/*  368 */     if (pBaseColor != null) {
/*  369 */       pBaseColor[0] = baseColor;
/*      */     }
/*  371 */     Color newColor = FlatUIUtils.deriveColor(color, baseColor);
/*      */ 
/*      */ 
/*      */     
/*  375 */     return new Color(newColor.getRGB(), true);
/*      */   }
/*      */   
/*      */   private Properties loadDerivedColorKeys() {
/*  379 */     Properties properties = new Properties();
/*  380 */     try (InputStream in = getClass().getResourceAsStream("/com/formdev/flatlaf/extras/resources/DerivedColorKeys.properties")) {
/*  381 */       properties.load(in);
/*  382 */     } catch (IOException ex) {
/*  383 */       ex.printStackTrace();
/*      */     } 
/*  385 */     return properties;
/*      */   }
/*      */   
/*      */   private static void updateWindowTitle(JFrame frame) {
/*  389 */     String title = frame.getTitle();
/*  390 */     String sep = "  -  ";
/*  391 */     int sepIndex = title.indexOf(sep);
/*  392 */     if (sepIndex >= 0)
/*  393 */       title = title.substring(0, sepIndex); 
/*  394 */     frame.setTitle(title + sep + UIManager.getLookAndFeel().getName());
/*      */   }
/*      */   
/*      */   private void saveWindowBounds(JFrame frame) {
/*  398 */     Preferences prefs = getPrefs();
/*  399 */     prefs.putInt("x", frame.getX());
/*  400 */     prefs.putInt("y", frame.getY());
/*  401 */     prefs.putInt("width", frame.getWidth());
/*  402 */     prefs.putInt("height", frame.getHeight());
/*      */   }
/*      */   
/*      */   private Preferences getPrefs() {
/*  406 */     return Preferences.userRoot().node("flatlaf-uidefaults-inspector");
/*      */   }
/*      */   
/*      */   private void filterChanged() {
/*  410 */     String filter = this.filterField.getText().trim();
/*  411 */     String valueType = (String)this.valueTypeField.getSelectedItem();
/*      */ 
/*      */     
/*  414 */     String[] filters = !filter.isEmpty() ? filter.split(" +") : null;
/*  415 */     Pattern[] patterns = (filters != null) ? new Pattern[filters.length] : null;
/*  416 */     if (filters != null) {
/*  417 */       for (int i = 0; i < filters.length; i++) {
/*  418 */         filters[i] = filters[i].toLowerCase(Locale.ENGLISH);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  425 */         String f = filters[i];
/*  426 */         boolean matchBeginning = f.startsWith("^");
/*  427 */         boolean matchEnd = f.endsWith("$");
/*  428 */         if (f.indexOf('*') >= 0 || f.indexOf('?') >= 0 || matchBeginning || matchEnd) {
/*  429 */           if (matchBeginning)
/*  430 */             f = f.substring(1); 
/*  431 */           if (matchEnd) {
/*  432 */             f = f.substring(0, f.length() - 1);
/*      */           }
/*  434 */           String regex = ("\\Q" + f + "\\E").replace("*", "\\E.*\\Q").replace("?", "\\E.\\Q");
/*  435 */           if (!matchBeginning)
/*  436 */             regex = ".*" + regex; 
/*  437 */           if (!matchEnd)
/*  438 */             regex = regex + ".*"; 
/*  439 */           patterns[i] = Pattern.compile(regex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  444 */     ItemsTableModel model = (ItemsTableModel)this.table.getModel();
/*  445 */     model.setFilter(item -> {
/*      */           if (valueType != null && !valueType.equals("(any)") && !valueType.equals(typeOfValue(item.value))) {
/*      */             return false;
/*      */           }
/*      */           
/*      */           if (filters == null) {
/*      */             return true;
/*      */           }
/*      */           String lkey = item.key.toLowerCase(Locale.ENGLISH);
/*      */           String lvalue = item.getValueAsString().toLowerCase(Locale.ENGLISH);
/*      */           for (int i = 0; i < filters.length; i++) {
/*      */             Pattern p = patterns[i];
/*      */             if (p != null) {
/*      */               if (p.matcher(lkey).matches() || p.matcher(lvalue).matches()) {
/*      */                 return true;
/*      */               }
/*      */             } else {
/*      */               String f = filters[i];
/*      */               if (lkey.contains(f) || lvalue.contains(f)) {
/*      */                 return true;
/*      */               }
/*      */             } 
/*      */           } 
/*      */           return false;
/*      */         });
/*  470 */     Preferences prefs = getPrefs();
/*  471 */     prefs.put("filter", filter);
/*  472 */     prefs.put("valueType", valueType);
/*      */   }
/*      */   
/*      */   private String typeOfValue(Object value) {
/*  476 */     if (value instanceof Boolean)
/*  477 */       return "Boolean"; 
/*  478 */     if (value instanceof Border)
/*  479 */       return "Border"; 
/*  480 */     if (value instanceof Color || value instanceof Color[])
/*  481 */       return "Color"; 
/*  482 */     if (value instanceof Dimension)
/*  483 */       return "Dimension"; 
/*  484 */     if (value instanceof Float)
/*  485 */       return "Float"; 
/*  486 */     if (value instanceof Font)
/*  487 */       return "Font"; 
/*  488 */     if (value instanceof Icon)
/*  489 */       return "Icon"; 
/*  490 */     if (value instanceof Insets)
/*  491 */       return "Insets"; 
/*  492 */     if (value instanceof Integer)
/*  493 */       return "Integer"; 
/*  494 */     if (value instanceof String)
/*  495 */       return "String"; 
/*  496 */     return "(other)";
/*      */   }
/*      */   
/*      */   private void tableMousePressed(MouseEvent e) {
/*  500 */     if (!SwingUtilities.isRightMouseButton(e)) {
/*      */       return;
/*      */     }
/*  503 */     int row = this.table.rowAtPoint(e.getPoint());
/*  504 */     if (row >= 0 && !this.table.isRowSelected(row))
/*  505 */       this.table.setRowSelectionInterval(row, row); 
/*      */   }
/*      */   
/*      */   private void copyKey() {
/*  509 */     copyToClipboard(0);
/*      */   }
/*      */   
/*      */   private void copyValue() {
/*  513 */     copyToClipboard(1);
/*      */   }
/*      */   
/*      */   private void copyKeyAndValue() {
/*  517 */     copyToClipboard(-1);
/*      */   }
/*      */   
/*      */   private void copyToClipboard(int column) {
/*  521 */     int[] rows = this.table.getSelectedRows();
/*  522 */     if (rows.length == 0) {
/*      */       return;
/*      */     }
/*  525 */     StringBuilder buf = new StringBuilder();
/*  526 */     for (int i = 0; i < rows.length; i++) {
/*  527 */       if (i > 0) {
/*  528 */         buf.append('\n');
/*      */       }
/*  530 */       if (column < 0 || column == 0)
/*  531 */         buf.append(this.table.getValueAt(rows[i], 0)); 
/*  532 */       if (column < 0)
/*  533 */         buf.append(" = "); 
/*  534 */       if (column < 0 || column == 1) {
/*  535 */         buf.append(this.table.getValueAt(rows[i], 1));
/*      */       }
/*      */     } 
/*  538 */     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(buf
/*  539 */           .toString()), null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void initComponents() {
/*  544 */     this.panel = new JPanel();
/*  545 */     this.filterPanel = new JPanel();
/*  546 */     this.flterLabel = new JLabel();
/*  547 */     this.filterField = new JTextField();
/*  548 */     this.valueTypeLabel = new JLabel();
/*  549 */     this.valueTypeField = new JComboBox<>();
/*  550 */     this.scrollPane = new JScrollPane();
/*  551 */     this.table = new JTable();
/*  552 */     this.tablePopupMenu = new JPopupMenu();
/*  553 */     this.copyKeyMenuItem = new JMenuItem();
/*  554 */     this.copyValueMenuItem = new JMenuItem();
/*  555 */     this.copyKeyAndValueMenuItem = new JMenuItem();
/*      */ 
/*      */ 
/*      */     
/*  559 */     this.panel.setLayout(new BorderLayout());
/*      */ 
/*      */ 
/*      */     
/*  563 */     this.filterPanel.setLayout(new GridBagLayout());
/*  564 */     ((GridBagLayout)this.filterPanel.getLayout()).columnWidths = new int[] { 0, 0, 0, 0, 0 };
/*  565 */     ((GridBagLayout)this.filterPanel.getLayout()).rowHeights = new int[] { 0, 0 };
/*  566 */     ((GridBagLayout)this.filterPanel.getLayout()).columnWeights = new double[] { 0.0D, 1.0D, 0.0D, 0.0D, 1.0E-4D };
/*  567 */     ((GridBagLayout)this.filterPanel.getLayout()).rowWeights = new double[] { 0.0D, 1.0E-4D };
/*      */ 
/*      */     
/*  570 */     this.flterLabel.setText("Filter:");
/*  571 */     this.flterLabel.setLabelFor(this.filterField);
/*  572 */     this.flterLabel.setDisplayedMnemonic('F');
/*  573 */     this.filterPanel.add(this.flterLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 10), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  578 */     this.filterField.putClientProperty("JTextField.placeholderText", "enter one or more filter strings, separated by space characters");
/*  579 */     this.filterPanel.add(this.filterField, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 10), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  584 */     this.valueTypeLabel.setText("Value Type:");
/*  585 */     this.valueTypeLabel.setLabelFor(this.valueTypeField);
/*  586 */     this.valueTypeLabel.setDisplayedMnemonic('T');
/*  587 */     this.filterPanel.add(this.valueTypeLabel, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 10), 0, 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  592 */     this.valueTypeField.setModel(new DefaultComboBoxModel<>(new String[] { "(any)", "Boolean", "Border", "Color", "Dimension", "Float", "Font", "Icon", "Insets", "Integer", "String", "(other)" }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  606 */     this.valueTypeField.addActionListener(e -> filterChanged());
/*  607 */     this.filterPanel.add(this.valueTypeField, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */ 
/*      */ 
/*      */     
/*  611 */     this.panel.add(this.filterPanel, "North");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  617 */     this.table.setAutoCreateRowSorter(true);
/*  618 */     this.table.setComponentPopupMenu(this.tablePopupMenu);
/*  619 */     this.table.addMouseListener(new MouseAdapter()
/*      */         {
/*      */           public void mousePressed(MouseEvent e) {
/*  622 */             FlatUIDefaultsInspector.this.tableMousePressed(e);
/*      */           }
/*      */         });
/*  625 */     this.scrollPane.setViewportView(this.table);
/*      */     
/*  627 */     this.panel.add(this.scrollPane, "Center");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     this.copyKeyMenuItem.setText("Copy Key");
/*  635 */     this.copyKeyMenuItem.addActionListener(e -> copyKey());
/*  636 */     this.tablePopupMenu.add(this.copyKeyMenuItem);
/*      */ 
/*      */     
/*  639 */     this.copyValueMenuItem.setText("Copy Value");
/*  640 */     this.copyValueMenuItem.addActionListener(e -> copyValue());
/*  641 */     this.tablePopupMenu.add(this.copyValueMenuItem);
/*      */ 
/*      */     
/*  644 */     this.copyKeyAndValueMenuItem.setText("Copy Key and Value");
/*  645 */     this.copyKeyAndValueMenuItem.addActionListener(e -> copyKeyAndValue());
/*  646 */     this.tablePopupMenu.add(this.copyKeyAndValueMenuItem);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Item
/*      */   {
/*      */     final String key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Object value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Object lafValue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String valueStr;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Item(String key, Object value, Object lafValue) {
/*  676 */       this.key = key;
/*  677 */       this.value = value;
/*  678 */       this.lafValue = lafValue;
/*      */     }
/*      */     
/*      */     String getValueAsString() {
/*  682 */       if (this.valueStr == null)
/*  683 */         this.valueStr = valueAsString(this.value); 
/*  684 */       return this.valueStr;
/*      */     }
/*      */     
/*      */     static String valueAsString(Object value) {
/*  688 */       if (value instanceof Color || value instanceof Color[]) {
/*  689 */         Color color = (value instanceof Color[]) ? ((Color[])value)[0] : (Color)value;
/*  690 */         HSLColor hslColor = new HSLColor(color);
/*  691 */         if (color.getAlpha() == 255)
/*  692 */           return String.format("%-9s HSL %3d %3d %3d", new Object[] {
/*  693 */                 color2hex(color), 
/*  694 */                 Integer.valueOf((int)hslColor.getHue()), Integer.valueOf((int)hslColor.getSaturation()), 
/*  695 */                 Integer.valueOf((int)hslColor.getLuminance())
/*      */               }); 
/*  697 */         return String.format("%-9s HSL %3d %3d %3d %2d", new Object[] {
/*  698 */               color2hex(color), 
/*  699 */               Integer.valueOf((int)hslColor.getHue()), Integer.valueOf((int)hslColor.getSaturation()), 
/*  700 */               Integer.valueOf((int)hslColor.getLuminance()), Integer.valueOf((int)(hslColor.getAlpha() * 100.0F)) });
/*      */       } 
/*  702 */       if (value instanceof Insets) {
/*  703 */         Insets insets = (Insets)value;
/*  704 */         return insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right;
/*  705 */       }  if (value instanceof Dimension) {
/*  706 */         Dimension dim = (Dimension)value;
/*  707 */         return dim.width + "," + dim.height;
/*  708 */       }  if (value instanceof Font) {
/*  709 */         Font font = (Font)value;
/*  710 */         String s = font.getFamily() + " " + font.getSize();
/*  711 */         if (font.isBold())
/*  712 */           s = s + " bold"; 
/*  713 */         if (font.isItalic())
/*  714 */           s = s + " italic"; 
/*  715 */         return s;
/*  716 */       }  if (value instanceof Icon) {
/*  717 */         Icon icon = (Icon)value;
/*  718 */         return icon.getIconWidth() + "x" + icon.getIconHeight() + "   " + icon.getClass().getName();
/*  719 */       }  if (value instanceof Border) {
/*  720 */         Border border = (Border)value;
/*  721 */         if (border instanceof FlatLineBorder) {
/*  722 */           FlatLineBorder lineBorder = (FlatLineBorder)border;
/*  723 */           return valueAsString(lineBorder.getUnscaledBorderInsets()) + "  " + 
/*  724 */             color2hex(lineBorder.getLineColor()) + "  " + lineBorder
/*  725 */             .getLineThickness() + "    " + border
/*  726 */             .getClass().getName();
/*  727 */         }  if (border instanceof EmptyBorder) {
/*      */ 
/*      */           
/*  730 */           Insets insets = (border instanceof FlatEmptyBorder) ? ((FlatEmptyBorder)border).getUnscaledBorderInsets() : ((EmptyBorder)border).getBorderInsets();
/*  731 */           return valueAsString(insets) + "    " + border.getClass().getName();
/*  732 */         }  if (border instanceof com.formdev.flatlaf.ui.FlatBorder || border instanceof com.formdev.flatlaf.ui.FlatMarginBorder) {
/*  733 */           return border.getClass().getName();
/*      */         }
/*  735 */         return String.valueOf(value);
/*  736 */       }  if (value instanceof GrayFilter) {
/*  737 */         GrayFilter grayFilter = (GrayFilter)value;
/*  738 */         return grayFilter.getBrightness() + "," + grayFilter.getContrast() + " " + grayFilter
/*  739 */           .getAlpha() + "    " + grayFilter.getClass().getName();
/*  740 */       }  if (value instanceof ActionMap) {
/*  741 */         ActionMap actionMap = (ActionMap)value;
/*  742 */         return "ActionMap (" + actionMap.size() + ")";
/*  743 */       }  if (value instanceof InputMap) {
/*  744 */         InputMap inputMap = (InputMap)value;
/*  745 */         return "InputMap (" + inputMap.size() + ")";
/*  746 */       }  if (value instanceof Object[])
/*  747 */         return Arrays.toString((Object[])value); 
/*  748 */       if (value instanceof int[]) {
/*  749 */         return Arrays.toString((int[])value);
/*      */       }
/*  751 */       return String.valueOf(value);
/*      */     }
/*      */     
/*      */     private static String color2hex(Color color) {
/*  755 */       int rgb = color.getRGB();
/*  756 */       boolean hasAlpha = (color.getAlpha() != 255);
/*      */       
/*  758 */       boolean useShortFormat = ((rgb & 0xF0000000) == (rgb & 0xF000000) << 4 && (rgb & 0xF00000) == (rgb & 0xF0000) << 4 && (rgb & 0xF000) == (rgb & 0xF00) << 4 && (rgb & 0xF0) == (rgb & 0xF) << 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  764 */       if (useShortFormat) {
/*  765 */         int srgb = (rgb & 0xF0000) >> 8 | (rgb & 0xF00) >> 4 | rgb & 0xF;
/*  766 */         return String.format(hasAlpha ? "#%03X%X" : "#%03X", new Object[] { Integer.valueOf(srgb), Integer.valueOf(rgb >> 24 & 0xF) });
/*      */       } 
/*  768 */       return String.format(hasAlpha ? "#%06X%02X" : "#%06X", new Object[] { Integer.valueOf(rgb & 0xFFFFFF), Integer.valueOf(rgb >> 24 & 0xFF) });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  774 */       return getValueAsString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ItemsTableModel
/*      */     extends AbstractTableModel
/*      */   {
/*      */     private FlatUIDefaultsInspector.Item[] allItems;
/*      */     
/*      */     private FlatUIDefaultsInspector.Item[] items;
/*      */     private Predicate<FlatUIDefaultsInspector.Item> filter;
/*      */     
/*      */     ItemsTableModel(FlatUIDefaultsInspector.Item[] items) {
/*  788 */       this.allItems = this.items = items;
/*      */     }
/*      */     
/*      */     void setItems(FlatUIDefaultsInspector.Item[] items) {
/*  792 */       this.allItems = this.items = items;
/*  793 */       setFilter(this.filter);
/*      */     }
/*      */     
/*      */     void setFilter(Predicate<FlatUIDefaultsInspector.Item> filter) {
/*  797 */       this.filter = filter;
/*      */       
/*  799 */       if (filter != null) {
/*  800 */         ArrayList<FlatUIDefaultsInspector.Item> list = new ArrayList<>(this.allItems.length);
/*  801 */         for (FlatUIDefaultsInspector.Item item : this.allItems) {
/*  802 */           if (filter.test(item))
/*  803 */             list.add(item); 
/*      */         } 
/*  805 */         this.items = list.<FlatUIDefaultsInspector.Item>toArray(new FlatUIDefaultsInspector.Item[list.size()]);
/*      */       } else {
/*  807 */         this.items = this.allItems;
/*      */       } 
/*  809 */       fireTableDataChanged();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getRowCount() {
/*  814 */       return this.items.length;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getColumnCount() {
/*  819 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getColumnName(int columnIndex) {
/*  824 */       switch (columnIndex) { case 0:
/*  825 */           return "Name";
/*  826 */         case 1: return "Value"; }
/*      */       
/*  828 */       return super.getColumnName(columnIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Class<?> getColumnClass(int columnIndex) {
/*  833 */       switch (columnIndex) { case 0:
/*  834 */           return String.class;
/*  835 */         case 1: return FlatUIDefaultsInspector.Item.class; }
/*      */       
/*  837 */       return super.getColumnClass(columnIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValueAt(int rowIndex, int columnIndex) {
/*  842 */       FlatUIDefaultsInspector.Item item = this.items[rowIndex];
/*  843 */       switch (columnIndex) { case 0:
/*  844 */           return item.key;
/*  845 */         case 1: return item; }
/*      */       
/*  847 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Renderer
/*      */     extends DefaultTableCellRenderer
/*      */   {
/*      */     protected boolean selected;
/*      */     protected boolean first;
/*      */     
/*      */     private Renderer() {}
/*      */     
/*      */     protected void init(JTable table, String key, boolean selected, int row) {
/*  860 */       this.selected = selected;
/*      */       
/*  862 */       this.first = false;
/*  863 */       if (row > 0) {
/*  864 */         String previousKey = (String)table.getValueAt(row - 1, 0);
/*  865 */         int dot = key.indexOf('.');
/*  866 */         if (dot > 0) {
/*  867 */           String prefix = key.substring(0, dot + 1);
/*  868 */           this.first = !previousKey.startsWith(prefix);
/*      */         } else {
/*  870 */           this.first = (previousKey.indexOf('.') > 0);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     protected void paintSeparator(Graphics g) {
/*  875 */       if (this.first && !this.selected) {
/*  876 */         g.setColor(FlatLaf.isLafDark() ? Color.gray : Color.lightGray);
/*  877 */         g.fillRect(0, 0, getWidth() - 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected String layoutLabel(FontMetrics fm, String text, Rectangle textR) {
/*  882 */       int width = getWidth();
/*  883 */       int height = getHeight();
/*  884 */       Insets insets = getInsets();
/*      */       
/*  886 */       Rectangle viewR = new Rectangle(insets.left, insets.top, width - insets.left + insets.right, height - insets.top + insets.bottom);
/*      */ 
/*      */       
/*  889 */       Rectangle iconR = new Rectangle();
/*      */       
/*  891 */       return SwingUtilities.layoutCompoundLabel(this, fm, text, null, 
/*  892 */           getVerticalAlignment(), getHorizontalAlignment(), 
/*  893 */           getVerticalTextPosition(), getHorizontalTextPosition(), viewR, iconR, textR, 
/*  894 */           getIconTextGap());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class KeyRenderer
/*      */     extends Renderer
/*      */   {
/*      */     private String key;
/*      */     
/*      */     private boolean isOverridden;
/*      */     
/*      */     private Icon overriddenIcon;
/*      */     
/*      */     private KeyRenderer() {}
/*      */     
/*      */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  911 */       this.key = (String)value;
/*  912 */       init(table, this.key, isSelected, row);
/*      */       
/*  914 */       FlatUIDefaultsInspector.Item item = (FlatUIDefaultsInspector.Item)table.getValueAt(row, 1);
/*  915 */       this.isOverridden = (item.lafValue != null);
/*      */ 
/*      */       
/*  918 */       String toolTipText = this.key;
/*  919 */       if (this.isOverridden)
/*  920 */         toolTipText = toolTipText + "    \n\nLaF UI default value was overridden with UIManager.put(key,value)."; 
/*  921 */       setToolTipText(toolTipText);
/*      */       
/*  923 */       return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void paintComponent(Graphics g) {
/*  928 */       g.setColor(getBackground());
/*  929 */       g.fillRect(0, 0, getWidth(), getHeight());
/*      */       
/*  931 */       FontMetrics fm = getFontMetrics(getFont());
/*  932 */       Rectangle textR = new Rectangle();
/*  933 */       String clippedText = layoutLabel(fm, this.key, textR);
/*  934 */       int x = textR.x;
/*  935 */       int y = textR.y + fm.getAscent();
/*      */       
/*  937 */       int dot = this.key.indexOf('.');
/*  938 */       if (dot > 0 && !this.selected) {
/*  939 */         g.setColor(FlatUIUtils.getUIColor("Label.disabledForeground", 
/*  940 */               FlatUIUtils.getUIColor("Label.disabledText", Color.gray)));
/*      */         
/*  942 */         if (dot >= clippedText.length()) {
/*  943 */           FlatUIUtils.drawString(this, g, clippedText, x, y);
/*      */         } else {
/*  945 */           String prefix = clippedText.substring(0, dot + 1);
/*  946 */           String subkey = clippedText.substring(dot + 1);
/*      */           
/*  948 */           FlatUIUtils.drawString(this, g, prefix, x, y);
/*      */           
/*  950 */           g.setColor(getForeground());
/*  951 */           FlatUIUtils.drawString(this, g, subkey, x + fm.stringWidth(prefix), y);
/*      */         } 
/*      */       } else {
/*  954 */         g.setColor(getForeground());
/*  955 */         FlatUIUtils.drawString(this, g, clippedText, x, y);
/*      */       } 
/*      */       
/*  958 */       if (this.isOverridden) {
/*  959 */         if (this.overriddenIcon == null) {
/*  960 */           this.overriddenIcon = (Icon)new FlatAbstractIcon(16, 16, null)
/*      */             {
/*      */               protected void paintIcon(Component c, Graphics2D g2) {
/*  963 */                 g2.setColor(FlatUIUtils.getUIColor("Actions.Red", Color.red));
/*  964 */                 g2.setStroke(new BasicStroke(2.0F));
/*  965 */                 g2.draw(FlatUIUtils.createPath(false, new double[] { 3.0D, 10.0D, 8.0D, 5.0D, 13.0D, 10.0D }));
/*      */               }
/*      */             };
/*      */         }
/*      */         
/*  970 */         this.overriddenIcon.paintIcon(this, g, 
/*  971 */             getWidth() - this.overriddenIcon.getIconWidth(), (
/*  972 */             getHeight() - this.overriddenIcon.getIconHeight()) / 2);
/*      */       } 
/*      */       
/*  975 */       paintSeparator(g);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ValueRenderer
/*      */     extends Renderer
/*      */   {
/*      */     private FlatUIDefaultsInspector.Item item;
/*      */ 
/*      */     
/*      */     private ValueRenderer() {}
/*      */ 
/*      */     
/*      */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  990 */       this.item = (FlatUIDefaultsInspector.Item)value;
/*  991 */       init(table, this.item.key, isSelected, row);
/*      */ 
/*      */       
/*  994 */       if (!(this.item.value instanceof Color) && !(this.item.value instanceof Color[])) {
/*  995 */         setBackground((Color)null);
/*  996 */         setForeground((Color)null);
/*      */       } 
/*  998 */       if (!(this.item.value instanceof Icon)) {
/*  999 */         setIcon((Icon)null);
/*      */       }
/*      */       
/* 1002 */       value = this.item.getValueAsString();
/*      */       
/* 1004 */       super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*      */       
/* 1006 */       if (this.item.value instanceof Color || this.item.value instanceof Color[]) {
/* 1007 */         Color color = (this.item.value instanceof Color[]) ? ((Color[])this.item.value)[0] : (Color)this.item.value;
/* 1008 */         boolean isDark = ((new HSLColor(color)).getLuminance() < 70.0F && color.getAlpha() >= 128);
/* 1009 */         setBackground(color);
/* 1010 */         setForeground(isDark ? Color.white : Color.black);
/* 1011 */       } else if (this.item.value instanceof Icon) {
/* 1012 */         Icon icon = (Icon)this.item.value;
/* 1013 */         setIcon(new FlatUIDefaultsInspector.SafeIcon(icon));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1019 */       String toolTipText = (this.item.value instanceof Object[]) ? Arrays.toString((Object[])this.item.value).replace(", ", ",\n") : String.valueOf(this.item.value);
/* 1020 */       if (this.item.lafValue != null)
/*      */       {
/* 1022 */         toolTipText = toolTipText + "    \n\nLaF UI default value was overridden with UIManager.put(key,value):\n    " + FlatUIDefaultsInspector.Item.valueAsString(this.item.lafValue) + "\n    " + String.valueOf(this.item.lafValue);
/*      */       }
/* 1024 */       setToolTipText(toolTipText);
/*      */       
/* 1026 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void paintComponent(Graphics g) {
/* 1031 */       if (this.item.value instanceof Color || this.item.value instanceof Color[])
/* 1032 */       { int width = getWidth();
/* 1033 */         int height = getHeight();
/* 1034 */         Color background = getBackground();
/*      */ 
/*      */         
/* 1037 */         fillRect(g, background, 0, 0, width, height);
/*      */         
/* 1039 */         if (this.item.value instanceof Color[]) {
/*      */           
/* 1041 */           int width2 = height * 2;
/* 1042 */           fillRect(g, ((Color[])this.item.value)[1], width - width2, 0, width2, height);
/*      */ 
/*      */           
/* 1045 */           Color defaultColor = ((Color[])this.item.value)[2];
/* 1046 */           if (defaultColor != null && !defaultColor.equals(background)) {
/* 1047 */             int width3 = height / 2;
/* 1048 */             fillRect(g, defaultColor, width - width3, 0, width3, height);
/*      */           } 
/*      */ 
/*      */           
/* 1052 */           int width4 = height / 4;
/* 1053 */           g.setColor(Color.magenta);
/* 1054 */           g.fillRect(width - width4, 0, width4, height);
/*      */         } 
/*      */ 
/*      */         
/* 1058 */         FontMetrics fm = getFontMetrics(getFont());
/* 1059 */         String text = getText();
/* 1060 */         Rectangle textR = new Rectangle();
/* 1061 */         layoutLabel(fm, text, textR);
/* 1062 */         int x = textR.x;
/* 1063 */         int y = textR.y + fm.getAscent();
/*      */         
/* 1065 */         g.setColor(getForeground());
/*      */ 
/*      */         
/* 1068 */         int hslIndex = text.indexOf("HSL");
/* 1069 */         if (hslIndex > 0) {
/* 1070 */           String hexText = text.substring(0, hslIndex);
/* 1071 */           String hslText = text.substring(hslIndex);
/* 1072 */           int hexWidth = Math.max(fm.stringWidth(hexText), fm.stringWidth("#12345678  "));
/* 1073 */           FlatUIUtils.drawString(this, g, hexText, x, y);
/* 1074 */           FlatUIUtils.drawString(this, g, hslText, x + hexWidth, y);
/*      */         } else {
/* 1076 */           FlatUIUtils.drawString(this, g, text, x, y);
/*      */         }  }
/* 1078 */       else { super.paintComponent(g); }
/*      */       
/* 1080 */       paintSeparator(g);
/*      */     }
/*      */ 
/*      */     
/*      */     private void fillRect(Graphics g, Color color, int x, int y, int width, int height) {
/* 1085 */       if (color.getAlpha() != 255) {
/* 1086 */         g.setColor(Color.white);
/* 1087 */         g.fillRect(x, y, width, height);
/*      */       } 
/*      */       
/* 1090 */       g.setColor(color);
/* 1091 */       g.fillRect(x, y, width, height);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class SafeIcon
/*      */     implements Icon
/*      */   {
/*      */     private final Icon icon;
/*      */ 
/*      */     
/*      */     SafeIcon(Icon icon) {
/* 1103 */       this.icon = icon;
/*      */     }
/*      */ 
/*      */     
/*      */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 1108 */       int width = getIconWidth();
/* 1109 */       int height = getIconHeight();
/*      */       
/*      */       try {
/* 1112 */         g.setColor(UIManager.getColor("Panel.background"));
/* 1113 */         g.fillRect(x, y, width, height);
/*      */         
/* 1115 */         this.icon.paintIcon(c, g, x, y);
/* 1116 */       } catch (Exception ex) {
/* 1117 */         g.setColor(Color.red);
/* 1118 */         g.drawRect(x, y, width - 1, height - 1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int getIconWidth() {
/* 1124 */       return this.icon.getIconWidth();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getIconHeight() {
/* 1129 */       return this.icon.getIconHeight();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\FlatUIDefaultsInspector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */