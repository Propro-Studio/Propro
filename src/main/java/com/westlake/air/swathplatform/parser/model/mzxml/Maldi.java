package com.westlake.air.swathplatform.parser.model.mzxml;

import com.westlake.air.swathplatform.parser.xml.NonNegativeIntegerAdapter;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;
import java.io.Serializable;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="plateID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="spotID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="laserShootCount" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="laserFrequency" type="{http://www.w3.org/2001/XMLSchema}duration" /&gt;
 *       &lt;attribute name="laserIntensity" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="collisionGas" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Maldi
    implements Serializable, MzXMLObject
{

    private final static long serialVersionUID = 320L;
    @XmlAttribute(required = true)
    protected String plateID;
    @XmlAttribute(required = true)
    protected String spotID;
    @XmlAttribute
    @XmlJavaTypeAdapter(NonNegativeIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Long laserShootCount;
    @XmlAttribute
    protected Duration laserFrequency;
    @XmlAttribute
    @XmlJavaTypeAdapter(NonNegativeIntegerAdapter.class)
    @XmlSchemaType(name = "positiveInteger")
    protected Long laserIntensity;
    @XmlAttribute
    protected Boolean collisionGas;

    /**
     * Gets the value of the plateID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlateID() {
        return plateID;
    }

    /**
     * Sets the value of the plateID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlateID(String value) {
        this.plateID = value;
    }

    /**
     * Gets the value of the spotID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpotID() {
        return spotID;
    }

    /**
     * Sets the value of the spotID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpotID(String value) {
        this.spotID = value;
    }

    /**
     * Gets the value of the laserShootCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getLaserShootCount() {
        return laserShootCount;
    }

    /**
     * Sets the value of the laserShootCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLaserShootCount(Long value) {
        this.laserShootCount = value;
    }

    /**
     * Gets the value of the laserFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getLaserFrequency() {
        return laserFrequency;
    }

    /**
     * Sets the value of the laserFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setLaserFrequency(Duration value) {
        this.laserFrequency = value;
    }

    /**
     * Gets the value of the laserIntensity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getLaserIntensity() {
        return laserIntensity;
    }

    /**
     * Sets the value of the laserIntensity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLaserIntensity(Long value) {
        this.laserIntensity = value;
    }

    /**
     * Gets the value of the collisionGas property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCollisionGas() {
        return collisionGas;
    }

    /**
     * Sets the value of the collisionGas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCollisionGas(Boolean value) {
        this.collisionGas = value;
    }

}
