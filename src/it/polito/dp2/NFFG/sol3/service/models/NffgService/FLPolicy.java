//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.17 at 08:21:30 PM CET 
//


package it.polito.dp2.NFFG.sol3.service.models.NffgService;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="FLV_result" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="policy" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="message" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="FLTraversal_requested_node" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nffg" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    protected FLPolicy.FLVResult flvResult;
    @XmlElement(name = "FLTraversal_requested_node")
    protected List<FLPolicy.FLTraversalRequestedNode> flTraversalRequestedNode;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "nffg", required = true)
    protected String nffg;
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
     *     {@link FLPolicy.FLVResult }
     *     
     */
    public FLPolicy.FLVResult getFLVResult() {
        return flvResult;
    }

    /**
     * Sets the value of the flvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FLPolicy.FLVResult }
     *     
     */
    public void setFLVResult(FLPolicy.FLVResult value) {
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
     * Gets the value of the nffg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNffg() {
        return nffg;
    }

    /**
     * Sets the value of the nffg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNffg(String value) {
        this.nffg = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="policy" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="message" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FLVResult {

        @XmlAttribute(name = "policy")
        protected String policy;
        @XmlAttribute(name = "result")
        protected Boolean result;
        @XmlAttribute(name = "time")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar time;
        @XmlAttribute(name = "message")
        protected String message;

        /**
         * Gets the value of the policy property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPolicy() {
            return policy;
        }

        /**
         * Sets the value of the policy property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPolicy(String value) {
            this.policy = value;
        }

        /**
         * Gets the value of the result property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isResult() {
            return result;
        }

        /**
         * Sets the value of the result property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setResult(Boolean value) {
            this.result = value;
        }

        /**
         * Gets the value of the time property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getTime() {
            return time;
        }

        /**
         * Sets the value of the time property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setTime(XMLGregorianCalendar value) {
            this.time = value;
        }

        /**
         * Gets the value of the message property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the value of the message property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessage(String value) {
            this.message = value;
        }

    }

}
