//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.29 at 07:48:51 PM IST 
//

package com.temenos.messagingLayer.mappingpojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for t24Constant element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="t24Constant">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}T24field"/>
 *           &lt;element ref="{}T24Value"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "t24Field", "t24Value" })
@XmlRootElement(name = "t24Constant")
public class T24Constant {

	@XmlElement(name = "T24field")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String t24Field;
	@XmlElement(name = "T24Value")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String t24Value;

	/**
	 * Gets the value of the t24Field property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getT24Field() {
		return t24Field;
	}

	/**
	 * Sets the value of the t24Field property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setT24Field(String value) {
		this.t24Field = value;
	}

	/**
	 * Gets the value of the t24Value property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getT24Value() {
		return t24Value;
	}

	/**
	 * Sets the value of the t24Value property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setT24Value(String value) {
		this.t24Value = value;
	}

}