package org.richfaces.cdk.xmlconfig.testmodel;

import jakarta.xml.bind.annotation.XmlElement;

public interface Id {
    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the id
     */
    @XmlElement(namespace = Root.HTTP_FOO_BAR_SCHEMA)
    String getId();
}
