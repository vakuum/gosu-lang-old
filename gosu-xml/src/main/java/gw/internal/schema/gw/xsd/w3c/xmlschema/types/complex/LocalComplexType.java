package gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex;

/***************************************************************************/
/* THIS IS AUTOGENERATED CODE - DO NOT MODIFY OR YOUR CHANGES WILL BE LOST */
/* THIS CODE CAN BE REGENERATED USING 'xsd-codegen'                        */
/***************************************************************************/
public class LocalComplexType extends gw.internal.schema.gw.xsd.w3c.xmlschema.types.complex.ComplexType implements gw.internal.xml.IXmlGeneratedClass {

  public static final javax.xml.namespace.QName $ATTRIBUTE_QNAME_Id = new javax.xml.namespace.QName( "", "id", "" );
  public static final javax.xml.namespace.QName $ATTRIBUTE_QNAME_Mixed = new javax.xml.namespace.QName( "", "mixed", "" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_All = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "all", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_Annotation = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "annotation", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_AnyAttribute = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "anyAttribute", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_Attribute = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "attribute", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_AttributeGroup = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "attributeGroup", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_Choice = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "choice", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_ComplexContent = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "complexContent", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_Group = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "group", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_Sequence = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "sequence", "xs" );
  public static final javax.xml.namespace.QName $ELEMENT_QNAME_SimpleContent = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "simpleContent", "xs" );
  public static final javax.xml.namespace.QName $QNAME = new javax.xml.namespace.QName( "http://www.w3.org/2001/XMLSchema", "localComplexType", "xs" );
  public static final gw.util.concurrent.LockingLazyVar<gw.lang.reflect.IType> TYPE = new gw.util.concurrent.LockingLazyVar<gw.lang.reflect.IType>( gw.lang.reflect.TypeSystem.getGlobalLock() ) {
          @Override
          protected gw.lang.reflect.IType init() {
            return gw.lang.reflect.TypeSystem.getByFullName( "gw.xsd.w3c.xmlschema.types.complex.LocalComplexType" );
          }
        };
  private static final gw.util.concurrent.LockingLazyVar<java.lang.Object> SCHEMAINFO = new gw.util.concurrent.LockingLazyVar<java.lang.Object>( gw.lang.reflect.TypeSystem.getGlobalLock() ) {
          @Override
          protected java.lang.Object init() {
            gw.lang.reflect.IType type = TYPE.get();
            return getSchemaInfoByType( type );
          }
        };

  public LocalComplexType() {
    super( TYPE.get(), SCHEMAINFO.get() );
  }

  protected LocalComplexType( gw.lang.reflect.IType type, java.lang.Object schemaInfo ) {
    super( type, schemaInfo );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.All All() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.All) TYPE.get().getTypeInfo().getProperty( "All" ).getAccessor().getValue( this );
  }

  public void setAll$( gw.internal.schema.gw.xsd.w3c.xmlschema.All param ) {
    TYPE.get().getTypeInfo().getProperty( "All" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.Annotation Annotation() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.Annotation) TYPE.get().getTypeInfo().getProperty( "Annotation" ).getAccessor().getValue( this );
  }

  public void setAnnotation$( gw.internal.schema.gw.xsd.w3c.xmlschema.Annotation param ) {
    TYPE.get().getTypeInfo().getProperty( "Annotation" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.AnyAttribute AnyAttribute() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.AnyAttribute) TYPE.get().getTypeInfo().getProperty( "AnyAttribute" ).getAccessor().getValue( this );
  }

  public void setAnyAttribute$( gw.internal.schema.gw.xsd.w3c.xmlschema.AnyAttribute param ) {
    TYPE.get().getTypeInfo().getProperty( "AnyAttribute" ).getAccessor().setValue( this, param );
  }


  @Deprecated
  public java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_Attribute> Attribute() {
    return super.Attribute();
  }

  @Deprecated
  public void setAttribute$( java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_Attribute> param ) {
    super.setAttribute$( param );
  }

  public java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Attribute> Attribute$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType() {
    //noinspection unchecked
    return (java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Attribute>) TYPE.get().getTypeInfo().getProperty( "Attribute" ).getAccessor().getValue( this );
  }

  public void setAttribute$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType$( java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Attribute> param ) {
    TYPE.get().getTypeInfo().getProperty( "Attribute" ).getAccessor().setValue( this, param );
  }


  @Deprecated
  public java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_AttributeGroup> AttributeGroup() {
    return super.AttributeGroup();
  }

  @Deprecated
  public void setAttributeGroup$( java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_AttributeGroup> param ) {
    super.setAttributeGroup$( param );
  }

  public java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_AttributeGroup> AttributeGroup$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType() {
    //noinspection unchecked
    return (java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_AttributeGroup>) TYPE.get().getTypeInfo().getProperty( "AttributeGroup" ).getAccessor().getValue( this );
  }

  public void setAttributeGroup$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType$( java.util.List<gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_AttributeGroup> param ) {
    TYPE.get().getTypeInfo().getProperty( "AttributeGroup" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.Choice Choice() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.Choice) TYPE.get().getTypeInfo().getProperty( "Choice" ).getAccessor().getValue( this );
  }

  public void setChoice$( gw.internal.schema.gw.xsd.w3c.xmlschema.Choice param ) {
    TYPE.get().getTypeInfo().getProperty( "Choice" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexContent ComplexContent() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexContent) TYPE.get().getTypeInfo().getProperty( "ComplexContent" ).getAccessor().getValue( this );
  }

  public void setComplexContent$( gw.internal.schema.gw.xsd.w3c.xmlschema.ComplexContent param ) {
    TYPE.get().getTypeInfo().getProperty( "ComplexContent" ).getAccessor().setValue( this, param );
  }


  @Deprecated
  public gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_Group Group() {
    return super.Group();
  }

  @Deprecated
  public void setGroup$( gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.ComplexType_Group param ) {
    super.setGroup$( param );
  }

  public gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Group Group$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Group) TYPE.get().getTypeInfo().getProperty( "Group" ).getAccessor().getValue( this );
  }

  public void setGroup$$gw_xsd_w3c_xmlschema_types_complex_LocalComplexType$( gw.internal.schema.gw.xsd.w3c.xmlschema.anonymous.elements.LocalComplexType_Group param ) {
    TYPE.get().getTypeInfo().getProperty( "Group" ).getAccessor().setValue( this, param );
  }


  public java.lang.String Id() {
    return (java.lang.String) TYPE.get().getTypeInfo().getProperty( "Id" ).getAccessor().getValue( this );
  }

  public void setId$( java.lang.String param ) {
    TYPE.get().getTypeInfo().getProperty( "Id" ).getAccessor().setValue( this, param );
  }


  public java.lang.Boolean Mixed() {
    return (java.lang.Boolean) TYPE.get().getTypeInfo().getProperty( "Mixed" ).getAccessor().getValue( this );
  }

  public void setMixed$( java.lang.Boolean param ) {
    TYPE.get().getTypeInfo().getProperty( "Mixed" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence Sequence() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence) TYPE.get().getTypeInfo().getProperty( "Sequence" ).getAccessor().getValue( this );
  }

  public void setSequence$( gw.internal.schema.gw.xsd.w3c.xmlschema.Sequence param ) {
    TYPE.get().getTypeInfo().getProperty( "Sequence" ).getAccessor().setValue( this, param );
  }


  public gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleContent SimpleContent() {
    return (gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleContent) TYPE.get().getTypeInfo().getProperty( "SimpleContent" ).getAccessor().getValue( this );
  }

  public void setSimpleContent$( gw.internal.schema.gw.xsd.w3c.xmlschema.SimpleContent param ) {
    TYPE.get().getTypeInfo().getProperty( "SimpleContent" ).getAccessor().setValue( this, param );
  }

  @SuppressWarnings( {"UnusedDeclaration"} )
  private static final long FINGERPRINT = -3788403261967307401L;

}
