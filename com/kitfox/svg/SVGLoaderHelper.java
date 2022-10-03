/*    */ package com.kitfox.svg;
/*    */ 
/*    */ import com.kitfox.svg.animation.parser.AnimTimeParser;
/*    */ import java.io.StringReader;
/*    */ import java.net.URI;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SVGLoaderHelper
/*    */ {
/*    */   public final SVGUniverse universe;
/*    */   public final SVGDiagram diagram;
/*    */   public final URI xmlBase;
/* 65 */   public final AnimTimeParser animTimeParser = new AnimTimeParser(new StringReader(""));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SVGLoaderHelper(URI xmlBase, SVGUniverse universe, SVGDiagram diagram) {
/* 75 */     this.xmlBase = xmlBase;
/* 76 */     this.universe = universe;
/* 77 */     this.diagram = diagram;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGLoaderHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */