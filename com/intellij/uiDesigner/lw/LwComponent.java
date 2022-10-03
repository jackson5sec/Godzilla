/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.jdom.Element;
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
/*     */ public abstract class LwComponent
/*     */   implements IComponent
/*     */ {
/*     */   private String myId;
/*     */   private String myBinding;
/*     */   private final String myClassName;
/*     */   private LwContainer myParent;
/*     */   private final GridConstraints myConstraints;
/*     */   private Object myCustomLayoutConstraints;
/*     */   private final Rectangle myBounds;
/*     */   private final HashMap myIntrospectedProperty2Value;
/*     */   private Element myErrorComponentProperties;
/*     */   protected final HashMap myClientProperties;
/*     */   protected final HashMap myDelegeeClientProperties;
/*     */   private boolean myCustomCreate = false;
/*     */   private boolean myDefaultBinding = false;
/*     */   
/*     */   public LwComponent(String className) {
/*  70 */     if (className == null) {
/*  71 */       throw new IllegalArgumentException("className cannot be null");
/*     */     }
/*  73 */     this.myBounds = new Rectangle();
/*  74 */     this.myConstraints = new GridConstraints();
/*  75 */     this.myIntrospectedProperty2Value = new HashMap();
/*  76 */     this.myClassName = className;
/*  77 */     this.myClientProperties = new HashMap();
/*  78 */     this.myDelegeeClientProperties = new HashMap();
/*     */   }
/*     */   
/*     */   public final String getId() {
/*  82 */     return this.myId;
/*     */   }
/*     */   
/*     */   public final void setId(String id) {
/*  86 */     if (id == null) {
/*  87 */       throw new IllegalArgumentException("id cannot be null");
/*     */     }
/*  89 */     this.myId = id;
/*     */   }
/*     */   
/*     */   public final String getBinding() {
/*  93 */     return this.myBinding;
/*     */   }
/*     */   
/*     */   public final void setBinding(String binding) {
/*  97 */     this.myBinding = binding;
/*     */   }
/*     */   
/*     */   public final Object getCustomLayoutConstraints() {
/* 101 */     return this.myCustomLayoutConstraints;
/*     */   }
/*     */   
/*     */   public final void setCustomLayoutConstraints(Object customLayoutConstraints) {
/* 105 */     this.myCustomLayoutConstraints = customLayoutConstraints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getComponentClassName() {
/* 112 */     return this.myClassName;
/*     */   }
/*     */   
/*     */   public IProperty[] getModifiedProperties() {
/* 116 */     return (IProperty[])getAssignedIntrospectedProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Rectangle getBounds() {
/* 124 */     return (Rectangle)this.myBounds.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final GridConstraints getConstraints() {
/* 132 */     return this.myConstraints;
/*     */   }
/*     */   
/*     */   public boolean isCustomCreate() {
/* 136 */     return this.myCustomCreate;
/*     */   }
/*     */   
/*     */   public boolean isDefaultBinding() {
/* 140 */     return this.myDefaultBinding;
/*     */   }
/*     */   
/*     */   public boolean accept(ComponentVisitor visitor) {
/* 144 */     return visitor.visit(this);
/*     */   }
/*     */   
/*     */   public boolean areChildrenExclusive() {
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   public final LwContainer getParent() {
/* 152 */     return this.myParent;
/*     */   }
/*     */   
/*     */   public IContainer getParentContainer() {
/* 156 */     return this.myParent;
/*     */   }
/*     */   
/*     */   protected final void setParent(LwContainer parent) {
/* 160 */     this.myParent = parent;
/*     */   }
/*     */   
/*     */   public final void setBounds(Rectangle bounds) {
/* 164 */     this.myBounds.setBounds(bounds);
/*     */   }
/*     */   
/*     */   public final Object getPropertyValue(LwIntrospectedProperty property) {
/* 168 */     return this.myIntrospectedProperty2Value.get(property);
/*     */   }
/*     */   
/*     */   public final void setPropertyValue(LwIntrospectedProperty property, Object value) {
/* 172 */     this.myIntrospectedProperty2Value.put(property, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Element getErrorComponentProperties() {
/* 180 */     return this.myErrorComponentProperties;
/*     */   }
/*     */   
/*     */   public final LwIntrospectedProperty[] getAssignedIntrospectedProperties() {
/* 184 */     LwIntrospectedProperty[] properties = new LwIntrospectedProperty[this.myIntrospectedProperty2Value.size()];
/* 185 */     Iterator iterator = this.myIntrospectedProperty2Value.keySet().iterator();
/*     */     
/* 187 */     for (int i = 0; iterator.hasNext(); i++) {
/* 188 */       properties[i] = iterator.next();
/*     */     }
/* 190 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readBase(Element element) {
/* 198 */     setId(LwXmlReader.getRequiredString(element, "id"));
/* 199 */     setBinding(element.getAttributeValue("binding"));
/* 200 */     this.myCustomCreate = LwXmlReader.getOptionalBoolean(element, "custom-create", false);
/* 201 */     this.myDefaultBinding = LwXmlReader.getOptionalBoolean(element, "default-binding", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readProperties(Element element, PropertiesProvider provider) {
/* 209 */     if (provider == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 214 */     Element propertiesElement = LwXmlReader.getChild(element, "properties");
/* 215 */     if (propertiesElement == null) {
/* 216 */       propertiesElement = new Element("properties", element.getNamespace());
/*     */     }
/*     */     
/* 219 */     HashMap name2property = provider.getLwProperties(getComponentClassName());
/* 220 */     if (name2property == null) {
/* 221 */       this.myErrorComponentProperties = (Element)propertiesElement.clone();
/*     */       
/*     */       return;
/*     */     } 
/* 225 */     List propertyElements = propertiesElement.getChildren();
/* 226 */     for (int i = 0; i < propertyElements.size(); i++) {
/* 227 */       Element t = propertyElements.get(i);
/* 228 */       String name = t.getName();
/* 229 */       LwIntrospectedProperty property = (LwIntrospectedProperty)name2property.get(name);
/* 230 */       if (property != null) {
/*     */         
/*     */         try {
/*     */           
/* 234 */           Object value = property.read(t);
/* 235 */           setPropertyValue(property, value);
/*     */         }
/* 237 */         catch (Exception exc) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 242 */     readClientProperties(element);
/*     */   }
/*     */   
/*     */   private void readClientProperties(Element element) {
/* 246 */     Element propertiesElement = LwXmlReader.getChild(element, "clientProperties");
/* 247 */     if (propertiesElement == null)
/* 248 */       return;  List clientPropertyList = propertiesElement.getChildren();
/* 249 */     for (int i = 0; i < clientPropertyList.size(); i++) {
/* 250 */       LwIntrospectedProperty lwProp; Element prop = clientPropertyList.get(i);
/* 251 */       String propName = prop.getName();
/* 252 */       String className = LwXmlReader.getRequiredString(prop, "class");
/*     */ 
/*     */       
/* 255 */       if (className.equals(Integer.class.getName())) {
/* 256 */         lwProp = new LwIntroIntProperty(propName);
/*     */       }
/* 258 */       else if (className.equals(Boolean.class.getName())) {
/* 259 */         lwProp = new LwIntroBooleanProperty(propName);
/*     */       }
/* 261 */       else if (className.equals(Double.class.getName())) {
/* 262 */         lwProp = new LwIntroPrimitiveTypeProperty(propName, Double.class);
/*     */       } else {
/*     */         Class propClass;
/*     */         
/*     */         try {
/* 267 */           propClass = Class.forName(className);
/*     */         }
/* 269 */         catch (ClassNotFoundException e) {}
/*     */ 
/*     */         
/* 272 */         lwProp = CompiledClassPropertiesProvider.propertyFromClass(propClass, propName);
/*     */       } 
/*     */       
/* 275 */       if (lwProp != null) {
/*     */         Object value;
/*     */         try {
/* 278 */           value = lwProp.read(prop);
/*     */         }
/* 280 */         catch (Exception e) {}
/*     */ 
/*     */         
/* 283 */         this.myDelegeeClientProperties.put(propName, value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readConstraints(Element element) {
/* 292 */     LwContainer parent = getParent();
/* 293 */     if (parent == null) {
/* 294 */       throw new IllegalStateException("component must be in LW tree: " + this);
/*     */     }
/* 296 */     parent.readConstraintsForChild(element, this);
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
/*     */   public final Object getClientProperty(Object key) {
/* 308 */     if (key == null) {
/* 309 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/* 311 */     return this.myClientProperties.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void putClientProperty(Object key, Object value) {
/* 318 */     if (key == null) {
/* 319 */       throw new IllegalArgumentException("key cannot be null");
/*     */     }
/* 321 */     this.myClientProperties.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap getDelegeeClientProperties() {
/* 325 */     return this.myDelegeeClientProperties;
/*     */   }
/*     */   
/*     */   public abstract void read(Element paramElement, PropertiesProvider paramPropertiesProvider) throws Exception;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwComponent.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */