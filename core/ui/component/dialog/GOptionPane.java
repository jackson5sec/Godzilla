/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.HeadlessException;
/*     */ import java.util.Locale;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class GOptionPane
/*     */ {
/*  15 */   public static final Object UNINITIALIZED_VALUE = JOptionPane.UNINITIALIZED_VALUE;
/*     */   
/*     */   public static final int DEFAULT_OPTION = -1;
/*     */   
/*     */   public static final int YES_NO_OPTION = 0;
/*     */   
/*     */   public static final int YES_NO_CANCEL_OPTION = 1;
/*     */   
/*     */   public static final int OK_CANCEL_OPTION = 2;
/*     */   
/*     */   public static final int YES_OPTION = 0;
/*     */   
/*     */   public static final int NO_OPTION = 1;
/*     */   
/*     */   public static final int CANCEL_OPTION = 2;
/*     */   public static final int OK_OPTION = 0;
/*     */   public static final int CLOSED_OPTION = -1;
/*     */   public static final int ERROR_MESSAGE = 0;
/*     */   public static final int INFORMATION_MESSAGE = 1;
/*     */   public static final int WARNING_MESSAGE = 2;
/*     */   public static final int QUESTION_MESSAGE = 3;
/*     */   public static final int PLAIN_MESSAGE = -1;
/*     */   public static final String ICON_PROPERTY = "icon";
/*     */   public static final String MESSAGE_PROPERTY = "message";
/*     */   public static final String VALUE_PROPERTY = "value";
/*     */   public static final String OPTIONS_PROPERTY = "options";
/*     */   public static final String INITIAL_VALUE_PROPERTY = "initialValue";
/*     */   public static final String MESSAGE_TYPE_PROPERTY = "messageType";
/*     */   public static final String OPTION_TYPE_PROPERTY = "optionType";
/*     */   public static final String SELECTION_VALUES_PROPERTY = "selectionValues";
/*     */   public static final String INITIAL_SELECTION_VALUE_PROPERTY = "initialSelectionValue";
/*     */   public static final String INPUT_VALUE_PROPERTY = "inputValue";
/*     */   public static final String WANTS_INPUT_PROPERTY = "wantsInput";
/*     */   
/*     */   public static String showInputDialog(Object message) throws HeadlessException {
/*  50 */     return showInputDialog((Component)null, message);
/*     */   }
/*     */   
/*     */   public static String showInputDialog(Object message, Object initialSelectionValue) {
/*  54 */     return showInputDialog(null, message, initialSelectionValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String showInputDialog(Component parentComponent, Object message) throws HeadlessException {
/*  59 */     return showInputDialog(parentComponent, message, getString("OptionPane.inputDialogTitle", parentComponent), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String showInputDialog(Component parentComponent, Object message, Object initialSelectionValue) {
/*  65 */     return (String)showInputDialog(parentComponent, message, 
/*  66 */         getString("OptionPane.inputDialogTitle", parentComponent), 3, null, null, initialSelectionValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String showInputDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
/*  74 */     return (String)showInputDialog(parentComponent, message, title, messageType, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) throws HeadlessException {
/*  82 */     title = EasyI18N.getI18nString(title);
/*  83 */     if (message instanceof String) {
/*  84 */       message = EasyI18N.getI18nString(message.toString());
/*     */     }
/*  86 */     return JOptionPane.showInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showMessageDialog(Component parentComponent, Object message) throws HeadlessException {
/*  92 */     showMessageDialog(parentComponent, message, getString("OptionPane.messageDialogTitle", parentComponent), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
/* 100 */     showMessageDialog(parentComponent, message, title, messageType, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType, Icon icon) throws HeadlessException {
/* 106 */     showOptionDialog(parentComponent, message, title, -1, messageType, icon, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(Component parentComponent, Object message) throws HeadlessException {
/* 112 */     return showConfirmDialog(parentComponent, message, 
/* 113 */         UIManager.getString("OptionPane.titleText"), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) throws HeadlessException {
/* 120 */     return showConfirmDialog(parentComponent, message, title, optionType, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) throws HeadlessException {
/* 128 */     return showConfirmDialog(parentComponent, message, title, optionType, messageType, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon) throws HeadlessException {
/* 135 */     return showOptionDialog(parentComponent, message, title, optionType, messageType, icon, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) throws HeadlessException {
/* 143 */     title = EasyI18N.getI18nString(title);
/* 144 */     if (message instanceof String) {
/* 145 */       message = EasyI18N.getI18nString(message.toString());
/*     */     }
/* 147 */     return JOptionPane.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showInternalMessageDialog(Component parentComponent, Object message) {
/* 157 */     showInternalMessageDialog(parentComponent, message, 
/* 158 */         getString("OptionPane.messageDialogTitle", parentComponent), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showInternalMessageDialog(Component parentComponent, Object message, String title, int messageType) {
/* 165 */     showInternalMessageDialog(parentComponent, message, title, messageType, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showInternalMessageDialog(Component parentComponent, Object message, String title, int messageType, Icon icon) {
/* 173 */     showInternalOptionDialog(parentComponent, message, title, -1, messageType, icon, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showInternalConfirmDialog(Component parentComponent, Object message) {
/* 180 */     return showInternalConfirmDialog(parentComponent, message, 
/* 181 */         UIManager.getString("OptionPane.titleText"), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showInternalConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
/* 189 */     return showInternalConfirmDialog(parentComponent, message, title, optionType, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showInternalConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
/* 197 */     return showInternalConfirmDialog(parentComponent, message, title, optionType, messageType, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showInternalConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon) {
/* 205 */     return showInternalOptionDialog(parentComponent, message, title, optionType, messageType, icon, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int showInternalOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
/* 214 */     title = EasyI18N.getI18nString(title);
/* 215 */     if (message instanceof String) {
/* 216 */       message = EasyI18N.getI18nString(message.toString());
/*     */     }
/* 218 */     return JOptionPane.showInternalOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String showInternalInputDialog(Component parentComponent, Object message) {
/* 223 */     return showInternalInputDialog(parentComponent, message, getString("OptionPane.inputDialogTitle", parentComponent), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String showInternalInputDialog(Component parentComponent, Object message, String title, int messageType) {
/* 229 */     title = EasyI18N.getI18nString(title);
/* 230 */     if (message instanceof String) {
/* 231 */       message = EasyI18N.getI18nString(message.toString());
/*     */     }
/* 233 */     return (String)showInternalInputDialog(parentComponent, message, title, messageType, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object showInternalInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue) {
/* 240 */     return JOptionPane.showInternalInputDialog(parentComponent, message, title, messageType, icon, selectionValues, initialSelectionValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Frame getFrameForComponent(Component parentComponent) throws HeadlessException {
/* 245 */     return JOptionPane.getFrameForComponent(parentComponent);
/*     */   }
/*     */   
/*     */   public static String getString(Object key, Component c) {
/* 249 */     Locale l = (c == null) ? Locale.getDefault() : c.getLocale();
/* 250 */     return UIManager.getString(key, l);
/*     */   }
/*     */ 
/*     */   
/*     */   public static JDesktopPane getDesktopPaneForComponent(Component parentComponent) {
/* 255 */     return JOptionPane.getDesktopPaneForComponent(parentComponent);
/*     */   }
/*     */   
/*     */   public static void setRootFrame(Frame newRootFrame) {
/* 259 */     JOptionPane.setRootFrame(newRootFrame);
/*     */   }
/*     */   
/*     */   public static Frame getRootFrame() throws HeadlessException {
/* 263 */     return JOptionPane.getRootFrame();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\GOptionPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */