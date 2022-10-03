/*     */ package util;
/*     */ 
/*     */ import core.ui.component.annotation.ButtonToMenuItem;
/*     */ import java.awt.Button;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.MenuElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class automaticBindClick
/*     */ {
/*     */   public static void bindButtonClick(final Object fieldClass, Object eventClass) {
/*     */     try {
/*  23 */       Field[] fields = fieldClass.getClass().getDeclaredFields();
/*  24 */       for (Field field : fields) {
/*  25 */         if (field.getType().isAssignableFrom(Button.class)) {
/*  26 */           field.setAccessible(true);
/*  27 */           Button fieldValue = (Button)field.get(fieldClass);
/*  28 */           String fieldName = field.getName();
/*  29 */           if (fieldValue != null) {
/*     */             try {
/*  31 */               final Method method = eventClass.getClass().getDeclaredMethod(fieldName + "Click", new Class[] { ActionEvent.class });
/*  32 */               method.setAccessible(true);
/*  33 */               if (method != null) {
/*  34 */                 fieldValue.addActionListener(new ActionListener()
/*     */                     {
/*     */                       
/*     */                       public void actionPerformed(ActionEvent e)
/*     */                       {
/*     */                         try {
/*  40 */                           method.invoke(fieldClass, new Object[] { e });
/*  41 */                         } catch (Exception e1) {
/*  42 */                           Log.error(e1);
/*     */                         } 
/*     */                       }
/*     */                     });
/*     */               }
/*  47 */             } catch (NoSuchMethodException e) {
/*     */               
/*  49 */               Log.error(fieldName + "Click  未实现");
/*     */             }
/*     */           
/*     */           }
/*     */         } 
/*     */       } 
/*  55 */     } catch (Exception e) {
/*     */       
/*  57 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void bindJButtonClick(Class fieldClass, Object fieldObject, Class eventClass, final Object eventObject) {
/*     */     try {
/*  65 */       Field[] fields = fieldClass.getDeclaredFields();
/*  66 */       for (Field field : fields) {
/*  67 */         if (field.getType().isAssignableFrom(JButton.class)) {
/*  68 */           field.setAccessible(true);
/*  69 */           JButton fieldValue = (JButton)field.get(fieldObject);
/*  70 */           String fieldName = field.getName();
/*  71 */           if (fieldValue != null) {
/*     */             try {
/*  73 */               final Method method = eventClass.getDeclaredMethod(fieldName + "Click", new Class[] { ActionEvent.class });
/*  74 */               method.setAccessible(true);
/*  75 */               if (method != null) {
/*  76 */                 fieldValue.addActionListener(new ActionListener()
/*     */                     {
/*     */                       
/*     */                       public void actionPerformed(ActionEvent e)
/*     */                       {
/*     */                         try {
/*  82 */                           method.invoke(eventObject, new Object[] { e });
/*  83 */                         } catch (Exception e1) {
/*  84 */                           Log.error(e1);
/*     */                         } 
/*     */                       }
/*     */                     });
/*     */               }
/*  89 */             } catch (NoSuchMethodException e) {
/*  90 */               Log.error(fieldName + "Click  未实现");
/*     */             }
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/*  97 */     } catch (Exception e) {
/*     */       
/*  99 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   public static void bindJButtonClick(Object fieldClass, Object eventClass) {
/* 103 */     bindJButtonClick(fieldClass.getClass(), fieldClass, eventClass.getClass(), eventClass);
/*     */   }
/*     */   public static void bindMenuItemClick(Object item, Map<String, Method> methodMap, Object eventClass) {
/* 106 */     MenuElement[] menuElements = ((MenuElement)item).getSubElements();
/* 107 */     if (methodMap == null) {
/* 108 */       methodMap = getMenuItemMethod(eventClass);
/*     */     }
/* 110 */     if (menuElements.length == 0) {
/* 111 */       if (item.getClass().isAssignableFrom(JMenuItem.class)) {
/* 112 */         Method method = methodMap.get(((JMenuItem)item).getActionCommand() + "MenuItemClick");
/* 113 */         addMenuItemClickEvent(item, method, eventClass);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 119 */       for (int i = 0; i < menuElements.length; i++) {
/* 120 */         MenuElement menuElement = menuElements[i];
/* 121 */         Class<?> itemClass = menuElement.getClass();
/* 122 */         if (itemClass.isAssignableFrom(JPopupMenu.class) || itemClass.isAssignableFrom(JMenu.class)) {
/* 123 */           bindMenuItemClick(menuElement, methodMap, eventClass);
/* 124 */         } else if (item.getClass().isAssignableFrom(JMenuItem.class)) {
/* 125 */           Method method = methodMap.get(((JMenuItem)menuElement).getActionCommand() + "MenuItemClick");
/* 126 */           addMenuItemClickEvent(menuElement, method, eventClass);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void bindButtonToMenuItem(final Object fieldClass, Object eventClass, Object menu) {
/*     */     try {
/* 134 */       if (JMenu.class.isAssignableFrom(menu.getClass()) || JPopupMenu.class.isAssignableFrom(menu.getClass())) {
/*     */         try {
/* 136 */           Field[] fields = fieldClass.getClass().getDeclaredFields();
/* 137 */           for (Field field : fields) {
/* 138 */             if (field.getType().isAssignableFrom(JButton.class)) {
/* 139 */               field.setAccessible(true);
/* 140 */               JButton fieldValue = (JButton)field.get(fieldClass);
/* 141 */               String fieldName = field.getName();
/* 142 */               if (fieldValue != null && field.isAnnotationPresent((Class)ButtonToMenuItem.class)) {
/* 143 */                 ButtonToMenuItem buttonToMenuItem = field.<ButtonToMenuItem>getAnnotation(ButtonToMenuItem.class);
/*     */                 try {
/* 145 */                   final Method method = eventClass.getClass().getDeclaredMethod(fieldName + "Click", new Class[] { ActionEvent.class });
/* 146 */                   method.setAccessible(true);
/* 147 */                   if (method != null) {
/* 148 */                     Method addMethod = menu.getClass().getMethod("add", new Class[] { JMenuItem.class });
/*     */                     
/* 150 */                     String menuItemName = fieldValue.getText();
/*     */                     
/* 152 */                     JMenuItem menuItem = new JMenuItem((buttonToMenuItem.name().length() > 0) ? buttonToMenuItem.name() : menuItemName);
/* 153 */                     menuItem.addActionListener(new ActionListener()
/*     */                         {
/*     */                           
/*     */                           public void actionPerformed(ActionEvent e)
/*     */                           {
/*     */                             try {
/* 159 */                               method.invoke(fieldClass, new Object[] { e });
/* 160 */                             } catch (Exception e1) {
/* 161 */                               Log.error(e1);
/*     */                             } 
/*     */                           }
/*     */                         });
/* 165 */                     addMethod.invoke(menu, new Object[] { menuItem });
/*     */                   } 
/* 167 */                 } catch (NoSuchMethodException e) {
/* 168 */                   Log.error(fieldName + "Click  未实现");
/*     */                 }
/*     */               
/*     */               }
/*     */             
/*     */             } 
/*     */           } 
/* 175 */         } catch (Exception e) {
/*     */           
/* 177 */           e.printStackTrace();
/*     */         }
/*     */       
/*     */       }
/* 181 */     } catch (Exception e) {
/* 182 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, Method> getMenuItemMethod(Object eventClass) {
/* 188 */     Method[] methods = eventClass.getClass().getDeclaredMethods();
/*     */ 
/*     */     
/* 191 */     Map<String, Method> methodMap = new HashMap<>();
/* 192 */     for (int i = 0; i < methods.length; i++) {
/* 193 */       Method method = methods[i];
/* 194 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 195 */       if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(ActionEvent.class) && method.getReturnType().isAssignableFrom(void.class) && method.getName().endsWith("MenuItemClick")) {
/* 196 */         methodMap.put(method.getName(), method);
/*     */       }
/*     */     } 
/* 199 */     return methodMap;
/*     */   }
/*     */   private static void addMenuItemClickEvent(Object item, final Method method, final Object eventClass) {
/* 202 */     if (method != null && eventClass != null && item.getClass().isAssignableFrom(JMenuItem.class))
/* 203 */       ((JMenuItem)item).addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent paramActionEvent)
/*     */             {
/*     */               try {
/* 208 */                 method.setAccessible(true);
/* 209 */                 method.invoke(eventClass, new Object[] { paramActionEvent });
/* 210 */               } catch (Exception e) {
/* 211 */                 e.printStackTrace();
/*     */               } 
/*     */             }
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\automaticBindClick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */