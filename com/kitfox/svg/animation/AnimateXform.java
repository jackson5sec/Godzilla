/*    */ package com.kitfox.svg.animation;
/*    */ 
/*    */ import com.kitfox.svg.SVGElement;
/*    */ import com.kitfox.svg.SVGLoaderHelper;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AnimateXform
/*    */   extends AnimateBase
/*    */ {
/*    */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/* 60 */     super.loaderStartElement(helper, attrs, parent);
/*    */   }
/*    */   
/*    */   public abstract AffineTransform eval(AffineTransform paramAffineTransform, double paramDouble);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\AnimateXform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */