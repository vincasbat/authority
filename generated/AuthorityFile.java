


package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "authorityFileDoc"
})
@XmlRootElement(name = "authority-file")
public class AuthorityFile {

    @XmlAttribute(name = "country", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String country;
    @XmlAttribute(name = "date-produced", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String dateProduced;
    @XmlElement(name = "authority-file-doc", required = true)
    protected List<AuthorityFileDoc> authorityFileDoc;

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the dateProduced property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateProduced() {
        return dateProduced;
    }

    /**
     * Sets the value of the dateProduced property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateProduced(String value) {
        this.dateProduced = value;
    }

    /**
     * Gets the value of the authorityFileDoc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorityFileDoc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorityFileDoc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthorityFileDoc }
     * 
     * 
     */
    public List<AuthorityFileDoc> getAuthorityFileDoc() {
        if (authorityFileDoc == null) {
            authorityFileDoc = new ArrayList<AuthorityFileDoc>();
        }
        return this.authorityFileDoc;
    }

}
