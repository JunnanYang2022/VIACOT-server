package com.lineage.newick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A node implementation using the New Hampshire eXtended (NHX) format
 * using Newick comments to encode additional key value pairs, e.g. the node id or scientificName.
 * Supported keys are:
 *
 * <pre>
 * {@code
 * :GN=	string	gene name	<name>(<sequence>)
 * :AC=	string	sequence accession	<accession>(<sequence>)
 * :ND=	string	node identifier - if this is being used, it has to be unique within each phylogeny <node_id>(<clade>)
 * :B=	decimal	confidence value for parent branch <confidence>(<clade>)
 * :D=	'T', 'F', or '?'	'T' if this node represents a duplication event - 'F' if this node represents a speciation event, '?' if this node represents an unknown event (D= tag should be replaced by Ev= tag)	n/a
 * :E=	string	EC number at this node	<annotation>(<sequence>)
 * :Fu=	string	function at this node	<annotation>(<sequence>)
 * :DS=protein-length>from>to>support>name>from>...	int int int double string int ...	domain structure at this node	<domain_architecture>(<sequence>)
 * :S=	string	species name of the species/phylum at this node	<scientific_name> (<taxonomy>)
 * :T=	integer	taxonomy ID of the species/phylum at this node	<id>(<taxonomy>)
 * :W=	integer	width of parent branch	<width>(<clade>)
 * :C=rrr.ggg.bbb	integer.integer.integer	color of parent branch	<color>(<clade>)
 * :Co=	'Y' or 'N'	collapse this node when drawing the tree (default is not to collapse)	n/a
 * :XB=	string	custom data associated with a branch	<property>(<clade>)
 * :XN=	string	custom data associated with a node	<property>(<clade>)
 * :O=	integer	orthologous to this external node	n/a
 * :SN=	integer	subtree neighbors	n/a
 * :SO=	integer	super orthologous (no duplications on paths) to this external node	n/a
 * }</pre>
 *
 * See also http://www.phylosoft.org/NHX/
 */
public class NHXNode extends XNode<NHXNode> {

  private enum KEY {
    S,
    T,
    GN,
    AC,
    ND,
    B,
    D,
    E,
    Fu,
    DS,
    W,
    C,
    Co,
    XB,
    XN,
    O,
    SN,
    SO;
  }

  /**
   * {@code protein-length>from>to>support>name>from>...	int int int double string int ...	domain structure at this node	<domain_architecture>(<sequence>)}
   */
  public static class DomainStructure {
    public final int proteinLength;
    public final List<Domain> domains = new ArrayList<>();

    public DomainStructure(String structure) {
      //TODO: parse structure
      this.proteinLength = -1;
    }

    public DomainStructure(int proteinLength) {
      this.proteinLength = proteinLength;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(proteinLength);
      sb.append(">");
      for (Domain d : domains) {
        sb.append(d);
      }
      return sb.toString();
    }
  }

  public static class Domain {
    public final int from;
    public final int to;
    public final double support;
    public final String name;

    public Domain(int from, int to, double support, String name) {
      this.from = from;
      this.to = to;
      this.support = support;
      this.name = name;
    }

    @Override
    public String toString() {
      return String.format("%d>%d>%f>%s", from, to, support, name);
    }

  }

  /**
   * {@code rrr.ggg.bbb	integer.integer.integer	color of parent branch <color>(<clade>})
   */
  public static class RGB {
    static final Pattern RGB = Pattern.compile("^([0-9]{3})\\.([0-9]{3})\\.([0-9]{3})$");
    public final int red;
    public final int green;
    public final int blue;

    public RGB(String rgb) {
      Matcher m = RGB.matcher(rgb);
      if (m.find()) {
        this.red = Integer.parseInt(m.group(1));
        this.green = Integer.parseInt(m.group(2));
        this.blue = Integer.parseInt(m.group(3));
      } else {
        throw new IllegalArgumentException(rgb + " is no valid RGB value");
      }
    }

    public RGB(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }

    @Override
    public String toString() {
      return String.format("%03d.%03d.%03d", red, green, blue);
    }
  }

  private String geneName;
  private String sequenceAccession;
  private String nodeIdentifier;
  private Float confidence;
  private Character event;
  private String ecNumber;
  private String function;
  private DomainStructure domainStructure;
  private String speciesName;
  private Integer taxonomyID;
  private Integer width;
  private RGB color;
  private boolean collapse;
  private String customBranch;
  private String customNode;
  private Integer orthologous;
  private Integer subtreeNeighbors;
  private Integer superOrthologous;

  public NHXNode() {
  }

  public NHXNode(String label) {
    super(label);
  }

  public NHXNode(String label, Double length) {
    super(label, length);
  }

  public String getGeneName() {
    return geneName;
  }

  public void setGeneName(String geneName) {
    this.geneName = geneName;
  }

  public String getSequenceAccession() {
    return sequenceAccession;
  }

  public void setSequenceAccession(String sequenceAccession) {
    this.sequenceAccession = sequenceAccession;
  }

  public String getNodeIdentifier() {
    return nodeIdentifier;
  }

  public void setNodeIdentifier(String nodeIdentifier) {
    this.nodeIdentifier = nodeIdentifier;
  }

  public Float getConfidence() {
    return confidence;
  }

  public void setConfidence(Float confidence) {
    this.confidence = confidence;
  }

  public Character getEvent() {
    return event;
  }

  public void setEvent(Character event) {
    this.event = event;
  }

