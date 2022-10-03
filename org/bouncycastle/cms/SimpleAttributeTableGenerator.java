package org.bouncycastle.cms;

import java.util.Map;
import org.bouncycastle.asn1.cms.AttributeTable;

public class SimpleAttributeTableGenerator implements CMSAttributeTableGenerator {
  private final AttributeTable attributes;
  
  public SimpleAttributeTableGenerator(AttributeTable paramAttributeTable) {
    this.attributes = paramAttributeTable;
  }
  
  public AttributeTable getAttributes(Map paramMap) {
    return this.attributes;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\SimpleAttributeTableGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */