
package it.polito.dp2.NFFG.sol3.client2.models.NffgService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{https://france193.wordpress.com}FLNffg" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{https://france193.wordpress.com}FLPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{https://france193.wordpress.com}FLVResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "flNffg",
    "flPolicy",
    "flvResult"
})
@XmlRootElement(name = "FLNffgs")
public class FLNffgs {

    @XmlElement(name = "FLNffg")
    protected List<FLNffg> flNffg;
    @XmlElement(name = "FLPolicy")
    protected List<FLPolicy> flPolicy;
    @XmlElement(name = "FLVResult")
    protected List<FLVResult> flvResult;

    /**
     * Gets the value of the flNffg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flNffg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFLNffg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FLNffg }
     * 
     * 
     */
    public List<FLNffg> getFLNffg() {
        if (flNffg == null) {
            flNffg = new ArrayList<FLNffg>();
        }
        return this.flNffg;
    }

    /**
     * Gets the value of the flPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFLPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FLPolicy }
     * 
     * 
     */
    public List<FLPolicy> getFLPolicy() {
        if (flPolicy == null) {
            flPolicy = new ArrayList<FLPolicy>();
        }
        return this.flPolicy;
    }

    /**
     * Gets the value of the flvResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flvResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFLVResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FLVResult }
     * 
     * 
     */
    public List<FLVResult> getFLVResult() {
        if (flvResult == null) {
            flvResult = new ArrayList<FLVResult>();
        }
        return this.flvResult;
    }

}