  public String getEcNumber() {
    return ecNumber;
  }

  public void setEcNumber(String ecNumber) {
    this.ecNumber = ecNumber;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public DomainStructure getDomainStructure() {
    return domainStructure;
  }

  public void setDomainStructure(DomainStructure domainStructure) {
    this.domainStructure = domainStructure;
  }

  public String getSpeciesName() {
    return speciesName;
  }

  public void setSpeciesName(String speciesName) {
    this.speciesName = speciesName;
  }

  public Integer getTaxonomyID() {
    return taxonomyID;
  }

  public void setTaxonomyID(Integer taxonomyID) {
    this.taxonomyID = taxonomyID;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public RGB getColor() {
    return color;
  }

  public void setColor(RGB color) {
    this.color = color;
  }

  public boolean isCollapse() {
    return collapse;
  }

  public void setCollapse(boolean collapse) {
    this.collapse = collapse;
  }

  public String getCustomBranch() {
    return customBranch;
  }

  public void setCustomBranch(String customBranch) {
    this.customBranch = customBranch;
  }

  public String getCustomNode() {
    return customNode;
  }

  public void setCustomNode(String customNode) {
    this.customNode = customNode;
  }

  public Integer getOrthologous() {
    return orthologous;
  }

  public void setOrthologous(Integer orthologous) {
    this.orthologous = orthologous;
  }

  public Integer getSubtreeNeighbors() {
    return subtreeNeighbors;
  }

  public void setSubtreeNeighbors(Integer subtreeNeighbors) {
    this.subtreeNeighbors = subtreeNeighbors;
  }

  public Integer getSuperOrthologous() {
    return superOrthologous;
  }

  public void setSuperOrthologous(Integer superOrthologous) {
    this.superOrthologous = superOrthologous;
  }

  @Override
  public String getValue(String key) {
    switch (key) {
      case "GN": return geneName;
      case "AC": return sequenceAccession;
      case "ND": return nodeIdentifier;
      case "B": return obj2str(confidence);
      case "D": return obj2str(event);
      case "E": return ecNumber;
      case "Fu": return function;
      case "DS": return obj2str(domainStructure);
      case "S": return speciesName;
      case "T": return obj2str(taxonomyID);
      case "W": return obj2str(width);
      case "C": return obj2str(color);
      case "Co": return collapse ? "Y" : null;
      case "XB": return customBranch;
      case "XN": return customNode;
      case "O": return obj2str(orthologous);
      case "SN": return obj2str(subtreeNeighbors);
      case "SO": return obj2str(superOrthologous);
    }
    return null;
  }

  public void setValue(String key, String value) {

    switch (key) {
      case "GN": geneName = value; break;
      case "AC": sequenceAccession = value; break;
      case "ND": nodeIdentifier = value; break;
      case "B": confidence = value == null ? null : Float.parseFloat(value); break;
      case "D": event = value == null || value.length() != 1 ? null : value.charAt(0); break;
      case "E": ecNumber = value; break;
      case "Fu": function = value; break;
      case "DS": domainStructure = value == null ? null : new DomainStructure(value); break;
      case "S": speciesName = value; break;
      case "T": taxonomyID = value == null ? null : Integer.parseInt(value); break;
      case "W": width = value == null ? null : Integer.parseInt(value); break;
      case "C": color = value == null ? null : new RGB(value); break;
      case "Co": collapse = "Y".equalsIgnoreCase(value); break;
      case "XB": customBranch = value; break;
      case "XN": customNode = value; break;
      case "O": orthologous = value == null ? null : Integer.parseInt(value); break;
      case "SN": subtreeNeighbors = value == null ? null : Integer.parseInt(value); break;
      case "SO": superOrthologous = value == null ? null : Integer.parseInt(value); break;
    }
  }

  private static String obj2str(Object obj){
    return obj == null ? null : obj.toString();
  }

  @Override
  protected List<String> listKeys() {
    return Arrays.stream(KEY.values()).map(Enum::name).collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NHXNode)) return false;
    if (!super.equals(o)) return false;
    NHXNode nhxNode = (NHXNode) o;
    return collapse == nhxNode.collapse
           && Objects.equals(geneName, nhxNode.geneName)
           && Objects.equals(sequenceAccession, nhxNode.sequenceAccession)
           && Objects.equals(nodeIdentifier, nhxNode.nodeIdentifier)
           && Objects.equals(confidence, nhxNode.confidence)
           && event == nhxNode.event
           && Objects.equals(ecNumber, nhxNode.ecNumber)
           && Objects.equals(function, nhxNode.function)
           && Objects.equals(domainStructure, nhxNode.domainStructure)
           && Objects.equals(speciesName, nhxNode.speciesName)
           && Objects.equals(taxonomyID, nhxNode.taxonomyID)
           && Objects.equals(width, nhxNode.width)
           && Objects.equals(color, nhxNode.color)
           && Objects.equals(customBranch, nhxNode.customBranch)
           && Objects.equals(customNode, nhxNode.customNode)
           && Objects.equals(orthologous, nhxNode.orthologous)
           && Objects.equals(subtreeNeighbors, nhxNode.subtreeNeighbors)
           && Objects.equals(superOrthologous, nhxNode.superOrthologous);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), geneName, sequenceAccession, nodeIdentifier, confidence, event, ecNumber, function, domainStructure, speciesName, taxonomyID, width, color, collapse, customBranch, customNode, orthologous, subtreeNeighbors, superOrthologous);
  }
}
