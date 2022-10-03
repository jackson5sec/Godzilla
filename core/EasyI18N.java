/*     */ package core;
/*     */ import core.annotation.I18NAction;
/*     */ import java.awt.Window;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.MenuElement;
/*     */ import util.functions;
/*     */ 
/*     */ public class EasyI18N {
/*  19 */   private static final HashMap<Class<?>, Method> actionMap = new HashMap<>(); public static final String SETING_KETY = "language";
/*  20 */   private static final Class[] parameterTypes = new Class[] { Object.class, Field.class };
/*  21 */   private static final Locale language = new Locale(Db.getSetingValue("language", "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? "zh" : "en"));
/*  22 */   private static final ResourceBundle bundle = ResourceBundle.getBundle("godzilla", language);
/*     */   
/*     */   static {
/*     */     try {
/*  26 */       Method[] methods = EasyI18N.class.getDeclaredMethods();
/*  27 */       for (Method method : methods) {
/*  28 */         if (Modifier.isStatic(method.getModifiers()) && Arrays.equals((Object[])parameterTypes, (Object[])method.getParameterTypes())) {
/*  29 */           I18NAction action = method.<I18NAction>getDeclaredAnnotation(I18NAction.class);
/*  30 */           if (action != null) {
/*  31 */             actionMap.put(action.targetClass(), method);
/*     */           }
/*     */         } 
/*     */       } 
/*  35 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static void installObject(Object obj) {
/*     */     try {
/*  41 */       Class<?> objClass = obj.getClass();
/*  42 */       while (objClass != null && (!objClass.getName().startsWith("java") || !objClass.getName().startsWith("sun"))) {
/*     */         try {
/*  44 */           Field[] fields = objClass.getDeclaredFields();
/*  45 */           Method actionMethod = null;
/*  46 */           for (Field field : fields) {
/*  47 */             if (field.getAnnotation(NoI18N.class) == null) {
/*  48 */               field.setAccessible(true);
/*  49 */               actionMethod = findAction(field.getType());
/*  50 */               if (actionMethod != null) {
/*  51 */                 actionMethod.setAccessible(true);
/*  52 */                 actionMethod.invoke((Object)null, new Object[] { obj, field });
/*     */               } 
/*     */             } 
/*     */           } 
/*  56 */           if (objClass.getAnnotation(NoI18N.class) == null) {
/*  57 */             actionMethod = findAction(objClass);
/*  58 */             if (actionMethod != null) {
/*  59 */               actionMethod.setAccessible(true);
/*  60 */               actionMethod.invoke((Object)null, new Object[] { obj, null });
/*     */             } 
/*     */           } 
/*  63 */         } catch (Exception exception) {}
/*     */ 
/*     */         
/*  66 */         objClass = objClass.getSuperclass();
/*     */       } 
/*  68 */     } catch (Exception e) {
/*  69 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getI18nString(String format, Object... args) {
/*  74 */     return String.format(getI18nString(format), args);
/*     */   }
/*     */   public static String getI18nString(String key) {
/*  77 */     if ("zh".equals(language.getLanguage())) {
/*  78 */       return key;
/*     */     }
/*  80 */     if (key != null) {
/*  81 */       String value = null;
/*     */       try {
/*  83 */         value = bundle.getString(key.trim().replace("\r\n", "\\r\\n").replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
/*  84 */         if (value != null) {
/*  85 */           value = value.replace("\\r\\n", "\r\n").replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
/*     */         }
/*  87 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/*  90 */       return (value == null) ? key : value;
/*     */     } 
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method findAction(Class fieldType) {
/*  97 */     Method action = findAction(fieldType, true);
/*  98 */     if (action == null) {
/*  99 */       action = findAction(fieldType, false);
/*     */     }
/* 101 */     return action;
/*     */   }
/*     */   private static Method findAction(Class<?> fieldType, boolean comparisonThis) {
/* 104 */     if (comparisonThis) {
/* 105 */       Iterator<Class<?>> keys = actionMap.keySet().iterator();
/* 106 */       while (keys.hasNext()) {
/* 107 */         Class clazz = keys.next();
/* 108 */         if (fieldType.equals(clazz)) {
/* 109 */           return actionMap.get(clazz);
/*     */         }
/*     */       } 
/*     */     } else {
/* 113 */       Iterator<Class<?>> keys = actionMap.keySet().iterator();
/* 114 */       while (keys.hasNext()) {
/* 115 */         Class clazz = keys.next();
/* 116 */         if (clazz.isAssignableFrom(fieldType)) {
/* 117 */           return actionMap.get(clazz);
/*     */         }
/*     */       } 
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = JLabel.class)
/*     */   public static void installJLabel(Object obj, Field targetField) throws Throwable {
/* 126 */     JLabel label = (JLabel)targetField.get(obj);
/* 127 */     if (label != null) {
/* 128 */       label.setText(getI18nString(label.getText()));
/*     */     } else {
/* 130 */       targetField.set(obj, new JLabel(targetField.getName()));
/*     */     } 
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = JMenu.class)
/*     */   public static void installJMenu(Object obj, Field targetField) throws Throwable {
/* 136 */     JMenu menu = (JMenu)targetField.get(obj);
/* 137 */     menu.setText(getI18nString(menu.getText()));
/* 138 */     int itemCount = menu.getItemCount();
/* 139 */     for (int i = 0; i < itemCount; i++) {
/* 140 */       JMenuItem menuItem = menu.getItem(i);
/* 141 */       menuItem.setText(getI18nString(menuItem.getText()));
/*     */     } 
/*     */   }
/*     */   @I18NAction(targetClass = JTabbedPane.class)
/*     */   public static void installJTabbedPane(Object obj, Field targetField) throws Throwable {
/* 146 */     JTabbedPane tabbedPane = (JTabbedPane)targetField.get(obj);
/* 147 */     int itemCount = tabbedPane.getTabCount();
/* 148 */     for (int i = 0; i < itemCount; i++) {
/* 149 */       String title = tabbedPane.getTitleAt(i);
/* 150 */       if (title != null)
/* 151 */         tabbedPane.setTitleAt(i, getI18nString(title)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = JPopupMenu.class)
/*     */   public static void installJPopupMenu(Object obj, Field targetField) throws Throwable {
/* 157 */     JPopupMenu popupMenu = (JPopupMenu)targetField.get(obj);
/* 158 */     MenuElement[] menuElements = popupMenu.getSubElements();
/* 159 */     for (MenuElement menuElement : menuElements) {
/* 160 */       if (menuElement instanceof JMenuItem) {
/* 161 */         JMenuItem menuItem = (JMenuItem)menuElement;
/* 162 */         menuItem.setText(getI18nString(menuItem.getText()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   @I18NAction(targetClass = JButton.class)
/*     */   public static void installJButton(Object obj, Field targetField) throws Throwable {
/* 168 */     JButton button = (JButton)targetField.get(obj);
/* 169 */     if (button != null)
/* 170 */       button.setText(getI18nString(button.getText())); 
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = JCheckBox.class)
/*     */   public static void installJCheckBox(Object obj, Field targetField) throws Throwable {
/* 175 */     JCheckBox checkBox = (JCheckBox)targetField.get(obj);
/* 176 */     if (checkBox != null)
/* 177 */       checkBox.setText(getI18nString(checkBox.getText())); 
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = JComponent.class)
/*     */   public static void installJComponent(Object obj, Field targetField) throws Throwable {
/* 182 */     JComponent component = null;
/* 183 */     if (targetField == null) {
/* 184 */       component = (JComponent)obj;
/*     */     } else {
/* 186 */       component = (JComponent)targetField.get(obj);
/*     */     } 
/* 188 */     if (component == null) {
/*     */       return;
/*     */     }
/* 191 */     Border border = component.getBorder();
/* 192 */     if (border instanceof TitledBorder) {
/* 193 */       TitledBorder titledBorder = (TitledBorder)border;
/* 194 */       titledBorder.setTitle(getI18nString(titledBorder.getTitle()));
/*     */     } 
/* 196 */     Method getTitleMethod = functions.getMethodByClass(component.getClass(), "getTitle", null);
/* 197 */     Method setTitleMethod = functions.getMethodByClass(component.getClass(), "setTitle", new Class[] { String.class });
/* 198 */     if (getTitleMethod != null && setTitleMethod != null) {
/* 199 */       getTitleMethod.setAccessible(true);
/* 200 */       setTitleMethod.setAccessible(true);
/* 201 */       String oldTitle = (String)getTitleMethod.invoke(obj, (Object[])null);
/* 202 */       if (oldTitle != null)
/* 203 */         setTitleMethod.invoke(obj, new Object[] { getI18nString(oldTitle) }); 
/*     */     } 
/*     */   }
/*     */   
/*     */   @I18NAction(targetClass = Window.class)
/*     */   public static void installWindow(Object obj, Field targetField) {
/*     */     try {
/* 210 */       Window component = null;
/* 211 */       if (targetField == null) {
/* 212 */         component = (Window)obj;
/*     */       } else {
/* 214 */         component = (Window)targetField.get(obj);
/*     */       } 
/* 216 */       Method getTitleMethod = functions.getMethodByClass(component.getClass(), "getTitle", null);
/* 217 */       Method setTitleMethod = functions.getMethodByClass(component.getClass(), "setTitle", new Class[] { String.class });
/* 218 */       if (getTitleMethod != null && setTitleMethod != null) {
/* 219 */         getTitleMethod.setAccessible(true);
/* 220 */         setTitleMethod.setAccessible(true);
/* 221 */         String oldTitle = (String)getTitleMethod.invoke(obj, (Object[])null);
/* 222 */         if (oldTitle != null) {
/* 223 */           setTitleMethod.invoke(obj, new Object[] { getI18nString(oldTitle) });
/*     */         }
/*     */       } 
/* 226 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\EasyI18N.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */