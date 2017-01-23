
package it.polito.dp2.NFFG.sol3.client1.models.NffgService;

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
 *         &lt;element ref="{https://france193.wordpress.com}FLV_result" minOccurs="0"/>
 *         &lt;element name="FLTraversal_requested_node" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                 &lt;/sequence>
 *                 &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffgName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isPositive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="source_node" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="destination_node" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "flvResult",
    "flTraversalRequestedNode"
})
@XmlRootElement(name = "FLPolicy")
public class FLPolicy {

    @XmlElement(name = "FLV_result")
    protected FLVResult flvResult;
    @XmlElement(name = "FLTraversal_requested_node")
    protected List<FLPolicy.FLTraversalRequestedNode> flTraversalRequestedNode;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "nffgName", required = true)
    protected String nffgName;
    @XmlAttribute(name = "isPositive", required = true)
    protected boolean isPositive;
    @XmlAttribute(name = "source_node", required = true)
    protected String sourceNode;
    @XmlAttribute(name = "destination_node", required = true)
    protected String destinationNode;

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
     * Gets the value of the flTraversalRequestedNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the flTraversalRequestedNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFLTraversalRequestedNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FLPolicy.FLTraversalRequestedNode }
     * 
     * 
     */
    public List<FLPolicy.FLTraversalRequestedNode> getFLTraversalRequestedNode() {
        if (flTraversalRequestedNode == null) {
            flTraversalRequestedNode = new ArrayList<FLPolicy.FLTraversalRequestedNode>();
        }
        return this.flTraversalRequestedNode;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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
     */
    public boolean isIsPositive() {
        return isPositive;
    }

    /**
     * Sets the value of the isPositive property.
     * 
     */
    public void setIsPositive(boolean value) {
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
     *       &lt;/sequence>
     *       &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FLTraversalRequestedNode {

        @XmlAttribute(name = "functional_type", required = true)
        protected NodeFunctionalType functionalType;

        /**
         * Gets the value of the functionalType property.
         * 
         * @return
         *     possible object is
         *     {@link NodeFunctionalType }
         *     
         */
        public NodeFunctionalType getFunctionalType() {
            return functionalType;
        }

        /**
         * Sets the value of the functionalType property.
         * 
         * @param value
         *     allowed object is
         *     {@link NodeFunctionalType }
         *     
         */
        public void setFunctionalType(NodeFunctionalType value) {
            this.functionalType = value;
        }

    }

}
