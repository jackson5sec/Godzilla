/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AccessibleObject;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import org.mozilla.javascript.annotations.JSConstructor;
/*      */ import org.mozilla.javascript.annotations.JSFunction;
/*      */ import org.mozilla.javascript.annotations.JSGetter;
/*      */ import org.mozilla.javascript.annotations.JSSetter;
/*      */ import org.mozilla.javascript.annotations.JSStaticFunction;
/*      */ import org.mozilla.javascript.debug.DebuggableObject;
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
/*      */ public abstract class ScriptableObject
/*      */   implements Scriptable, Serializable, DebuggableObject, ConstProperties
/*      */ {
/*      */   static final long serialVersionUID = 2829861078851942586L;
/*      */   public static final int EMPTY = 0;
/*      */   public static final int READONLY = 1;
/*      */   public static final int DONTENUM = 2;
/*      */   public static final int PERMANENT = 4;
/*      */   public static final int UNINITIALIZED_CONST = 8;
/*      */   public static final int CONST = 13;
/*      */   private Scriptable prototypeObject;
/*      */   private Scriptable parentScopeObject;
/*      */   private transient Slot[] slots;
/*      */   private int count;
/*      */   private transient ExternalArrayData externalData;
/*      */   private transient Slot firstAdded;
/*      */   private transient Slot lastAdded;
/*      */   private volatile Map<Object, Object> associatedValues;
/*      */   private static final int SLOT_QUERY = 1;
/*      */   private static final int SLOT_MODIFY = 2;
/*      */   private static final int SLOT_MODIFY_CONST = 3;
/*      */   private static final int SLOT_MODIFY_GETTER_SETTER = 4;
/*      */   private static final int SLOT_CONVERT_ACCESSOR_TO_DATA = 5;
/*      */   private static final int INITIAL_SLOT_SIZE = 4;
/*      */   private boolean isExtensible = true;
/*      */   private static final Method GET_ARRAY_LENGTH;
/*      */   
/*      */   static {
/*      */     try {
/*  142 */       GET_ARRAY_LENGTH = ScriptableObject.class.getMethod("getExternalArrayLength", new Class[0]);
/*  143 */     } catch (NoSuchMethodException nsm) {
/*  144 */       throw new RuntimeException(nsm);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static class Slot
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -6090581677123995491L;
/*      */     String name;
/*      */     int indexOrHash;
/*      */     private volatile short attributes;
/*      */     volatile transient boolean wasDeleted;
/*      */     volatile Object value;
/*      */     transient Slot next;
/*      */     volatile transient Slot orderedNext;
/*      */     
/*      */     Slot(String name, int indexOrHash, int attributes) {
/*  161 */       this.name = name;
/*  162 */       this.indexOrHash = indexOrHash;
/*  163 */       this.attributes = (short)attributes;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  169 */       in.defaultReadObject();
/*  170 */       if (this.name != null) {
/*  171 */         this.indexOrHash = this.name.hashCode();
/*      */       }
/*      */     }
/*      */     
/*      */     boolean setValue(Object value, Scriptable owner, Scriptable start) {
/*  176 */       if ((this.attributes & 0x1) != 0) {
/*  177 */         return true;
/*      */       }
/*  179 */       if (owner == start) {
/*  180 */         this.value = value;
/*  181 */         return true;
/*      */       } 
/*  183 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     Object getValue(Scriptable start) {
/*  188 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     int getAttributes() {
/*  193 */       return this.attributes;
/*      */     }
/*      */ 
/*      */     
/*      */     synchronized void setAttributes(int value) {
/*  198 */       ScriptableObject.checkValidAttributes(value);
/*  199 */       this.attributes = (short)value;
/*      */     }
/*      */     
/*      */     void markDeleted() {
/*  203 */       this.wasDeleted = true;
/*  204 */       this.value = null;
/*  205 */       this.name = null;
/*      */     }
/*      */     
/*      */     ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
/*  209 */       return ScriptableObject.buildDataDescriptor(scope, this.value, this.attributes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static ScriptableObject buildDataDescriptor(Scriptable scope, Object value, int attributes) {
/*  217 */     ScriptableObject desc = new NativeObject();
/*  218 */     ScriptRuntime.setBuiltinProtoAndParent(desc, scope, TopLevel.Builtins.Object);
/*  219 */     desc.defineProperty("value", value, 0);
/*  220 */     desc.defineProperty("writable", Boolean.valueOf(((attributes & 0x1) == 0)), 0);
/*  221 */     desc.defineProperty("enumerable", Boolean.valueOf(((attributes & 0x2) == 0)), 0);
/*  222 */     desc.defineProperty("configurable", Boolean.valueOf(((attributes & 0x4) == 0)), 0);
/*  223 */     return desc;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class GetterSlot
/*      */     extends Slot
/*      */   {
/*      */     static final long serialVersionUID = -4900574849788797588L;
/*      */     Object getter;
/*      */     Object setter;
/*      */     
/*      */     GetterSlot(String name, int indexOrHash, int attributes) {
/*  235 */       super(name, indexOrHash, attributes);
/*      */     }
/*      */ 
/*      */     
/*      */     ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
/*  240 */       int attr = getAttributes();
/*  241 */       ScriptableObject desc = new NativeObject();
/*  242 */       ScriptRuntime.setBuiltinProtoAndParent(desc, scope, TopLevel.Builtins.Object);
/*  243 */       desc.defineProperty("enumerable", Boolean.valueOf(((attr & 0x2) == 0)), 0);
/*  244 */       desc.defineProperty("configurable", Boolean.valueOf(((attr & 0x4) == 0)), 0);
/*  245 */       if (this.getter != null) desc.defineProperty("get", this.getter, 0); 
/*  246 */       if (this.setter != null) desc.defineProperty("set", this.setter, 0); 
/*  247 */       return desc;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean setValue(Object value, Scriptable owner, Scriptable start) {
/*  252 */       if (this.setter == null) {
/*  253 */         if (this.getter != null) {
/*  254 */           if (Context.getContext().hasFeature(11))
/*      */           {
/*      */             
/*  257 */             throw ScriptRuntime.typeError1("msg.set.prop.no.setter", this.name);
/*      */           }
/*      */ 
/*      */           
/*  261 */           return true;
/*      */         } 
/*      */       } else {
/*  264 */         Context cx = Context.getContext();
/*  265 */         if (this.setter instanceof MemberBox) {
/*  266 */           Object setterThis, args[]; MemberBox nativeSetter = (MemberBox)this.setter;
/*  267 */           Class<?>[] pTypes = nativeSetter.argTypes;
/*      */ 
/*      */           
/*  270 */           Class<?> valueType = pTypes[pTypes.length - 1];
/*  271 */           int tag = FunctionObject.getTypeTag(valueType);
/*  272 */           Object actualArg = FunctionObject.convertArg(cx, start, value, tag);
/*      */ 
/*      */ 
/*      */           
/*  276 */           if (nativeSetter.delegateTo == null) {
/*  277 */             setterThis = start;
/*  278 */             args = new Object[] { actualArg };
/*      */           } else {
/*  280 */             setterThis = nativeSetter.delegateTo;
/*  281 */             args = new Object[] { start, actualArg };
/*      */           } 
/*  283 */           nativeSetter.invoke(setterThis, args);
/*  284 */         } else if (this.setter instanceof Function) {
/*  285 */           Function f = (Function)this.setter;
/*  286 */           f.call(cx, f.getParentScope(), start, new Object[] { value });
/*      */         } 
/*      */         
/*  289 */         return true;
/*      */       } 
/*  291 */       return super.setValue(value, owner, start);
/*      */     }
/*      */ 
/*      */     
/*      */     Object getValue(Scriptable start) {
/*  296 */       if (this.getter != null) {
/*  297 */         if (this.getter instanceof MemberBox) {
/*  298 */           Object getterThis, args[]; MemberBox nativeGetter = (MemberBox)this.getter;
/*      */ 
/*      */           
/*  301 */           if (nativeGetter.delegateTo == null) {
/*  302 */             getterThis = start;
/*  303 */             args = ScriptRuntime.emptyArgs;
/*      */           } else {
/*  305 */             getterThis = nativeGetter.delegateTo;
/*  306 */             args = new Object[] { start };
/*      */           } 
/*  308 */           return nativeGetter.invoke(getterThis, args);
/*  309 */         }  if (this.getter instanceof Function) {
/*  310 */           Function f = (Function)this.getter;
/*  311 */           Context cx = Context.getContext();
/*  312 */           return f.call(cx, f.getParentScope(), start, ScriptRuntime.emptyArgs);
/*      */         } 
/*      */       } 
/*      */       
/*  316 */       Object val = this.value;
/*  317 */       if (val instanceof LazilyLoadedCtor) {
/*  318 */         LazilyLoadedCtor initializer = (LazilyLoadedCtor)val;
/*      */         try {
/*  320 */           initializer.init();
/*      */         } finally {
/*  322 */           this.value = val = initializer.getValue();
/*      */         } 
/*      */       } 
/*  325 */       return val;
/*      */     }
/*      */ 
/*      */     
/*      */     void markDeleted() {
/*  330 */       super.markDeleted();
/*  331 */       this.getter = null;
/*  332 */       this.setter = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class RelinkedSlot
/*      */     extends Slot
/*      */   {
/*      */     final ScriptableObject.Slot slot;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     RelinkedSlot(ScriptableObject.Slot slot) {
/*  348 */       super(slot.name, slot.indexOrHash, slot.attributes);
/*      */       
/*  350 */       this.slot = ScriptableObject.unwrapSlot(slot);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean setValue(Object value, Scriptable owner, Scriptable start) {
/*  355 */       return this.slot.setValue(value, owner, start);
/*      */     }
/*      */ 
/*      */     
/*      */     Object getValue(Scriptable start) {
/*  360 */       return this.slot.getValue(start);
/*      */     }
/*      */ 
/*      */     
/*      */     ScriptableObject getPropertyDescriptor(Context cx, Scriptable scope) {
/*  365 */       return this.slot.getPropertyDescriptor(cx, scope);
/*      */     }
/*      */ 
/*      */     
/*      */     int getAttributes() {
/*  370 */       return this.slot.getAttributes();
/*      */     }
/*      */ 
/*      */     
/*      */     void setAttributes(int value) {
/*  375 */       this.slot.setAttributes(value);
/*      */     }
/*      */ 
/*      */     
/*      */     void markDeleted() {
/*  380 */       super.markDeleted();
/*  381 */       this.slot.markDeleted();
/*      */     }
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/*  385 */       out.writeObject(this.slot);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkValidAttributes(int attributes) {
/*  392 */     int mask = 15;
/*  393 */     if ((attributes & 0xFFFFFFF0) != 0) {
/*  394 */       throw new IllegalArgumentException(String.valueOf(attributes));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScriptableObject(Scriptable scope, Scriptable prototype) {
/*  404 */     if (scope == null) {
/*  405 */       throw new IllegalArgumentException();
/*      */     }
/*  407 */     this.parentScopeObject = scope;
/*  408 */     this.prototypeObject = prototype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTypeOf() {
/*  417 */     return avoidObjectDetection() ? "undefined" : "object";
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(String name, Scriptable start) {
/*  438 */     return (null != getSlot(name, 0, 1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(int index, Scriptable start) {
/*  450 */     if (this.externalData != null) {
/*  451 */       return (index < this.externalData.getArrayLength());
/*      */     }
/*  453 */     return (null != getSlot((String)null, index, 1));
/*      */   }
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
/*      */   public Object get(String name, Scriptable start) {
/*  468 */     Slot slot = getSlot(name, 0, 1);
/*  469 */     if (slot == null) {
/*  470 */       return Scriptable.NOT_FOUND;
/*      */     }
/*  472 */     return slot.getValue(start);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object get(int index, Scriptable start) {
/*  484 */     if (this.externalData != null) {
/*  485 */       if (index < this.externalData.getArrayLength()) {
/*  486 */         return this.externalData.getArrayElement(index);
/*      */       }
/*  488 */       return Scriptable.NOT_FOUND;
/*      */     } 
/*      */     
/*  491 */     Slot slot = getSlot((String)null, index, 1);
/*  492 */     if (slot == null) {
/*  493 */       return Scriptable.NOT_FOUND;
/*      */     }
/*  495 */     return slot.getValue(start);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(String name, Scriptable start, Object value) {
/*  515 */     if (putImpl(name, 0, start, value)) {
/*      */       return;
/*      */     }
/*  518 */     if (start == this) throw Kit.codeBug(); 
/*  519 */     start.put(name, start, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void put(int index, Scriptable start, Object value) {
/*  531 */     if (this.externalData != null) {
/*  532 */       if (index < this.externalData.getArrayLength()) {
/*  533 */         this.externalData.setArrayElement(index, value);
/*      */       } else {
/*  535 */         throw new JavaScriptException(ScriptRuntime.newNativeError(Context.getCurrentContext(), this, TopLevel.NativeErrors.RangeError, new Object[] { "External array index out of bounds " }), null, 0);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/*  544 */     if (putImpl(null, index, start, value)) {
/*      */       return;
/*      */     }
/*  547 */     if (start == this) throw Kit.codeBug(); 
/*  548 */     start.put(index, start, value);
/*      */   }
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
/*      */   public void delete(String name) {
/*  561 */     checkNotSealed(name, 0);
/*  562 */     removeSlot(name, 0);
/*      */   }
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
/*      */   public void delete(int index) {
/*  575 */     checkNotSealed(null, index);
/*  576 */     removeSlot(null, index);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putConst(String name, Scriptable start, Object value) {
/*  596 */     if (putConstImpl(name, 0, start, value, 1)) {
/*      */       return;
/*      */     }
/*  599 */     if (start == this) throw Kit.codeBug(); 
/*  600 */     if (start instanceof ConstProperties) {
/*  601 */       ((ConstProperties)start).putConst(name, start, value);
/*      */     } else {
/*  603 */       start.put(name, start, value);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void defineConst(String name, Scriptable start) {
/*  608 */     if (putConstImpl(name, 0, start, Undefined.instance, 8)) {
/*      */       return;
/*      */     }
/*  611 */     if (start == this) throw Kit.codeBug(); 
/*  612 */     if (start instanceof ConstProperties) {
/*  613 */       ((ConstProperties)start).defineConst(name, start);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConst(String name) {
/*  624 */     Slot slot = getSlot(name, 0, 1);
/*  625 */     if (slot == null) {
/*  626 */       return false;
/*      */     }
/*  628 */     return ((slot.getAttributes() & 0x5) == 5);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final int getAttributes(String name, Scriptable start) {
/*  640 */     return getAttributes(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final int getAttributes(int index, Scriptable start) {
/*  650 */     return getAttributes(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final void setAttributes(String name, Scriptable start, int attributes) {
/*  661 */     setAttributes(name, attributes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setAttributes(int index, Scriptable start, int attributes) {
/*  672 */     setAttributes(index, attributes);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAttributes(String name) {
/*  692 */     return findAttributeSlot(name, 0, 1).getAttributes();
/*      */   }
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
/*      */ 
/*      */   
/*      */   public int getAttributes(int index) {
/*  710 */     return findAttributeSlot(null, index, 1).getAttributes();
/*      */   }
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
/*      */   public void setAttributes(String name, int attributes) {
/*  736 */     checkNotSealed(name, 0);
/*  737 */     findAttributeSlot(name, 0, 2).setAttributes(attributes);
/*      */   }
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
/*      */   
/*      */   public void setAttributes(int index, int attributes) {
/*  754 */     checkNotSealed(null, index);
/*  755 */     findAttributeSlot(null, index, 2).setAttributes(attributes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGetterOrSetter(String name, int index, Callable getterOrSetter, boolean isSetter) {
/*  764 */     setGetterOrSetter(name, index, getterOrSetter, isSetter, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setGetterOrSetter(String name, int index, Callable getterOrSetter, boolean isSetter, boolean force) {
/*      */     GetterSlot gslot;
/*  770 */     if (name != null && index != 0) {
/*  771 */       throw new IllegalArgumentException(name);
/*      */     }
/*  773 */     if (!force) {
/*  774 */       checkNotSealed(name, index);
/*      */     }
/*      */ 
/*      */     
/*  778 */     if (isExtensible()) {
/*  779 */       gslot = (GetterSlot)getSlot(name, index, 4);
/*      */     } else {
/*  781 */       Slot slot = unwrapSlot(getSlot(name, index, 1));
/*  782 */       if (!(slot instanceof GetterSlot))
/*      */         return; 
/*  784 */       gslot = (GetterSlot)slot;
/*      */     } 
/*      */     
/*  787 */     if (!force) {
/*  788 */       int attributes = gslot.getAttributes();
/*  789 */       if ((attributes & 0x1) != 0) {
/*  790 */         throw Context.reportRuntimeError1("msg.modify.readonly", name);
/*      */       }
/*      */     } 
/*  793 */     if (isSetter) {
/*  794 */       gslot.setter = getterOrSetter;
/*      */     } else {
/*  796 */       gslot.getter = getterOrSetter;
/*      */     } 
/*  798 */     gslot.value = Undefined.instance;
/*      */   }
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
/*      */ 
/*      */   
/*      */   public Object getGetterOrSetter(String name, int index, boolean isSetter) {
/*  816 */     if (name != null && index != 0)
/*  817 */       throw new IllegalArgumentException(name); 
/*  818 */     Slot slot = unwrapSlot(getSlot(name, index, 1));
/*  819 */     if (slot == null)
/*  820 */       return null; 
/*  821 */     if (slot instanceof GetterSlot) {
/*  822 */       GetterSlot gslot = (GetterSlot)slot;
/*  823 */       Object result = isSetter ? gslot.setter : gslot.getter;
/*  824 */       return (result != null) ? result : Undefined.instance;
/*      */     } 
/*  826 */     return Undefined.instance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isGetterOrSetter(String name, int index, boolean setter) {
/*  837 */     Slot slot = unwrapSlot(getSlot(name, index, 1));
/*  838 */     if (slot instanceof GetterSlot) {
/*  839 */       if (setter && ((GetterSlot)slot).setter != null) return true; 
/*  840 */       if (!setter && ((GetterSlot)slot).getter != null) return true; 
/*      */     } 
/*  842 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void addLazilyInitializedValue(String name, int index, LazilyLoadedCtor init, int attributes) {
/*  848 */     if (name != null && index != 0)
/*  849 */       throw new IllegalArgumentException(name); 
/*  850 */     checkNotSealed(name, index);
/*  851 */     GetterSlot gslot = (GetterSlot)getSlot(name, index, 4);
/*      */     
/*  853 */     gslot.setAttributes(attributes);
/*  854 */     gslot.getter = null;
/*  855 */     gslot.setter = null;
/*  856 */     gslot.value = init;
/*      */   }
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
/*      */   public void setExternalArrayData(ExternalArrayData array) {
/*  871 */     this.externalData = array;
/*      */     
/*  873 */     if (array == null) {
/*  874 */       delete("length");
/*      */     } else {
/*      */       
/*  877 */       defineProperty("length", null, GET_ARRAY_LENGTH, null, 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ExternalArrayData getExternalArrayData() {
/*  890 */     return this.externalData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getExternalArrayLength() {
/*  898 */     return Integer.valueOf((this.externalData == null) ? 0 : this.externalData.getArrayLength());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Scriptable getPrototype() {
/*  906 */     return this.prototypeObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrototype(Scriptable m) {
/*  914 */     this.prototypeObject = m;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Scriptable getParentScope() {
/*  922 */     return this.parentScopeObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentScope(Scriptable m) {
/*  930 */     this.parentScopeObject = m;
/*      */   }
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
/*      */   public Object[] getIds() {
/*  945 */     return getIds(false);
/*      */   }
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
/*      */   public Object[] getAllIds() {
/*  960 */     return getIds(true);
/*      */   }
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getDefaultValue(Class<?> typeHint) {
/*  979 */     return getDefaultValue(this, typeHint);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object getDefaultValue(Scriptable object, Class<?> typeHint) {
/*  984 */     Context cx = null;
/*  985 */     for (int i = 0; i < 2; i++) {
/*      */       boolean tryToString; String methodName; Object[] args;
/*  987 */       if (typeHint == ScriptRuntime.StringClass) {
/*  988 */         tryToString = (i == 0);
/*      */       } else {
/*  990 */         tryToString = (i == 1);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  995 */       if (tryToString) {
/*  996 */         methodName = "toString";
/*  997 */         args = ScriptRuntime.emptyArgs;
/*      */       } else {
/*  999 */         String hint; methodName = "valueOf";
/* 1000 */         args = new Object[1];
/*      */         
/* 1002 */         if (typeHint == null) {
/* 1003 */           hint = "undefined";
/* 1004 */         } else if (typeHint == ScriptRuntime.StringClass) {
/* 1005 */           hint = "string";
/* 1006 */         } else if (typeHint == ScriptRuntime.ScriptableClass) {
/* 1007 */           hint = "object";
/* 1008 */         } else if (typeHint == ScriptRuntime.FunctionClass) {
/* 1009 */           hint = "function";
/* 1010 */         } else if (typeHint == ScriptRuntime.BooleanClass || typeHint == boolean.class) {
/*      */ 
/*      */           
/* 1013 */           hint = "boolean";
/* 1014 */         } else if (typeHint == ScriptRuntime.NumberClass || typeHint == ScriptRuntime.ByteClass || typeHint == byte.class || typeHint == ScriptRuntime.ShortClass || typeHint == short.class || typeHint == ScriptRuntime.IntegerClass || typeHint == int.class || typeHint == ScriptRuntime.FloatClass || typeHint == float.class || typeHint == ScriptRuntime.DoubleClass || typeHint == double.class) {
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
/* 1026 */           hint = "number";
/*      */         } else {
/* 1028 */           throw Context.reportRuntimeError1("msg.invalid.type", typeHint.toString());
/*      */         } 
/*      */         
/* 1031 */         args[0] = hint;
/*      */       } 
/* 1033 */       Object v = getProperty(object, methodName);
/* 1034 */       if (v instanceof Function) {
/*      */         
/* 1036 */         Function fun = (Function)v;
/* 1037 */         if (cx == null)
/* 1038 */           cx = Context.getContext(); 
/* 1039 */         Object object1 = fun.call(cx, fun.getParentScope(), object, args);
/* 1040 */         if (object1 != null) {
/* 1041 */           if (!(object1 instanceof Scriptable)) {
/* 1042 */             return object1;
/*      */           }
/* 1044 */           if (typeHint == ScriptRuntime.ScriptableClass || typeHint == ScriptRuntime.FunctionClass)
/*      */           {
/*      */             
/* 1047 */             return object1;
/*      */           }
/* 1049 */           if (tryToString && object1 instanceof Wrapper) {
/*      */ 
/*      */             
/* 1052 */             Object u = ((Wrapper)object1).unwrap();
/* 1053 */             if (u instanceof String)
/* 1054 */               return u; 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1059 */     String arg = (typeHint == null) ? "undefined" : typeHint.getName();
/* 1060 */     throw ScriptRuntime.typeError1("msg.default.value", arg);
/*      */   }
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
/*      */ 
/*      */   
/*      */   public boolean hasInstance(Scriptable instance) {
/* 1078 */     return ScriptRuntime.jsDelegatesTo(instance, this);
/*      */   }
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
/*      */   public boolean avoidObjectDetection() {
/* 1093 */     return false;
/*      */   }
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
/*      */ 
/*      */   
/*      */   protected Object equivalentValues(Object value) {
/* 1111 */     return (this == value) ? Boolean.TRUE : Scriptable.NOT_FOUND;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
/* 1212 */     defineClass(scope, clazz, false, false);
/*      */   }
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
/*      */ 
/*      */   
/*      */   public static <T extends Scriptable> void defineClass(Scriptable scope, Class<T> clazz, boolean sealed) throws IllegalAccessException, InstantiationException, InvocationTargetException {
/* 1243 */     defineClass(scope, clazz, sealed, false);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Scriptable> String defineClass(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
/* 1279 */     BaseFunction ctor = buildClassCtor(scope, clazz, sealed, mapInheritance);
/*      */     
/* 1281 */     if (ctor == null)
/* 1282 */       return null; 
/* 1283 */     String name = ctor.getClassPrototype().getClassName();
/* 1284 */     defineProperty(scope, name, ctor, 2);
/* 1285 */     return name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T extends Scriptable> BaseFunction buildClassCtor(Scriptable scope, Class<T> clazz, boolean sealed, boolean mapInheritance) throws IllegalAccessException, InstantiationException, InvocationTargetException {
/* 1295 */     Method[] methods = FunctionObject.getMethodList(clazz);
/* 1296 */     for (int i = 0; i < methods.length; i++) {
/* 1297 */       Method method = methods[i];
/* 1298 */       if (method.getName().equals("init")) {
/*      */         
/* 1300 */         Class<?>[] parmTypes = method.getParameterTypes();
/* 1301 */         if (parmTypes.length == 3 && parmTypes[0] == ScriptRuntime.ContextClass && parmTypes[1] == ScriptRuntime.ScriptableClass && parmTypes[2] == boolean.class && Modifier.isStatic(method.getModifiers())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1307 */           Object[] args = { Context.getContext(), scope, sealed ? Boolean.TRUE : Boolean.FALSE };
/*      */           
/* 1309 */           method.invoke((Object)null, args);
/* 1310 */           return null;
/*      */         } 
/* 1312 */         if (parmTypes.length == 1 && parmTypes[0] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method.getModifiers())) {
/*      */ 
/*      */ 
/*      */           
/* 1316 */           Object[] args = { scope };
/* 1317 */           method.invoke((Object)null, args);
/* 1318 */           return null;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1326 */     Constructor[] arrayOfConstructor = (Constructor[])clazz.getConstructors();
/* 1327 */     Constructor<?> protoCtor = null;
/* 1328 */     for (int j = 0; j < arrayOfConstructor.length; j++) {
/* 1329 */       if ((arrayOfConstructor[j].getParameterTypes()).length == 0) {
/* 1330 */         protoCtor = arrayOfConstructor[j];
/*      */         break;
/*      */       } 
/*      */     } 
/* 1334 */     if (protoCtor == null) {
/* 1335 */       throw Context.reportRuntimeError1("msg.zero.arg.ctor", clazz.getName());
/*      */     }
/*      */ 
/*      */     
/* 1339 */     Scriptable proto = (Scriptable)protoCtor.newInstance(ScriptRuntime.emptyArgs);
/* 1340 */     String className = proto.getClassName();
/*      */ 
/*      */     
/* 1343 */     Object existing = getProperty(getTopLevelScope(scope), className);
/* 1344 */     if (existing instanceof BaseFunction) {
/* 1345 */       Object existingProto = ((BaseFunction)existing).getPrototypeProperty();
/* 1346 */       if (existingProto != null && clazz.equals(existingProto.getClass())) {
/* 1347 */         return (BaseFunction)existing;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1353 */     Scriptable superProto = null;
/* 1354 */     if (mapInheritance) {
/* 1355 */       Class<? super T> superClass = clazz.getSuperclass();
/* 1356 */       if (ScriptRuntime.ScriptableClass.isAssignableFrom(superClass) && !Modifier.isAbstract(superClass.getModifiers())) {
/*      */ 
/*      */         
/* 1359 */         Class<? extends Scriptable> superScriptable = extendsScriptable(superClass);
/*      */         
/* 1361 */         String name = defineClass(scope, superScriptable, sealed, mapInheritance);
/*      */         
/* 1363 */         if (name != null) {
/* 1364 */           superProto = getClassPrototype(scope, name);
/*      */         }
/*      */       } 
/*      */     } 
/* 1368 */     if (superProto == null) {
/* 1369 */       superProto = getObjectPrototype(scope);
/*      */     }
/* 1371 */     proto.setPrototype(superProto);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1376 */     String functionPrefix = "jsFunction_";
/* 1377 */     String staticFunctionPrefix = "jsStaticFunction_";
/* 1378 */     String getterPrefix = "jsGet_";
/* 1379 */     String setterPrefix = "jsSet_";
/* 1380 */     String ctorName = "jsConstructor";
/*      */     
/* 1382 */     Member ctorMember = findAnnotatedMember((AccessibleObject[])methods, (Class)JSConstructor.class);
/* 1383 */     if (ctorMember == null) {
/* 1384 */       ctorMember = findAnnotatedMember((AccessibleObject[])arrayOfConstructor, (Class)JSConstructor.class);
/*      */     }
/* 1386 */     if (ctorMember == null) {
/* 1387 */       ctorMember = FunctionObject.findSingleMethod(methods, "jsConstructor");
/*      */     }
/* 1389 */     if (ctorMember == null) {
/* 1390 */       if (arrayOfConstructor.length == 1) {
/* 1391 */         ctorMember = arrayOfConstructor[0];
/* 1392 */       } else if (arrayOfConstructor.length == 2) {
/* 1393 */         if ((arrayOfConstructor[0].getParameterTypes()).length == 0) {
/* 1394 */           ctorMember = arrayOfConstructor[1];
/* 1395 */         } else if ((arrayOfConstructor[1].getParameterTypes()).length == 0) {
/* 1396 */           ctorMember = arrayOfConstructor[0];
/*      */         } 
/* 1398 */       }  if (ctorMember == null) {
/* 1399 */         throw Context.reportRuntimeError1("msg.ctor.multiple.parms", clazz.getName());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1404 */     FunctionObject ctor = new FunctionObject(className, ctorMember, scope);
/* 1405 */     if (ctor.isVarArgsMethod()) {
/* 1406 */       throw Context.reportRuntimeError1("msg.varargs.ctor", ctorMember.getName());
/*      */     }
/*      */     
/* 1409 */     ctor.initAsConstructor(scope, proto);
/*      */     
/* 1411 */     Method finishInit = null;
/* 1412 */     HashSet<String> staticNames = new HashSet<String>();
/* 1413 */     HashSet<String> instanceNames = new HashSet<String>();
/* 1414 */     for (Method method : methods) {
/* 1415 */       if (method == ctorMember) {
/*      */         continue;
/*      */       }
/* 1418 */       String name = method.getName();
/* 1419 */       if (name.equals("finishInit")) {
/* 1420 */         Class<?>[] parmTypes = method.getParameterTypes();
/* 1421 */         if (parmTypes.length == 3 && parmTypes[0] == ScriptRuntime.ScriptableClass && parmTypes[1] == FunctionObject.class && parmTypes[2] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method.getModifiers())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1427 */           finishInit = method;
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/* 1432 */       if (name.indexOf('$') != -1)
/*      */         continue; 
/* 1434 */       if (name.equals("jsConstructor")) {
/*      */         continue;
/*      */       }
/* 1437 */       Annotation annotation = null;
/* 1438 */       String prefix = null;
/* 1439 */       if (method.isAnnotationPresent((Class)JSFunction.class)) {
/* 1440 */         annotation = method.getAnnotation(JSFunction.class);
/* 1441 */       } else if (method.isAnnotationPresent((Class)JSStaticFunction.class)) {
/* 1442 */         annotation = method.getAnnotation(JSStaticFunction.class);
/* 1443 */       } else if (method.isAnnotationPresent((Class)JSGetter.class)) {
/* 1444 */         annotation = method.getAnnotation(JSGetter.class);
/* 1445 */       } else if (method.isAnnotationPresent((Class)JSSetter.class)) {
/*      */         continue;
/*      */       } 
/*      */       
/* 1449 */       if (annotation == null) {
/* 1450 */         if (name.startsWith("jsFunction_")) {
/* 1451 */           prefix = "jsFunction_";
/* 1452 */         } else if (name.startsWith("jsStaticFunction_")) {
/* 1453 */           prefix = "jsStaticFunction_";
/* 1454 */         } else if (name.startsWith("jsGet_")) {
/* 1455 */           prefix = "jsGet_";
/* 1456 */         } else if (annotation == null) {
/*      */           continue;
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1463 */       boolean isStatic = (annotation instanceof JSStaticFunction || prefix == "jsStaticFunction_");
/*      */       
/* 1465 */       HashSet<String> names = isStatic ? staticNames : instanceNames;
/* 1466 */       String propName = getPropertyName(name, prefix, annotation);
/* 1467 */       if (names.contains(propName)) {
/* 1468 */         throw Context.reportRuntimeError2("duplicate.defineClass.name", name, propName);
/*      */       }
/*      */       
/* 1471 */       names.add(propName);
/* 1472 */       name = propName;
/*      */       
/* 1474 */       if (annotation instanceof JSGetter || prefix == "jsGet_") {
/* 1475 */         if (!(proto instanceof ScriptableObject)) {
/* 1476 */           throw Context.reportRuntimeError2("msg.extend.scriptable", proto.getClass().toString(), name);
/*      */         }
/*      */ 
/*      */         
/* 1480 */         Method setter = findSetterMethod(methods, name, "jsSet_");
/* 1481 */         int attr = 0x6 | ((setter != null) ? 0 : 1);
/*      */ 
/*      */ 
/*      */         
/* 1485 */         ((ScriptableObject)proto).defineProperty(name, null, method, setter, attr);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1491 */         if (isStatic && !Modifier.isStatic(method.getModifiers())) {
/* 1492 */           throw Context.reportRuntimeError("jsStaticFunction must be used with static method.");
/*      */         }
/*      */ 
/*      */         
/* 1496 */         FunctionObject f = new FunctionObject(name, method, proto);
/* 1497 */         if (f.isVarArgsConstructor()) {
/* 1498 */           throw Context.reportRuntimeError1("msg.varargs.fun", ctorMember.getName());
/*      */         }
/*      */         
/* 1501 */         defineProperty(isStatic ? ctor : proto, name, f, 2);
/* 1502 */         if (sealed) {
/* 1503 */           f.sealObject();
/*      */         }
/*      */       } 
/*      */       continue;
/*      */     } 
/* 1508 */     if (finishInit != null) {
/* 1509 */       Object[] finishArgs = { scope, ctor, proto };
/* 1510 */       finishInit.invoke((Object)null, finishArgs);
/*      */     } 
/*      */ 
/*      */     
/* 1514 */     if (sealed) {
/* 1515 */       ctor.sealObject();
/* 1516 */       if (proto instanceof ScriptableObject) {
/* 1517 */         ((ScriptableObject)proto).sealObject();
/*      */       }
/*      */     } 
/*      */     
/* 1521 */     return ctor;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Member findAnnotatedMember(AccessibleObject[] members, Class<? extends Annotation> annotation) {
/* 1526 */     for (AccessibleObject member : members) {
/* 1527 */       if (member.isAnnotationPresent(annotation)) {
/* 1528 */         return (Member)member;
/*      */       }
/*      */     } 
/* 1531 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Method findSetterMethod(Method[] methods, String name, String prefix) {
/* 1537 */     String newStyleName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
/*      */ 
/*      */     
/* 1540 */     for (Method method : methods) {
/* 1541 */       JSSetter annotation = method.<JSSetter>getAnnotation(JSSetter.class);
/* 1542 */       if (annotation != null && (
/* 1543 */         name.equals(annotation.value()) || ("".equals(annotation.value()) && newStyleName.equals(method.getName()))))
/*      */       {
/* 1545 */         return method;
/*      */       }
/*      */     } 
/*      */     
/* 1549 */     String oldStyleName = prefix + name;
/* 1550 */     for (Method method : methods) {
/* 1551 */       if (oldStyleName.equals(method.getName())) {
/* 1552 */         return method;
/*      */       }
/*      */     } 
/* 1555 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getPropertyName(String methodName, String prefix, Annotation annotation) {
/* 1561 */     if (prefix != null) {
/* 1562 */       return methodName.substring(prefix.length());
/*      */     }
/* 1564 */     String propName = null;
/* 1565 */     if (annotation instanceof JSGetter) {
/* 1566 */       propName = ((JSGetter)annotation).value();
/* 1567 */       if ((propName == null || propName.length() == 0) && 
/* 1568 */         methodName.length() > 3 && methodName.startsWith("get")) {
/* 1569 */         propName = methodName.substring(3);
/* 1570 */         if (Character.isUpperCase(propName.charAt(0))) {
/* 1571 */           if (propName.length() == 1) {
/* 1572 */             propName = propName.toLowerCase();
/* 1573 */           } else if (!Character.isUpperCase(propName.charAt(1))) {
/* 1574 */             propName = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);
/*      */           }
/*      */         
/*      */         }
/*      */       }
/*      */     
/* 1580 */     } else if (annotation instanceof JSFunction) {
/* 1581 */       propName = ((JSFunction)annotation).value();
/* 1582 */     } else if (annotation instanceof JSStaticFunction) {
/* 1583 */       propName = ((JSStaticFunction)annotation).value();
/*      */     } 
/* 1585 */     if (propName == null || propName.length() == 0) {
/* 1586 */       propName = methodName;
/*      */     }
/* 1588 */     return propName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Scriptable> Class<T> extendsScriptable(Class<?> c) {
/* 1594 */     if (ScriptRuntime.ScriptableClass.isAssignableFrom(c))
/* 1595 */       return (Class)c; 
/* 1596 */     return null;
/*      */   }
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
/*      */   public void defineProperty(String propertyName, Object value, int attributes) {
/* 1612 */     checkNotSealed(propertyName, 0);
/* 1613 */     put(propertyName, this, value);
/* 1614 */     setAttributes(propertyName, attributes);
/*      */   }
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
/*      */   public static void defineProperty(Scriptable destination, String propertyName, Object value, int attributes) {
/* 1627 */     if (!(destination instanceof ScriptableObject)) {
/* 1628 */       destination.put(propertyName, destination, value);
/*      */       return;
/*      */     } 
/* 1631 */     ScriptableObject so = (ScriptableObject)destination;
/* 1632 */     so.defineProperty(propertyName, value, attributes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void defineConstProperty(Scriptable destination, String propertyName) {
/* 1644 */     if (destination instanceof ConstProperties) {
/* 1645 */       ConstProperties cp = (ConstProperties)destination;
/* 1646 */       cp.defineConst(propertyName, destination);
/*      */     } else {
/* 1648 */       defineProperty(destination, propertyName, Undefined.instance, 13);
/*      */     } 
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineProperty(String propertyName, Class<?> clazz, int attributes) {
/* 1672 */     int length = propertyName.length();
/* 1673 */     if (length == 0) throw new IllegalArgumentException(); 
/* 1674 */     char[] buf = new char[3 + length];
/* 1675 */     propertyName.getChars(0, length, buf, 3);
/* 1676 */     buf[3] = Character.toUpperCase(buf[3]);
/* 1677 */     buf[0] = 'g';
/* 1678 */     buf[1] = 'e';
/* 1679 */     buf[2] = 't';
/* 1680 */     String getterName = new String(buf);
/* 1681 */     buf[0] = 's';
/* 1682 */     String setterName = new String(buf);
/*      */     
/* 1684 */     Method[] methods = FunctionObject.getMethodList(clazz);
/* 1685 */     Method getter = FunctionObject.findSingleMethod(methods, getterName);
/* 1686 */     Method setter = FunctionObject.findSingleMethod(methods, setterName);
/* 1687 */     if (setter == null)
/* 1688 */       attributes |= 0x1; 
/* 1689 */     defineProperty(propertyName, null, getter, (setter == null) ? null : setter, attributes);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineProperty(String propertyName, Object delegateTo, Method getter, Method setter, int attributes) {
/* 1737 */     MemberBox getterBox = null;
/* 1738 */     if (getter != null) {
/* 1739 */       boolean delegatedForm; getterBox = new MemberBox(getter);
/*      */ 
/*      */       
/* 1742 */       if (!Modifier.isStatic(getter.getModifiers())) {
/* 1743 */         delegatedForm = (delegateTo != null);
/* 1744 */         getterBox.delegateTo = delegateTo;
/*      */       } else {
/* 1746 */         delegatedForm = true;
/*      */ 
/*      */         
/* 1749 */         getterBox.delegateTo = void.class;
/*      */       } 
/*      */       
/* 1752 */       String errorId = null;
/* 1753 */       Class<?>[] parmTypes = getter.getParameterTypes();
/* 1754 */       if (parmTypes.length == 0) {
/* 1755 */         if (delegatedForm) {
/* 1756 */           errorId = "msg.obj.getter.parms";
/*      */         }
/* 1758 */       } else if (parmTypes.length == 1) {
/* 1759 */         Object<?> argType = (Object<?>)parmTypes[0];
/*      */         
/* 1761 */         if (argType != ScriptRuntime.ScriptableClass && argType != ScriptRuntime.ScriptableObjectClass) {
/*      */ 
/*      */           
/* 1764 */           errorId = "msg.bad.getter.parms";
/* 1765 */         } else if (!delegatedForm) {
/* 1766 */           errorId = "msg.bad.getter.parms";
/*      */         } 
/*      */       } else {
/* 1769 */         errorId = "msg.bad.getter.parms";
/*      */       } 
/* 1771 */       if (errorId != null) {
/* 1772 */         throw Context.reportRuntimeError1(errorId, getter.toString());
/*      */       }
/*      */     } 
/*      */     
/* 1776 */     MemberBox setterBox = null;
/* 1777 */     if (setter != null) {
/* 1778 */       boolean delegatedForm; if (setter.getReturnType() != void.class) {
/* 1779 */         throw Context.reportRuntimeError1("msg.setter.return", setter.toString());
/*      */       }
/*      */       
/* 1782 */       setterBox = new MemberBox(setter);
/*      */ 
/*      */       
/* 1785 */       if (!Modifier.isStatic(setter.getModifiers())) {
/* 1786 */         delegatedForm = (delegateTo != null);
/* 1787 */         setterBox.delegateTo = delegateTo;
/*      */       } else {
/* 1789 */         delegatedForm = true;
/*      */ 
/*      */         
/* 1792 */         setterBox.delegateTo = void.class;
/*      */       } 
/*      */       
/* 1795 */       String errorId = null;
/* 1796 */       Class<?>[] parmTypes = setter.getParameterTypes();
/* 1797 */       if (parmTypes.length == 1) {
/* 1798 */         if (delegatedForm) {
/* 1799 */           errorId = "msg.setter2.expected";
/*      */         }
/* 1801 */       } else if (parmTypes.length == 2) {
/* 1802 */         Object<?> argType = (Object<?>)parmTypes[0];
/*      */         
/* 1804 */         if (argType != ScriptRuntime.ScriptableClass && argType != ScriptRuntime.ScriptableObjectClass) {
/*      */ 
/*      */           
/* 1807 */           errorId = "msg.setter2.parms";
/* 1808 */         } else if (!delegatedForm) {
/* 1809 */           errorId = "msg.setter1.parms";
/*      */         } 
/*      */       } else {
/* 1812 */         errorId = "msg.setter.parms";
/*      */       } 
/* 1814 */       if (errorId != null) {
/* 1815 */         throw Context.reportRuntimeError1(errorId, setter.toString());
/*      */       }
/*      */     } 
/*      */     
/* 1819 */     GetterSlot gslot = (GetterSlot)getSlot(propertyName, 0, 4);
/*      */     
/* 1821 */     gslot.setAttributes(attributes);
/* 1822 */     gslot.getter = getterBox;
/* 1823 */     gslot.setter = setterBox;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineOwnProperties(Context cx, ScriptableObject props) {
/* 1833 */     Object[] ids = props.getIds();
/* 1834 */     ScriptableObject[] descs = new ScriptableObject[ids.length]; int i, len;
/* 1835 */     for (i = 0, len = ids.length; i < len; i++) {
/* 1836 */       Object descObj = ScriptRuntime.getObjectElem(props, ids[i], cx);
/* 1837 */       ScriptableObject desc = ensureScriptableObject(descObj);
/* 1838 */       checkPropertyDefinition(desc);
/* 1839 */       descs[i] = desc;
/*      */     } 
/* 1841 */     for (i = 0, len = ids.length; i < len; i++) {
/* 1842 */       defineOwnProperty(cx, ids[i], descs[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineOwnProperty(Context cx, Object id, ScriptableObject desc) {
/* 1854 */     checkPropertyDefinition(desc);
/* 1855 */     defineOwnProperty(cx, id, desc, true);
/*      */   }
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
/*      */   protected void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid) {
/*      */     int attributes;
/* 1871 */     Slot slot = getSlot(cx, id, 1);
/* 1872 */     boolean isNew = (slot == null);
/*      */     
/* 1874 */     if (checkValid) {
/* 1875 */       ScriptableObject current = (slot == null) ? null : slot.getPropertyDescriptor(cx, this);
/*      */       
/* 1877 */       String name = ScriptRuntime.toString(id);
/* 1878 */       checkPropertyChange(name, current, desc);
/*      */     } 
/*      */     
/* 1881 */     boolean isAccessor = isAccessorDescriptor(desc);
/*      */ 
/*      */     
/* 1884 */     if (slot == null) {
/* 1885 */       slot = getSlot(cx, id, isAccessor ? 4 : 2);
/* 1886 */       attributes = applyDescriptorToAttributeBitset(7, desc);
/*      */     } else {
/* 1888 */       attributes = applyDescriptorToAttributeBitset(slot.getAttributes(), desc);
/*      */     } 
/*      */     
/* 1891 */     slot = unwrapSlot(slot);
/*      */     
/* 1893 */     if (isAccessor) {
/* 1894 */       if (!(slot instanceof GetterSlot)) {
/* 1895 */         slot = getSlot(cx, id, 4);
/*      */       }
/*      */       
/* 1898 */       GetterSlot gslot = (GetterSlot)slot;
/*      */       
/* 1900 */       Object getter = getProperty(desc, "get");
/* 1901 */       if (getter != NOT_FOUND) {
/* 1902 */         gslot.getter = getter;
/*      */       }
/* 1904 */       Object setter = getProperty(desc, "set");
/* 1905 */       if (setter != NOT_FOUND) {
/* 1906 */         gslot.setter = setter;
/*      */       }
/*      */       
/* 1909 */       gslot.value = Undefined.instance;
/* 1910 */       gslot.setAttributes(attributes);
/*      */     } else {
/* 1912 */       if (slot instanceof GetterSlot && isDataDescriptor(desc)) {
/* 1913 */         slot = getSlot(cx, id, 5);
/*      */       }
/*      */       
/* 1916 */       Object value = getProperty(desc, "value");
/* 1917 */       if (value != NOT_FOUND) {
/* 1918 */         slot.value = value;
/* 1919 */       } else if (isNew) {
/* 1920 */         slot.value = Undefined.instance;
/*      */       } 
/* 1922 */       slot.setAttributes(attributes);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void checkPropertyDefinition(ScriptableObject desc) {
/* 1927 */     Object getter = getProperty(desc, "get");
/* 1928 */     if (getter != NOT_FOUND && getter != Undefined.instance && !(getter instanceof Callable))
/*      */     {
/* 1930 */       throw ScriptRuntime.notFunctionError(getter);
/*      */     }
/* 1932 */     Object setter = getProperty(desc, "set");
/* 1933 */     if (setter != NOT_FOUND && setter != Undefined.instance && !(setter instanceof Callable))
/*      */     {
/* 1935 */       throw ScriptRuntime.notFunctionError(setter);
/*      */     }
/* 1937 */     if (isDataDescriptor(desc) && isAccessorDescriptor(desc)) {
/* 1938 */       throw ScriptRuntime.typeError0("msg.both.data.and.accessor.desc");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkPropertyChange(String id, ScriptableObject current, ScriptableObject desc) {
/* 1944 */     if (current == null) {
/* 1945 */       if (!isExtensible()) throw ScriptRuntime.typeError0("msg.not.extensible");
/*      */     
/* 1947 */     } else if (isFalse(current.get("configurable", current))) {
/* 1948 */       if (isTrue(getProperty(desc, "configurable"))) {
/* 1949 */         throw ScriptRuntime.typeError1("msg.change.configurable.false.to.true", id);
/*      */       }
/* 1951 */       if (isTrue(current.get("enumerable", current)) != isTrue(getProperty(desc, "enumerable"))) {
/* 1952 */         throw ScriptRuntime.typeError1("msg.change.enumerable.with.configurable.false", id);
/*      */       }
/* 1954 */       boolean isData = isDataDescriptor(desc);
/* 1955 */       boolean isAccessor = isAccessorDescriptor(desc);
/* 1956 */       if (isData || isAccessor)
/*      */       {
/* 1958 */         if (isData && isDataDescriptor(current)) {
/* 1959 */           if (isFalse(current.get("writable", current))) {
/* 1960 */             if (isTrue(getProperty(desc, "writable"))) {
/* 1961 */               throw ScriptRuntime.typeError1("msg.change.writable.false.to.true.with.configurable.false", id);
/*      */             }
/*      */             
/* 1964 */             if (!sameValue(getProperty(desc, "value"), current.get("value", current))) {
/* 1965 */               throw ScriptRuntime.typeError1("msg.change.value.with.writable.false", id);
/*      */             }
/*      */           } 
/* 1968 */         } else if (isAccessor && isAccessorDescriptor(current)) {
/* 1969 */           if (!sameValue(getProperty(desc, "set"), current.get("set", current))) {
/* 1970 */             throw ScriptRuntime.typeError1("msg.change.setter.with.configurable.false", id);
/*      */           }
/*      */           
/* 1973 */           if (!sameValue(getProperty(desc, "get"), current.get("get", current))) {
/* 1974 */             throw ScriptRuntime.typeError1("msg.change.getter.with.configurable.false", id);
/*      */           }
/*      */         } else {
/* 1977 */           if (isDataDescriptor(current)) {
/* 1978 */             throw ScriptRuntime.typeError1("msg.change.property.data.to.accessor.with.configurable.false", id);
/*      */           }
/*      */           
/* 1981 */           throw ScriptRuntime.typeError1("msg.change.property.accessor.to.data.with.configurable.false", id);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected static boolean isTrue(Object value) {
/* 1989 */     return (value != NOT_FOUND && ScriptRuntime.toBoolean(value));
/*      */   }
/*      */   
/*      */   protected static boolean isFalse(Object value) {
/* 1993 */     return !isTrue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean sameValue(Object newValue, Object currentValue) {
/* 2004 */     if (newValue == NOT_FOUND) {
/* 2005 */       return true;
/*      */     }
/* 2007 */     if (currentValue == NOT_FOUND) {
/* 2008 */       currentValue = Undefined.instance;
/*      */     }
/*      */ 
/*      */     
/* 2012 */     if (currentValue instanceof Number && newValue instanceof Number) {
/* 2013 */       double d1 = ((Number)currentValue).doubleValue();
/* 2014 */       double d2 = ((Number)newValue).doubleValue();
/* 2015 */       if (Double.isNaN(d1) && Double.isNaN(d2)) {
/* 2016 */         return true;
/*      */       }
/* 2018 */       if (d1 == 0.0D && Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2)) {
/* 2019 */         return false;
/*      */       }
/*      */     } 
/* 2022 */     return ScriptRuntime.shallowEq(currentValue, newValue);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int applyDescriptorToAttributeBitset(int attributes, ScriptableObject desc) {
/* 2028 */     Object enumerable = getProperty(desc, "enumerable");
/* 2029 */     if (enumerable != NOT_FOUND) {
/* 2030 */       attributes = ScriptRuntime.toBoolean(enumerable) ? (attributes & 0xFFFFFFFD) : (attributes | 0x2);
/*      */     }
/*      */ 
/*      */     
/* 2034 */     Object writable = getProperty(desc, "writable");
/* 2035 */     if (writable != NOT_FOUND) {
/* 2036 */       attributes = ScriptRuntime.toBoolean(writable) ? (attributes & 0xFFFFFFFE) : (attributes | 0x1);
/*      */     }
/*      */ 
/*      */     
/* 2040 */     Object configurable = getProperty(desc, "configurable");
/* 2041 */     if (configurable != NOT_FOUND) {
/* 2042 */       attributes = ScriptRuntime.toBoolean(configurable) ? (attributes & 0xFFFFFFFB) : (attributes | 0x4);
/*      */     }
/*      */ 
/*      */     
/* 2046 */     return attributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isDataDescriptor(ScriptableObject desc) {
/* 2055 */     return (hasProperty(desc, "value") || hasProperty(desc, "writable"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isAccessorDescriptor(ScriptableObject desc) {
/* 2064 */     return (hasProperty(desc, "get") || hasProperty(desc, "set"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isGenericDescriptor(ScriptableObject desc) {
/* 2073 */     return (!isDataDescriptor(desc) && !isAccessorDescriptor(desc));
/*      */   }
/*      */   
/*      */   protected static Scriptable ensureScriptable(Object arg) {
/* 2077 */     if (!(arg instanceof Scriptable))
/* 2078 */       throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(arg)); 
/* 2079 */     return (Scriptable)arg;
/*      */   }
/*      */   
/*      */   protected static ScriptableObject ensureScriptableObject(Object arg) {
/* 2083 */     if (!(arg instanceof ScriptableObject))
/* 2084 */       throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(arg)); 
/* 2085 */     return (ScriptableObject)arg;
/*      */   }
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineFunctionProperties(String[] names, Class<?> clazz, int attributes) {
/* 2104 */     Method[] methods = FunctionObject.getMethodList(clazz);
/* 2105 */     for (int i = 0; i < names.length; i++) {
/* 2106 */       String name = names[i];
/* 2107 */       Method m = FunctionObject.findSingleMethod(methods, name);
/* 2108 */       if (m == null) {
/* 2109 */         throw Context.reportRuntimeError2("msg.method.not.found", name, clazz.getName());
/*      */       }
/*      */       
/* 2112 */       FunctionObject f = new FunctionObject(name, m, this);
/* 2113 */       defineProperty(name, f, attributes);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable getObjectPrototype(Scriptable scope) {
/* 2122 */     return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable getFunctionPrototype(Scriptable scope) {
/* 2131 */     return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Function);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Scriptable getArrayPrototype(Scriptable scope) {
/* 2136 */     return TopLevel.getBuiltinPrototype(getTopLevelScope(scope), TopLevel.Builtins.Array);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Scriptable getClassPrototype(Scriptable scope, String className) {
/*      */     Object proto;
/* 2158 */     scope = getTopLevelScope(scope);
/* 2159 */     Object ctor = getProperty(scope, className);
/*      */     
/* 2161 */     if (ctor instanceof BaseFunction) {
/* 2162 */       proto = ((BaseFunction)ctor).getPrototypeProperty();
/* 2163 */     } else if (ctor instanceof Scriptable) {
/* 2164 */       Scriptable ctorObj = (Scriptable)ctor;
/* 2165 */       proto = ctorObj.get("prototype", ctorObj);
/*      */     } else {
/* 2167 */       return null;
/*      */     } 
/* 2169 */     if (proto instanceof Scriptable) {
/* 2170 */       return (Scriptable)proto;
/*      */     }
/* 2172 */     return null;
/*      */   }
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
/*      */   public static Scriptable getTopLevelScope(Scriptable obj) {
/*      */     while (true) {
/* 2187 */       Scriptable parent = obj.getParentScope();
/* 2188 */       if (parent == null) {
/* 2189 */         return obj;
/*      */       }
/* 2191 */       obj = parent;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isExtensible() {
/* 2196 */     return this.isExtensible;
/*      */   }
/*      */   
/*      */   public void preventExtensions() {
/* 2200 */     this.isExtensible = false;
/*      */   }
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
/*      */   public synchronized void sealObject() {
/* 2213 */     if (this.count >= 0) {
/*      */       
/* 2215 */       Slot slot = this.firstAdded;
/* 2216 */       while (slot != null) {
/* 2217 */         Object value = slot.value;
/* 2218 */         if (value instanceof LazilyLoadedCtor) {
/* 2219 */           LazilyLoadedCtor initializer = (LazilyLoadedCtor)value;
/*      */           try {
/* 2221 */             initializer.init();
/*      */           } finally {
/* 2223 */             slot.value = initializer.getValue();
/*      */           } 
/*      */         } 
/* 2226 */         slot = slot.orderedNext;
/*      */       } 
/* 2228 */       this.count ^= 0xFFFFFFFF;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSealed() {
/* 2240 */     return (this.count < 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkNotSealed(String name, int index) {
/* 2245 */     if (!isSealed()) {
/*      */       return;
/*      */     }
/* 2248 */     String str = (name != null) ? name : Integer.toString(index);
/* 2249 */     throw Context.reportRuntimeError1("msg.modify.sealed", str);
/*      */   }
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
/*      */   public static Object getProperty(Scriptable obj, String name) {
/*      */     Object result;
/* 2266 */     Scriptable start = obj;
/*      */     
/*      */     do {
/* 2269 */       result = obj.get(name, start);
/* 2270 */       if (result != Scriptable.NOT_FOUND)
/*      */         break; 
/* 2272 */       obj = obj.getPrototype();
/* 2273 */     } while (obj != null);
/* 2274 */     return result;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getTypedProperty(Scriptable s, int index, Class<T> type) {
/* 2297 */     Object val = getProperty(s, index);
/* 2298 */     if (val == Scriptable.NOT_FOUND) {
/* 2299 */       val = null;
/*      */     }
/* 2301 */     return type.cast(Context.jsToJava(val, type));
/*      */   }
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getProperty(Scriptable obj, int index) {
/*      */     Object result;
/* 2321 */     Scriptable start = obj;
/*      */     
/*      */     do {
/* 2324 */       result = obj.get(index, start);
/* 2325 */       if (result != Scriptable.NOT_FOUND)
/*      */         break; 
/* 2327 */       obj = obj.getPrototype();
/* 2328 */     } while (obj != null);
/* 2329 */     return result;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getTypedProperty(Scriptable s, String name, Class<T> type) {
/* 2349 */     Object val = getProperty(s, name);
/* 2350 */     if (val == Scriptable.NOT_FOUND) {
/* 2351 */       val = null;
/*      */     }
/* 2353 */     return type.cast(Context.jsToJava(val, type));
/*      */   }
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
/*      */   public static boolean hasProperty(Scriptable obj, String name) {
/* 2369 */     return (null != getBase(obj, name));
/*      */   }
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
/*      */   public static void redefineProperty(Scriptable obj, String name, boolean isConst) {
/* 2384 */     Scriptable base = getBase(obj, name);
/* 2385 */     if (base == null)
/*      */       return; 
/* 2387 */     if (base instanceof ConstProperties) {
/* 2388 */       ConstProperties cp = (ConstProperties)base;
/*      */       
/* 2390 */       if (cp.isConst(name))
/* 2391 */         throw ScriptRuntime.typeError1("msg.const.redecl", name); 
/*      */     } 
/* 2393 */     if (isConst) {
/* 2394 */       throw ScriptRuntime.typeError1("msg.var.redecl", name);
/*      */     }
/*      */   }
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
/*      */   public static boolean hasProperty(Scriptable obj, int index) {
/* 2409 */     return (null != getBase(obj, index));
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void putProperty(Scriptable obj, String name, Object value) {
/* 2429 */     Scriptable base = getBase(obj, name);
/* 2430 */     if (base == null)
/* 2431 */       base = obj; 
/* 2432 */     base.put(name, obj, value);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void putConstProperty(Scriptable obj, String name, Object value) {
/* 2452 */     Scriptable base = getBase(obj, name);
/* 2453 */     if (base == null)
/* 2454 */       base = obj; 
/* 2455 */     if (base instanceof ConstProperties) {
/* 2456 */       ((ConstProperties)base).putConst(name, obj, value);
/*      */     }
/*      */   }
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void putProperty(Scriptable obj, int index, Object value) {
/* 2476 */     Scriptable base = getBase(obj, index);
/* 2477 */     if (base == null)
/* 2478 */       base = obj; 
/* 2479 */     base.put(index, obj, value);
/*      */   }
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
/*      */   public static boolean deleteProperty(Scriptable obj, String name) {
/* 2495 */     Scriptable base = getBase(obj, name);
/* 2496 */     if (base == null)
/* 2497 */       return true; 
/* 2498 */     base.delete(name);
/* 2499 */     return !base.has(name, obj);
/*      */   }
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
/*      */   public static boolean deleteProperty(Scriptable obj, int index) {
/* 2515 */     Scriptable base = getBase(obj, index);
/* 2516 */     if (base == null)
/* 2517 */       return true; 
/* 2518 */     base.delete(index);
/* 2519 */     return !base.has(index, obj);
/*      */   }
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
/*      */   public static Object[] getPropertyIds(Scriptable obj) {
/* 2533 */     if (obj == null) {
/* 2534 */       return ScriptRuntime.emptyArgs;
/*      */     }
/* 2536 */     Object[] result = obj.getIds();
/* 2537 */     ObjToIntMap map = null;
/*      */     while (true) {
/* 2539 */       obj = obj.getPrototype();
/* 2540 */       if (obj == null) {
/*      */         break;
/*      */       }
/* 2543 */       Object[] ids = obj.getIds();
/* 2544 */       if (ids.length == 0) {
/*      */         continue;
/*      */       }
/* 2547 */       if (map == null) {
/* 2548 */         if (result.length == 0) {
/* 2549 */           result = ids;
/*      */           continue;
/*      */         } 
/* 2552 */         map = new ObjToIntMap(result.length + ids.length);
/* 2553 */         for (int j = 0; j != result.length; j++) {
/* 2554 */           map.intern(result[j]);
/*      */         }
/* 2556 */         result = null;
/*      */       } 
/* 2558 */       for (int i = 0; i != ids.length; i++) {
/* 2559 */         map.intern(ids[i]);
/*      */       }
/*      */     } 
/* 2562 */     if (map != null) {
/* 2563 */       result = map.getKeys();
/*      */     }
/* 2565 */     return result;
/*      */   }
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
/*      */   public static Object callMethod(Scriptable obj, String methodName, Object[] args) {
/* 2579 */     return callMethod(null, obj, methodName, args);
/*      */   }
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
/*      */   public static Object callMethod(Context cx, Scriptable obj, String methodName, Object[] args) {
/* 2593 */     Object funObj = getProperty(obj, methodName);
/* 2594 */     if (!(funObj instanceof Function)) {
/* 2595 */       throw ScriptRuntime.notFunctionError(obj, methodName);
/*      */     }
/* 2597 */     Function fun = (Function)funObj;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2605 */     Scriptable scope = getTopLevelScope(obj);
/* 2606 */     if (cx != null) {
/* 2607 */       return fun.call(cx, scope, obj, args);
/*      */     }
/* 2609 */     return Context.call(null, fun, scope, obj, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Scriptable getBase(Scriptable obj, String name) {
/* 2616 */     while (!obj.has(name, obj))
/*      */     
/* 2618 */     { obj = obj.getPrototype();
/* 2619 */       if (obj == null)
/* 2620 */         break;  }  return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Scriptable getBase(Scriptable obj, int index) {
/* 2626 */     while (!obj.has(index, obj))
/*      */     
/* 2628 */     { obj = obj.getPrototype();
/* 2629 */       if (obj == null)
/* 2630 */         break;  }  return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getAssociatedValue(Object key) {
/* 2640 */     Map<Object, Object> h = this.associatedValues;
/* 2641 */     if (h == null)
/* 2642 */       return null; 
/* 2643 */     return h.get(key);
/*      */   }
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
/*      */   public static Object getTopScopeValue(Scriptable scope, Object key) {
/* 2659 */     scope = getTopLevelScope(scope);
/*      */     while (true) {
/* 2661 */       if (scope instanceof ScriptableObject) {
/* 2662 */         ScriptableObject so = (ScriptableObject)scope;
/* 2663 */         Object value = so.getAssociatedValue(key);
/* 2664 */         if (value != null) {
/* 2665 */           return value;
/*      */         }
/*      */       } 
/* 2668 */       scope = scope.getPrototype();
/* 2669 */       if (scope == null) {
/* 2670 */         return null;
/*      */       }
/*      */     } 
/*      */   }
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
/*      */   
/*      */   public final synchronized Object associateValue(Object key, Object value) {
/* 2689 */     if (value == null) throw new IllegalArgumentException(); 
/* 2690 */     Map<Object, Object> h = this.associatedValues;
/* 2691 */     if (h == null) {
/* 2692 */       h = new HashMap<Object, Object>();
/* 2693 */       this.associatedValues = h;
/*      */     } 
/* 2695 */     return Kit.initHash(h, key, value);
/*      */   }
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
/*      */   
/*      */   private boolean putImpl(String name, int index, Scriptable start, Object value) {
/*      */     Slot slot;
/* 2713 */     if (this != start) {
/* 2714 */       slot = getSlot(name, index, 1);
/* 2715 */       if (slot == null) {
/* 2716 */         return false;
/*      */       }
/* 2718 */     } else if (!this.isExtensible) {
/* 2719 */       slot = getSlot(name, index, 1);
/* 2720 */       if (slot == null) {
/* 2721 */         return true;
/*      */       }
/*      */     } else {
/* 2724 */       if (this.count < 0) checkNotSealed(name, index); 
/* 2725 */       slot = getSlot(name, index, 2);
/*      */     } 
/* 2727 */     return slot.setValue(value, this, start);
/*      */   }
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
/*      */   
/*      */   private boolean putConstImpl(String name, int index, Scriptable start, Object value, int constFlag) {
/*      */     Slot slot;
/* 2745 */     assert constFlag != 0;
/*      */     
/* 2747 */     if (this != start) {
/* 2748 */       slot = getSlot(name, index, 1);
/* 2749 */       if (slot == null) {
/* 2750 */         return false;
/*      */       }
/* 2752 */     } else if (!isExtensible()) {
/* 2753 */       slot = getSlot(name, index, 1);
/* 2754 */       if (slot == null) {
/* 2755 */         return true;
/*      */       }
/*      */     } else {
/* 2758 */       checkNotSealed(name, index);
/*      */       
/* 2760 */       slot = unwrapSlot(getSlot(name, index, 3));
/* 2761 */       int attr = slot.getAttributes();
/* 2762 */       if ((attr & 0x1) == 0)
/* 2763 */         throw Context.reportRuntimeError1("msg.var.redecl", name); 
/* 2764 */       if ((attr & 0x8) != 0) {
/* 2765 */         slot.value = value;
/*      */         
/* 2767 */         if (constFlag != 8)
/* 2768 */           slot.setAttributes(attr & 0xFFFFFFF7); 
/*      */       } 
/* 2770 */       return true;
/*      */     } 
/* 2772 */     return slot.setValue(value, this, start);
/*      */   }
/*      */ 
/*      */   
/*      */   private Slot findAttributeSlot(String name, int index, int accessType) {
/* 2777 */     Slot slot = getSlot(name, index, accessType);
/* 2778 */     if (slot == null) {
/* 2779 */       String str = (name != null) ? name : Integer.toString(index);
/* 2780 */       throw Context.reportRuntimeError1("msg.prop.not.found", str);
/*      */     } 
/* 2782 */     return slot;
/*      */   }
/*      */   
/*      */   private static Slot unwrapSlot(Slot slot) {
/* 2786 */     return (slot instanceof RelinkedSlot) ? ((RelinkedSlot)slot).slot : slot;
/*      */   }
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
/*      */   private Slot getSlot(String name, int index, int accessType) {
/* 2799 */     Slot[] slotsLocalRef = this.slots;
/* 2800 */     if (slotsLocalRef == null && accessType == 1) {
/* 2801 */       return null;
/*      */     }
/*      */     
/* 2804 */     int indexOrHash = (name != null) ? name.hashCode() : index;
/* 2805 */     if (slotsLocalRef != null) {
/*      */       
/* 2807 */       int slotIndex = getSlotIndex(slotsLocalRef.length, indexOrHash);
/* 2808 */       Slot slot = slotsLocalRef[slotIndex];
/* 2809 */       for (; slot != null; 
/* 2810 */         slot = slot.next) {
/* 2811 */         Object sname = slot.name;
/* 2812 */         if (indexOrHash == slot.indexOrHash && (sname == name || (name != null && name.equals(sname)))) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2818 */       switch (accessType) {
/*      */         case 1:
/* 2820 */           return slot;
/*      */         case 2:
/*      */         case 3:
/* 2823 */           if (slot != null)
/* 2824 */             return slot; 
/*      */           break;
/*      */         case 4:
/* 2827 */           slot = unwrapSlot(slot);
/* 2828 */           if (slot instanceof GetterSlot)
/* 2829 */             return slot; 
/*      */           break;
/*      */         case 5:
/* 2832 */           slot = unwrapSlot(slot);
/* 2833 */           if (!(slot instanceof GetterSlot)) {
/* 2834 */             return slot;
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/* 2841 */     return createSlot(name, indexOrHash, accessType);
/*      */   }
/*      */   private synchronized Slot createSlot(String name, int indexOrHash, int accessType) {
/*      */     int insertPos;
/* 2845 */     Slot[] slotsLocalRef = this.slots;
/*      */     
/* 2847 */     if (this.count == 0) {
/*      */       
/* 2849 */       slotsLocalRef = new Slot[4];
/* 2850 */       this.slots = slotsLocalRef;
/* 2851 */       insertPos = getSlotIndex(slotsLocalRef.length, indexOrHash);
/*      */     } else {
/* 2853 */       int tableSize = slotsLocalRef.length;
/* 2854 */       insertPos = getSlotIndex(tableSize, indexOrHash);
/* 2855 */       Slot prev = slotsLocalRef[insertPos];
/* 2856 */       Slot slot = prev;
/* 2857 */       while (slot != null && (
/* 2858 */         slot.indexOrHash != indexOrHash || (slot.name != name && (name == null || !name.equals(slot.name))))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2864 */         prev = slot;
/* 2865 */         slot = slot.next;
/*      */       } 
/*      */       
/* 2868 */       if (slot != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2875 */         Slot slot1, inner = unwrapSlot(slot);
/*      */ 
/*      */         
/* 2878 */         if (accessType == 4 && !(inner instanceof GetterSlot))
/*      */         
/* 2880 */         { slot1 = new GetterSlot(name, indexOrHash, inner.getAttributes()); }
/* 2881 */         else if (accessType == 5 && inner instanceof GetterSlot)
/*      */         
/* 2883 */         { slot1 = new Slot(name, indexOrHash, inner.getAttributes()); }
/* 2884 */         else { if (accessType == 3) {
/* 2885 */             return null;
/*      */           }
/* 2887 */           return inner; }
/*      */ 
/*      */         
/* 2890 */         slot1.value = inner.value;
/* 2891 */         slot1.next = slot.next;
/*      */         
/* 2893 */         if (this.lastAdded != null) {
/* 2894 */           this.lastAdded.orderedNext = slot1;
/*      */         }
/* 2896 */         if (this.firstAdded == null) {
/* 2897 */           this.firstAdded = slot1;
/*      */         }
/* 2899 */         this.lastAdded = slot1;
/*      */         
/* 2901 */         if (prev == slot) {
/* 2902 */           slotsLocalRef[insertPos] = slot1;
/*      */         } else {
/* 2904 */           prev.next = slot1;
/*      */         } 
/*      */         
/* 2907 */         slot.markDeleted();
/* 2908 */         return slot1;
/*      */       } 
/*      */       
/* 2911 */       if (4 * (this.count + 1) > 3 * slotsLocalRef.length) {
/*      */         
/* 2913 */         slotsLocalRef = new Slot[slotsLocalRef.length * 2];
/* 2914 */         copyTable(this.slots, slotsLocalRef, this.count);
/* 2915 */         this.slots = slotsLocalRef;
/* 2916 */         insertPos = getSlotIndex(slotsLocalRef.length, indexOrHash);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2921 */     Slot newSlot = (accessType == 4) ? new GetterSlot(name, indexOrHash, 0) : new Slot(name, indexOrHash, 0);
/*      */ 
/*      */     
/* 2924 */     if (accessType == 3)
/* 2925 */       newSlot.setAttributes(13); 
/* 2926 */     this.count++;
/*      */     
/* 2928 */     if (this.lastAdded != null)
/* 2929 */       this.lastAdded.orderedNext = newSlot; 
/* 2930 */     if (this.firstAdded == null)
/* 2931 */       this.firstAdded = newSlot; 
/* 2932 */     this.lastAdded = newSlot;
/*      */     
/* 2934 */     addKnownAbsentSlot(slotsLocalRef, newSlot, insertPos);
/* 2935 */     return newSlot;
/*      */   }
/*      */   
/*      */   private synchronized void removeSlot(String name, int index) {
/* 2939 */     int indexOrHash = (name != null) ? name.hashCode() : index;
/*      */     
/* 2941 */     Slot[] slotsLocalRef = this.slots;
/* 2942 */     if (this.count != 0) {
/* 2943 */       int tableSize = slotsLocalRef.length;
/* 2944 */       int slotIndex = getSlotIndex(tableSize, indexOrHash);
/* 2945 */       Slot prev = slotsLocalRef[slotIndex];
/* 2946 */       Slot slot = prev;
/* 2947 */       while (slot != null && (
/* 2948 */         slot.indexOrHash != indexOrHash || (slot.name != name && (name == null || !name.equals(slot.name))))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2954 */         prev = slot;
/* 2955 */         slot = slot.next;
/*      */       } 
/* 2957 */       if (slot != null && (slot.getAttributes() & 0x4) == 0) {
/* 2958 */         this.count--;
/*      */         
/* 2960 */         if (prev == slot) {
/* 2961 */           slotsLocalRef[slotIndex] = slot.next;
/*      */         } else {
/* 2963 */           prev.next = slot.next;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2971 */         Slot deleted = unwrapSlot(slot);
/* 2972 */         if (deleted == this.firstAdded) {
/* 2973 */           prev = null;
/* 2974 */           this.firstAdded = deleted.orderedNext;
/*      */         } else {
/* 2976 */           prev = this.firstAdded;
/* 2977 */           while (prev.orderedNext != deleted) {
/* 2978 */             prev = prev.orderedNext;
/*      */           }
/* 2980 */           prev.orderedNext = deleted.orderedNext;
/*      */         } 
/* 2982 */         if (deleted == this.lastAdded) {
/* 2983 */           this.lastAdded = prev;
/*      */         }
/*      */ 
/*      */         
/* 2987 */         slot.markDeleted();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getSlotIndex(int tableSize, int indexOrHash) {
/* 2995 */     return indexOrHash & tableSize - 1;
/*      */   }
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
/*      */   
/*      */   private static void copyTable(Slot[] oldSlots, Slot[] newSlots, int count) {
/*      */     // Byte code:
/*      */     //   0: iload_2
/*      */     //   1: ifne -> 8
/*      */     //   4: invokestatic codeBug : ()Ljava/lang/RuntimeException;
/*      */     //   7: athrow
/*      */     //   8: aload_1
/*      */     //   9: arraylength
/*      */     //   10: istore_3
/*      */     //   11: aload_0
/*      */     //   12: arraylength
/*      */     //   13: istore #4
/*      */     //   15: iinc #4, -1
/*      */     //   18: aload_0
/*      */     //   19: iload #4
/*      */     //   21: aaload
/*      */     //   22: astore #5
/*      */     //   24: aload #5
/*      */     //   26: ifnull -> 90
/*      */     //   29: iload_3
/*      */     //   30: aload #5
/*      */     //   32: getfield indexOrHash : I
/*      */     //   35: invokestatic getSlotIndex : (II)I
/*      */     //   38: istore #6
/*      */     //   40: aload #5
/*      */     //   42: getfield next : Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   45: ifnonnull -> 53
/*      */     //   48: aload #5
/*      */     //   50: goto -> 62
/*      */     //   53: new org/mozilla/javascript/ScriptableObject$RelinkedSlot
/*      */     //   56: dup
/*      */     //   57: aload #5
/*      */     //   59: invokespecial <init> : (Lorg/mozilla/javascript/ScriptableObject$Slot;)V
/*      */     //   62: astore #7
/*      */     //   64: aload_1
/*      */     //   65: aload #7
/*      */     //   67: iload #6
/*      */     //   69: invokestatic addKnownAbsentSlot : ([Lorg/mozilla/javascript/ScriptableObject$Slot;Lorg/mozilla/javascript/ScriptableObject$Slot;I)V
/*      */     //   72: aload #5
/*      */     //   74: getfield next : Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   77: astore #5
/*      */     //   79: iinc #2, -1
/*      */     //   82: iload_2
/*      */     //   83: ifne -> 87
/*      */     //   86: return
/*      */     //   87: goto -> 24
/*      */     //   90: goto -> 15
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3001	-> 0
/*      */     //   #3003	-> 8
/*      */     //   #3004	-> 11
/*      */     //   #3006	-> 15
/*      */     //   #3007	-> 18
/*      */     //   #3008	-> 24
/*      */     //   #3009	-> 29
/*      */     //   #3012	-> 40
/*      */     //   #3013	-> 64
/*      */     //   #3014	-> 72
/*      */     //   #3015	-> 79
/*      */     //   #3016	-> 86
/*      */     //   #3017	-> 87
/*      */     //   #3018	-> 90
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   40	47	6	insertPos	I
/*      */     //   64	23	7	insSlot	Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   24	66	5	slot	Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   0	93	0	oldSlots	[Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   0	93	1	newSlots	[Lorg/mozilla/javascript/ScriptableObject$Slot;
/*      */     //   0	93	2	count	I
/*      */     //   11	82	3	tableSize	I
/*      */     //   15	78	4	i	I
/*      */   }
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
/*      */   
/*      */   private static void addKnownAbsentSlot(Slot[] slots, Slot slot, int insertPos) {
/* 3029 */     if (slots[insertPos] == null) {
/* 3030 */       slots[insertPos] = slot;
/*      */     } else {
/* 3032 */       Slot prev = slots[insertPos];
/* 3033 */       Slot next = prev.next;
/* 3034 */       while (next != null) {
/* 3035 */         prev = next;
/* 3036 */         next = prev.next;
/*      */       } 
/* 3038 */       prev.next = slot;
/*      */     } 
/*      */   }
/*      */   Object[] getIds(boolean getAll) {
/*      */     Object[] a;
/* 3043 */     Slot[] s = this.slots;
/*      */     
/* 3045 */     int externalLen = (this.externalData == null) ? 0 : this.externalData.getArrayLength();
/*      */     
/* 3047 */     if (externalLen == 0) {
/* 3048 */       a = ScriptRuntime.emptyArgs;
/*      */     } else {
/* 3050 */       a = new Object[externalLen];
/* 3051 */       for (int i = 0; i < externalLen; i++) {
/* 3052 */         a[i] = Integer.valueOf(i);
/*      */       }
/*      */     } 
/* 3055 */     if (s == null) {
/* 3056 */       return a;
/*      */     }
/*      */     
/* 3059 */     int c = externalLen;
/* 3060 */     Slot slot = this.firstAdded;
/* 3061 */     while (slot != null && slot.wasDeleted)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 3066 */       slot = slot.orderedNext;
/*      */     }
/* 3068 */     while (slot != null) {
/* 3069 */       if (getAll || (slot.getAttributes() & 0x2) == 0) {
/* 3070 */         if (c == externalLen) {
/* 3071 */           Object[] oldA = a;
/* 3072 */           a = new Object[s.length + externalLen];
/* 3073 */           if (oldA != null) {
/* 3074 */             System.arraycopy(oldA, 0, a, 0, externalLen);
/*      */           }
/*      */         } 
/* 3077 */         a[c++] = (slot.name != null) ? slot.name : Integer.valueOf(slot.indexOrHash);
/*      */       } 
/*      */ 
/*      */       
/* 3081 */       slot = slot.orderedNext;
/* 3082 */       while (slot != null && slot.wasDeleted)
/*      */       {
/* 3084 */         slot = slot.orderedNext;
/*      */       }
/*      */     } 
/* 3087 */     if (c == a.length + externalLen) {
/* 3088 */       return a;
/*      */     }
/* 3090 */     Object[] result = new Object[c];
/* 3091 */     System.arraycopy(a, 0, result, 0, c);
/* 3092 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void writeObject(ObjectOutputStream out) throws IOException {
/* 3098 */     out.defaultWriteObject();
/* 3099 */     int objectsCount = this.count;
/* 3100 */     if (objectsCount < 0)
/*      */     {
/* 3102 */       objectsCount ^= 0xFFFFFFFF;
/*      */     }
/* 3104 */     if (objectsCount == 0) {
/* 3105 */       out.writeInt(0);
/*      */     } else {
/* 3107 */       out.writeInt(this.slots.length);
/* 3108 */       Slot slot = this.firstAdded;
/* 3109 */       while (slot != null && slot.wasDeleted)
/*      */       {
/*      */         
/* 3112 */         slot = slot.orderedNext;
/*      */       }
/* 3114 */       this.firstAdded = slot;
/* 3115 */       while (slot != null) {
/* 3116 */         out.writeObject(slot);
/* 3117 */         Slot next = slot.orderedNext;
/* 3118 */         while (next != null && next.wasDeleted)
/*      */         {
/* 3120 */           next = next.orderedNext;
/*      */         }
/* 3122 */         slot.orderedNext = next;
/* 3123 */         slot = next;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 3131 */     in.defaultReadObject();
/*      */     
/* 3133 */     int tableSize = in.readInt();
/* 3134 */     if (tableSize != 0) {
/*      */ 
/*      */       
/* 3137 */       if ((tableSize & tableSize - 1) != 0) {
/* 3138 */         if (tableSize > 1073741824)
/* 3139 */           throw new RuntimeException("Property table overflow"); 
/* 3140 */         int newSize = 4;
/* 3141 */         while (newSize < tableSize)
/* 3142 */           newSize <<= 1; 
/* 3143 */         tableSize = newSize;
/*      */       } 
/* 3145 */       this.slots = new Slot[tableSize];
/* 3146 */       int objectsCount = this.count;
/* 3147 */       if (objectsCount < 0)
/*      */       {
/* 3149 */         objectsCount ^= 0xFFFFFFFF;
/*      */       }
/* 3151 */       Slot prev = null;
/* 3152 */       for (int i = 0; i != objectsCount; i++) {
/* 3153 */         this.lastAdded = (Slot)in.readObject();
/* 3154 */         if (i == 0) {
/* 3155 */           this.firstAdded = this.lastAdded;
/*      */         } else {
/* 3157 */           prev.orderedNext = this.lastAdded;
/*      */         } 
/* 3159 */         int slotIndex = getSlotIndex(tableSize, this.lastAdded.indexOrHash);
/* 3160 */         addKnownAbsentSlot(this.slots, this.lastAdded, slotIndex);
/* 3161 */         prev = this.lastAdded;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
/* 3167 */     Slot slot = getSlot(cx, id, 1);
/* 3168 */     if (slot == null) return null; 
/* 3169 */     Scriptable scope = getParentScope();
/* 3170 */     return slot.getPropertyDescriptor(cx, (scope == null) ? this : scope);
/*      */   }
/*      */   
/*      */   protected Slot getSlot(Context cx, Object id, int accessType) {
/* 3174 */     String name = ScriptRuntime.toStringIdOrIndex(cx, id);
/* 3175 */     if (name == null) {
/* 3176 */       return getSlot((String)null, ScriptRuntime.lastIndexResult(cx), accessType);
/*      */     }
/* 3178 */     return getSlot(name, 0, accessType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/* 3186 */     return (this.count < 0) ? (this.count ^ 0xFFFFFFFF) : this.count;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/* 3190 */     return (this.count == 0 || this.count == -1);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object get(Object key) {
/* 3195 */     Object value = null;
/* 3196 */     if (key instanceof String) {
/* 3197 */       value = get((String)key, this);
/* 3198 */     } else if (key instanceof Number) {
/* 3199 */       value = get(((Number)key).intValue(), this);
/*      */     } 
/* 3201 */     if (value == Scriptable.NOT_FOUND || value == Undefined.instance)
/* 3202 */       return null; 
/* 3203 */     if (value instanceof Wrapper) {
/* 3204 */       return ((Wrapper)value).unwrap();
/*      */     }
/* 3206 */     return value;
/*      */   }
/*      */   
/*      */   public ScriptableObject() {}
/*      */   
/*      */   public abstract String getClassName();
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ScriptableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */