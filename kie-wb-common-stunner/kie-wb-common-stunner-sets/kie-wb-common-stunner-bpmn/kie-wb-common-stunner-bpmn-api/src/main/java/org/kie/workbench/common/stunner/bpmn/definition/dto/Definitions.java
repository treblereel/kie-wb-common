/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.definition.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl;
import org.kie.workbench.common.stunner.bpmn.definition.Relationship;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.DiagramSet;
import org.treblereel.gwt.jackson.api.annotation.TargetNamespace;
import org.treblereel.gwt.jackson.api.annotation.XMLMapper;
import org.treblereel.gwt.jackson.api.annotation.XmlUnwrappedCollection;

@XMLMapper
@XmlType(propOrder = {"import", "itemDefinition", "process", "BPMNDiagram"})
@XmlRootElement(name = "definitions", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
@TargetNamespace(prefix = "bpmn2", namespace = "http://www.omg.org/bpmn20")
public class Definitions {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String exporter = "jBPM Process Modeler";

    @XmlAttribute
    private String exporterVersion = "2.0";

    @XmlUnwrappedCollection
    @XmlElement(name = "import")
    private List<Import> imports = new ArrayList<>();

    private DiagramSet process;

    @XmlUnwrappedCollection
    @XmlElement(name = "itemDefinition")
    private List<ItemDefinition> itemDefinitions = new ArrayList<>();
    //TODO
    @XmlElement(name = "BPMNDiagram")
    private BPMNDiagramImpl bpmnDiagram;
    private Relationship relationship;

    public BPMNDiagramImpl getBpmnDiagram() {
        return bpmnDiagram;
    }

    public void setBpmnDiagram(BPMNDiagramImpl bpmnDiagram) {
        this.bpmnDiagram = bpmnDiagram;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExporter() {
        return exporter;
    }

    public void setExporter(String exporter) {
        this.exporter = exporter;
    }

    public String getExporterVersion() {
        return exporterVersion;
    }

    public void setExporterVersion(String exporterVersion) {
        this.exporterVersion = exporterVersion;
    }

    public DiagramSet getProcess() {
        return process;
    }

    public void setProcess(DiagramSet process) {
        this.process = process;
    }

    public List<ItemDefinition> getItemDefinitions() {
        return itemDefinitions;
    }

    public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public List<Import> getImports() {
        return imports;
    }

    public void setImports(List<Import> imports) {
        this.imports = imports;
    }

    @Override
    public String toString() {
        return "Definitions{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", exporter='" + exporter + '\'' +
                ", exporterVersion='" + exporterVersion + '\'' +
                ", imports=" + imports +
                ", process=" + process +
                ", itemDefinitions=" + itemDefinitions +
                ", bpmnDiagram=" + bpmnDiagram +
                ", relationship=" + relationship +
                '}';
    }
}
