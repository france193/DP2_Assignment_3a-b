
package it.polito.dp2.NFFG.sol3.client2.models.NffgService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="traversed_node" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{https://france193.wordpress.com}FLVResult" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffg_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isPositive" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="source_node" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="destination_node" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "traversedNode",
    "flvResult"
})
@XmlRootElement(name = "FLPolicy")
public class FLPolicy {

    @XmlElement(name = "traversed_node")
    protected List<String> traversedNode;
    @XmlElement(name = "FLVResult")
    protected FLVResult flvResult;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "nffg_name")
    protected String nffgName;
    @XmlAttribute(name = "isPositive")
    protected Boolean isPositive;
    @XmlAttribute(name = "source_node")
    protected String sourceNode;
    @XmlAttribute(name = "destination_node")
    protected String destinationNode;

    /**
     * Gets the value of the traversedNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traversedNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraversedNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTraversedNode() {
        if (traversedNode == null) {
            traversedNode = new ArrayList<String>();
        }
        return this.traversedNode;
    }

    /**
     * Gets the value of the flvResult property.
     * 
     * @return
     *     possible object is
     *     {@link FLVResult }
     *     
     */
    public FLVResult getFLVResult() {
        return flvResult;
    }

    /**
     * Sets the value of the flvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FLVResult }
     *     
     */
    public void setFLVResult(FLVResult value) {
        this.flvResult = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nffgName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffgName() {
        return nffgName;
    }

    /**
     * Sets the value of the nffgName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffgName(String value) {
        this.nffgName = value;
    }

    /**
     * Gets the value of the isPositive property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPositive() {
        return isPositive;
    }

    /**
     * Sets the value of the isPositive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPositive(Boolean value) {
        this.isPositive = value;
    }

    /**
     * Gets the value of the sourceNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceNode() {
        return sourceNode;
    }

    /**
     * Sets the value of the sourceNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceNode(String value) {
        this.sourceNode = value;
    }

    /**
     * Gets the value of the destinationNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationNode() {
        return destinationNode;
    }

    /**
     * Sets the value of the destinationNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationNode(String value) {
        this.destinationNode = value;
    }

}
