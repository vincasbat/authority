

package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "documentId",
    "pubInd",
    "applicationReference",
    "priorityClaims"
})
@XmlRootElement(name = "authority-file-doc")
public class AuthorityFileDoc {

    @XmlElement(name = "document-id", required = true)
    protected DocumentId documentId;
    @XmlElement(name = "pub-ind", nillable=true, required = true)
    protected String pubInd;
    @XmlElement(name = "application-reference", required = true)
    protected ApplicationReference applicationReference;
    @XmlElement(name = "priority-claims")
    protected PriorityClaims priorityClaims;

    /**
     * Gets the value of the documentId property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentId }
     *     
     */
    public DocumentId getDocumentId() {
        return documentId;
    }

    /**
     * Sets the value of the documentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentId }
     *     
     */
    public void setDocumentId(DocumentId value) {
        this.documentId = value;
    }

    /**
     * Gets the value of the pubInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubInd() {
        return pubInd;
    }

    /**
     * Sets the value of the pubInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubInd(String value) {
        this.pubInd = value;
    }

    /**
     * Gets the value of the applicationReference property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationReference }
     *     
     */
    public ApplicationReference getApplicationReference() {
        return applicationReference;
    }

    /**
     * Sets the value of the applicationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationReference }
     *     
     */
    public void setApplicationReference(ApplicationReference value) {
        this.applicationReference = value;
    }

    /**
     * Gets the value of the priorityClaims property.
     * 
     * @return
     *     possible object is
     *     {@link PriorityClaims }
     *     
     */
    public PriorityClaims getPriorityClaims() {
        return priorityClaims;
    }

    /**
     * Sets the value of the priorityClaims property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriorityClaims }
     *     
     */
    public void setPriorityClaims(PriorityClaims value) {
        this.priorityClaims = value;
    }

}
