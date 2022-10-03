/*     */ package org.fife.rsta.ac.js.ast.type.ecma.client;
/*     */ 
/*     */ import org.fife.rsta.ac.js.ast.type.ECMAAdditions;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HTMLDOMAdditions
/*     */   implements ECMAAdditions
/*     */ {
/*     */   public void addAdditionalTypes(TypeDeclarations typeDecs) {
/*  14 */     typeDecs.addTypeDeclaration("HTMLAnchorElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLAnchorElement", "HTMLAnchorElement", false, false));
/*  15 */     typeDecs.addTypeDeclaration("HTMLAppletElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLAppletElement", "HTMLAppletElement", false, false));
/*  16 */     typeDecs.addTypeDeclaration("HTMLAreaElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLAreaElement", "HTMLAreaElement", false, false));
/*  17 */     typeDecs.addTypeDeclaration("HTMLBaseElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLBaseElement", "HTMLBaseElement", false, false));
/*  18 */     typeDecs.addTypeDeclaration("HTMLBaseFontElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLBaseFontElement", "HTMLBaseFontElement", false, false));
/*  19 */     typeDecs.addTypeDeclaration("HTMLBodyElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLBodyElement", "HTMLBodyElement", false, false));
/*  20 */     typeDecs.addTypeDeclaration("HTMLBRElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLBRElement", "HTMLBRElement", false, false));
/*  21 */     typeDecs.addTypeDeclaration("HTMLButtonElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLButtonElement", "HTMLButtonElement", false, false));
/*  22 */     typeDecs.addTypeDeclaration("HTMLCollection", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLCollection", "HTMLCollection", false, false));
/*  23 */     typeDecs.addTypeDeclaration("HTMLDirectoryElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLDirectoryElement", "HTMLDirectoryElement", false, false));
/*  24 */     typeDecs.addTypeDeclaration("HTMLDivElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLDivElement", "HTMLDivElement", false, false));
/*  25 */     typeDecs.addTypeDeclaration("HTMLDListElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLDListElement", "HTMLDListElement", false, false));
/*  26 */     typeDecs.addTypeDeclaration("HTMLDocument", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLDocument", "HTMLDocument", false, false));
/*  27 */     typeDecs.addTypeDeclaration("HTMLElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLElement", "HTMLElement", false, false));
/*  28 */     typeDecs.addTypeDeclaration("HTMLFieldSetElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLFieldSetElement", "HTMLFieldSetElement", false, false));
/*  29 */     typeDecs.addTypeDeclaration("HTMLFontElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLFontElement", "HTMLFontElement", false, false));
/*  30 */     typeDecs.addTypeDeclaration("HTMLFormElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLFormElement", "HTMLFormElement", false, false));
/*  31 */     typeDecs.addTypeDeclaration("HTMLFrameElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLFrameElement", "HTMLFrameElement", false, false));
/*  32 */     typeDecs.addTypeDeclaration("HTMLFrameSetElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLFrameSetElement", "HTMLFrameSetElement", false, false));
/*  33 */     typeDecs.addTypeDeclaration("HTMLHeadElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLHeadElement", "HTMLHeadElement", false, false));
/*  34 */     typeDecs.addTypeDeclaration("HTMLHeadingElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLHeadingElement", "HTMLHeadingElement", false, false));
/*  35 */     typeDecs.addTypeDeclaration("HTMLHRElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLHRElement", "HTMLHRElement", false, false));
/*  36 */     typeDecs.addTypeDeclaration("HTMLHtmlElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLHtmlElement", "HTMLHtmlElement", false, false));
/*  37 */     typeDecs.addTypeDeclaration("HTMLIFrameElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLIFrameElement", "HTMLIFrameElement", false, false));
/*  38 */     typeDecs.addTypeDeclaration("HTMLImageElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLImageElement", "HTMLImageElement", false, false));
/*  39 */     typeDecs.addTypeDeclaration("HTMLInputElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLInputElement", "HTMLInputElement", false, false));
/*  40 */     typeDecs.addTypeDeclaration("HTMLIsIndexElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLIsIndexElement", "HTMLIsIndexElement", false, false));
/*  41 */     typeDecs.addTypeDeclaration("HTMLLabelElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLLabelElement", "HTMLLabelElement", false, false));
/*  42 */     typeDecs.addTypeDeclaration("HTMLLegendElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLLegendElement", "HTMLLegendElement", false, false));
/*  43 */     typeDecs.addTypeDeclaration("HTMLLIElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLLIElement", "HTMLLIElement", false, false));
/*  44 */     typeDecs.addTypeDeclaration("HTMLLinkElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLLinkElement", "HTMLLinkElement", false, false));
/*  45 */     typeDecs.addTypeDeclaration("HTMLMapElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLMapElement", "HTMLMapElement", false, false));
/*  46 */     typeDecs.addTypeDeclaration("HTMLMenuElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLMenuElement", "HTMLMenuElement", false, false));
/*  47 */     typeDecs.addTypeDeclaration("HTMLMetaElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLMetaElement", "HTMLMetaElement", false, false));
/*  48 */     typeDecs.addTypeDeclaration("HTMLModElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLModElement", "HTMLModElement", false, false));
/*  49 */     typeDecs.addTypeDeclaration("HTMLObjectElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLObjectElement", "HTMLObjectElement", false, false));
/*  50 */     typeDecs.addTypeDeclaration("HTMLOListElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLOListElement", "HTMLOListElement", false, false));
/*  51 */     typeDecs.addTypeDeclaration("HTMLOptGroupElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLOptGroupElement", "HTMLOptGroupElement", false, false));
/*  52 */     typeDecs.addTypeDeclaration("HTMLOptionElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLOptionElement", "HTMLOptionElement", false, false));
/*  53 */     typeDecs.addTypeDeclaration("HTMLOptionsCollection", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLOptionsCollection", "HTMLOptionsCollection", false, false));
/*  54 */     typeDecs.addTypeDeclaration("HTMLParagraphElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLParagraphElement", "HTMLParagraphElement", false, false));
/*  55 */     typeDecs.addTypeDeclaration("HTMLParamElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLParamElement", "JSHTMLParamElement", false, false));
/*  56 */     typeDecs.addTypeDeclaration("HTMLPreElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLPreElement", "HTMLPreElement", false, false));
/*  57 */     typeDecs.addTypeDeclaration("HTMLQuoteElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLQuoteElement", "HTMLQuoteElement", false, false));
/*  58 */     typeDecs.addTypeDeclaration("HTMLScriptElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLScriptElement", "HTMLScriptElement", false, false));
/*  59 */     typeDecs.addTypeDeclaration("HTMLSelectElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLSelectElement", "HTMLSelectElement", false, false));
/*  60 */     typeDecs.addTypeDeclaration("HTMLStyleElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLStyleElement", "HTMLStyleElement", false, false));
/*  61 */     typeDecs.addTypeDeclaration("HTMLTableCaptionElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableCaptionElement", "HTMLTableCaptionElement", false, false));
/*  62 */     typeDecs.addTypeDeclaration("HTMLTableCellElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableCellElement", "JSHTMLTableCellElement", false, false));
/*  63 */     typeDecs.addTypeDeclaration("HTMLTableColElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableColElement", "HTMLTableColElement", false, false));
/*  64 */     typeDecs.addTypeDeclaration("HTMLTableElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableElement", "HTMLTableElement", false, false));
/*  65 */     typeDecs.addTypeDeclaration("HTMLTableRowElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableRowElement", "HTMLTableRowElement", false, false));
/*  66 */     typeDecs.addTypeDeclaration("HTMLTableSectionElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTableSectionElement", "HTMLTableSectionElement", false, false));
/*  67 */     typeDecs.addTypeDeclaration("HTMLTextAreaElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTextAreaElement", "HTMLTextAreaElement", false, false));
/*  68 */     typeDecs.addTypeDeclaration("HTMLTitleElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLTitleElement", "HTMLTitleElement", false, false));
/*  69 */     typeDecs.addTypeDeclaration("HTMLUListElement", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom.html", "JSHTMLUListElement", "HTMLUListElement", false, false));
/*     */ 
/*     */ 
/*     */     
/*  73 */     typeDecs.addECMAObject("HTMLAnchorElement", true);
/*  74 */     typeDecs.addECMAObject("HTMLAppletElement", true);
/*  75 */     typeDecs.addECMAObject("HTMLAreaElement", true);
/*  76 */     typeDecs.addECMAObject("HTMLBaseElement", true);
/*  77 */     typeDecs.addECMAObject("HTMLBaseFontElement", true);
/*  78 */     typeDecs.addECMAObject("HTMLBodyElement", true);
/*  79 */     typeDecs.addECMAObject("HTMLBRElement", true);
/*  80 */     typeDecs.addECMAObject("HTMLButtonElement", true);
/*  81 */     typeDecs.addECMAObject("HTMLCollection", true);
/*  82 */     typeDecs.addECMAObject("HTMLDirectoryElement", true);
/*  83 */     typeDecs.addECMAObject("HTMLDivElement", true);
/*  84 */     typeDecs.addECMAObject("HTMLDListElement", true);
/*  85 */     typeDecs.addECMAObject("HTMLDocument", true);
/*  86 */     typeDecs.addECMAObject("HTMLElement", true);
/*  87 */     typeDecs.addECMAObject("HTMLFieldSetElement", true);
/*  88 */     typeDecs.addECMAObject("HTMLFontElement", true);
/*  89 */     typeDecs.addECMAObject("HTMLFormElement", true);
/*  90 */     typeDecs.addECMAObject("HTMLFrameElement", true);
/*  91 */     typeDecs.addECMAObject("HTMLFrameSetElement", true);
/*  92 */     typeDecs.addECMAObject("HTMLHeadElement", true);
/*  93 */     typeDecs.addECMAObject("HTMLHeadingElement", true);
/*  94 */     typeDecs.addECMAObject("HTMLHRElement", true);
/*  95 */     typeDecs.addECMAObject("HTMLHtmlElement", true);
/*  96 */     typeDecs.addECMAObject("HTMLIFrameElement", true);
/*  97 */     typeDecs.addECMAObject("HTMLImageElement", true);
/*  98 */     typeDecs.addECMAObject("HTMLInputElement", true);
/*  99 */     typeDecs.addECMAObject("HTMLIsIndexElement", true);
/* 100 */     typeDecs.addECMAObject("HTMLLabelElement", true);
/* 101 */     typeDecs.addECMAObject("HTMLLegendElement", true);
/* 102 */     typeDecs.addECMAObject("HTMLLIElement", true);
/* 103 */     typeDecs.addECMAObject("HTMLLinkElement", true);
/* 104 */     typeDecs.addECMAObject("HTMLMapElement", true);
/* 105 */     typeDecs.addECMAObject("HTMLMenuElement", true);
/* 106 */     typeDecs.addECMAObject("HTMLMetaElement", true);
/* 107 */     typeDecs.addECMAObject("HTMLModElement", true);
/* 108 */     typeDecs.addECMAObject("HTMLObjectElement", true);
/* 109 */     typeDecs.addECMAObject("HTMLOListElement", true);
/* 110 */     typeDecs.addECMAObject("HTMLOptGroupElement", true);
/* 111 */     typeDecs.addECMAObject("HTMLOptionElement", true);
/* 112 */     typeDecs.addECMAObject("HTMLOptionsCollection", true);
/* 113 */     typeDecs.addECMAObject("HTMLParagraphElement", true);
/* 114 */     typeDecs.addECMAObject("HTMLParamElement", true);
/* 115 */     typeDecs.addECMAObject("HTMLPreElement", true);
/* 116 */     typeDecs.addECMAObject("HTMLQuoteElement", true);
/* 117 */     typeDecs.addECMAObject("HTMLScriptElement", true);
/* 118 */     typeDecs.addECMAObject("HTMLSelectElement", true);
/* 119 */     typeDecs.addECMAObject("HTMLStyleElement", true);
/* 120 */     typeDecs.addECMAObject("HTMLTableCaptionElement", true);
/* 121 */     typeDecs.addECMAObject("HTMLTableCellElement", true);
/* 122 */     typeDecs.addECMAObject("HTMLTableColElement", true);
/* 123 */     typeDecs.addECMAObject("HTMLTableElement", true);
/* 124 */     typeDecs.addECMAObject("HTMLTableRowElement", true);
/* 125 */     typeDecs.addECMAObject("HTMLTableSectionElement", true);
/* 126 */     typeDecs.addECMAObject("HTMLTextAreaElement", true);
/* 127 */     typeDecs.addECMAObject("HTMLTitleElement", true);
/* 128 */     typeDecs.addECMAObject("HTMLUListElement", true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\client\HTMLDOMAdditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */