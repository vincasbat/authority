

package generated;

import java.util.ArrayList;
import java.util.List;
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
    "priorityClaim"
})
@XmlRootElement(name = "priority-claims")
public class PriorityClaims {

    @XmlElement(name = "priority-claim", required = true)
    protected List<PriorityClaim> priorityClaim;

    /**
     * Gets the value of the priorityClaim property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priorityClaim property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriorityClaim().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PriorityClaim }
     * 
     * 
     */
    public List<PriorityClaim> getPriorityClaim() {
        if (priorityClaim == null) {
            priorityClaim = new ArrayList<PriorityClaim>();
        }
        return this.priorityClaim;
    }

}
