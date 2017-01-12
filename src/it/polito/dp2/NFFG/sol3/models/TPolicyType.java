//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.12 at 04:52:25 PM CET 
//


package it.polito.dp2.NFFG.sol3.models;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_policyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_policyType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://france193.wordpress.com}policyType">
 *       &lt;sequence>
 *         &lt;element name="traversal_requested_node" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="functional_type" use="required" type="{https://france193.wordpress.com}nodeFunctionalType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_policyType", propOrder = {
    "traversalRequestedNode"
})
public class TPolicyType
    extends PolicyType
{

    @XmlElement(name = "traversal_requested_node")
    protected List<TPolicyType.TraversalRequestedNode> traversalRequestedNode;

    /**
     * Gets the value of the traversalRequestedNode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traversalRequestedNode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraversalRequestedNode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TPolicyType.TraversalRequestedNode }
     * 
     * 
     */
    public List<TPolicyType.TraversalRequestedNode> getTraversalRequestedNode() {
        if (traversalRequestedNode == null) {
            traversalRequestedNode = new ArrayList<TPolicyType.TraversalRequestedNode>();
        }
        return this.traversalRequestedNode;
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
    public static class TraversalRequestedNode {

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
