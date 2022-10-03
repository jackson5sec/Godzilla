/*    */ package org.mozilla.javascript.xmlimpl;
/*    */ 
/*    */ import org.mozilla.javascript.NativeWith;
/*    */ import org.mozilla.javascript.Scriptable;
/*    */ import org.mozilla.javascript.xml.XMLObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class XMLWithScope
/*    */   extends NativeWith
/*    */ {
/*    */   private static final long serialVersionUID = -696429282095170887L;
/*    */   private XMLLibImpl lib;
/*    */   private int _currIndex;
/*    */   private XMLList _xmlList;
/*    */   private XMLObject _dqPrototype;
/*    */   
/*    */   XMLWithScope(XMLLibImpl lib, Scriptable parent, XMLObject prototype) {
/* 23 */     super(parent, (Scriptable)prototype);
/* 24 */     this.lib = lib;
/*    */   }
/*    */ 
/*    */   
/*    */   void initAsDotQuery() {
/* 29 */     XMLObject prototype = (XMLObject)getPrototype();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     this._currIndex = 0;
/* 38 */     this._dqPrototype = prototype;
/* 39 */     if (prototype instanceof XMLList) {
/* 40 */       XMLList xl = (XMLList)prototype;
/* 41 */       if (xl.length() > 0) {
/* 42 */         setPrototype((Scriptable)xl.get(0, null));
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 47 */     this._xmlList = this.lib.newXMLList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object updateDotQuery(boolean value) {
/* 55 */     XMLObject seed = this._dqPrototype;
/* 56 */     XMLList xmlL = this._xmlList;
/*    */     
/* 58 */     if (seed instanceof XMLList) {
/*    */ 
/*    */ 
/*    */       
/* 62 */       XMLList orgXmlL = (XMLList)seed;
/*    */       
/* 64 */       int idx = this._currIndex;
/*    */       
/* 66 */       if (value) {
/* 67 */         xmlL.addToList(orgXmlL.get(idx, null));
/*    */       }
/*    */ 
/*    */       
/* 71 */       if (++idx < orgXmlL.length())
/*    */       {
/*    */ 
/*    */         
/* 75 */         this._currIndex = idx;
/* 76 */         setPrototype((Scriptable)orgXmlL.get(idx, null));
/*    */ 
/*    */         
/* 79 */         return null;
/*    */       
/*    */       }
/*    */     
/*    */     }
/* 84 */     else if (value) {
/* 85 */       xmlL.addToList(seed);
/*    */     } 
/*    */ 
/*    */     
/* 89 */     return xmlL;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLWithScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */