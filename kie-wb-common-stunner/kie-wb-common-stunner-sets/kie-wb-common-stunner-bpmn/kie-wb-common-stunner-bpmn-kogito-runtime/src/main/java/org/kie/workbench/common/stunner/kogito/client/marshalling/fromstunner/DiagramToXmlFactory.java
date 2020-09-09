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

package org.kie.workbench.common.stunner.kogito.client.marshalling.fromstunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNViewDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.Relationship;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions_MapperImpl;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpmndi.BPMNPlane;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpmndi.BPMNPlaneElement;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpmndi.BPMNShape;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.Availability;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.BPSimData;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.CostParameters;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.ElementParameters;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.FloatingParameter;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.NormalDistribution;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.ProcessingTime;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.Quantity;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.ResourceParameters;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.Scenario;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.TimeParameters;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim.UnitCost;
import org.kie.workbench.common.stunner.bpmn.definition.dto.converter.from.UserTaskConverter;
import org.kie.workbench.common.stunner.bpmn.definition.dto.dc.Bounds;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl;
import org.kie.workbench.common.stunner.core.graph.impl.NodeImpl;
import org.treblereel.gwt.jackson.api.DefaultXMLSerializationContext;
import org.treblereel.gwt.jackson.api.XMLSerializationContext;

public class DiagramToXmlFactory {

    public static String toXml(Diagram diagram) {
        String definitionsId = diagram.getMetadata().getCanvasRootUUID();

        Definitions definitions = new Definitions();
        definitions.setId(definitionsId);

        List<BPMNViewDefinition> definitionList = new LinkedList<>();
        definitions.setItemDefinitions(new LinkedList<>());

        definitions.setRelationship(new Relationship());
        definitions.getRelationship().setSource(definitions.getId());
        definitions.getRelationship().setTarget(definitions.getId());
        definitions.getRelationship().setExtensionElements(new ArrayList<>());

        List<ElementParameters> elementParameters = new ArrayList<>();
        Scenario scenario = new Scenario(elementParameters);
        definitions.getRelationship().getExtensionElements().add(new BPSimData(scenario));


        GWT.log("1 " + diagram.getMetadata().getDefinitionSetId());
        GWT.log("2 " + diagram.getMetadata().getPath().getFileName());
        GWT.log("3 " + diagram.getMetadata().getPath().toURI());
        GWT.log("4 " + diagram.getMetadata().getTitle());
        GWT.log("5 " + diagram.getMetadata().getCanvasRootUUID());
        GWT.log("6 " + diagram.getMetadata().getShapeSetId());
        GWT.log("7 " + diagram.getMetadata().getProfileId());
        GWT.log("8 " + diagram.getMetadata().getRoot());


        GWT.log("**********************************************************************");


        GWT.log(" getContent + " + diagram.getGraph().getContent() + " " + diagram.getGraph().getContent().getClass().getCanonicalName());

        //org.kie.workbench.common.stunner.core.graph.content.definition.DefinitionSetImpl definitionSetImpl = (org.kie.workbench.common.stunner.core.graph.content.definition.DefinitionSetImpl) diagram.getGraph().getContent();

        BPMNPlane plane = new BPMNPlane();
        plane.setBpmnElement(diagram.getMetadata().getTitle());
        List<BPMNPlaneElement> elements = new ArrayList<>();
        plane.setElements(elements);

        diagram.getGraph().nodes().forEach(n -> {
            NodeImpl elm = (NodeImpl) n;

            GWT.log("  elm " + " " + elm.getContent() + " " + elm.getContent().getClass().getCanonicalName());

            //ViewImpl view = (ViewImpl) elm.getContent();

            Edge edge = elm.asEdge();
            Node node = elm.asNode();

            GWT.log("Edge " + edge);
            GWT.log("Node " + node);

            if (elm.asNode().getContent() instanceof ViewImpl) {
                ViewImpl view = (ViewImpl) elm.asNode().getContent();
                //Bounds bounds = view1.getBounds();
                if (view.getDefinition() instanceof UserTask) {
                    UserTask userTask = (UserTask) view.getDefinition();
                    //GWT.log("            UserTask " + userTask + " " + bounds);
                    UserTaskConverter.convert(userTask, definitions);
                    BPMNShape shape = new BPMNShape(userTask.getId());
                    shape.setBounds(new Bounds(view.getBounds()));
                    elements.add(shape);

                    ElementParameters parameters = new ElementParameters(userTask.getId());
                    elementParameters.add(parameters);

                    parameters.setParameters(new ArrayList<>());
                    parameters.getParameters().add(new TimeParameters(new ProcessingTime(new NormalDistribution())));
                    parameters.getParameters().add(new ResourceParameters(new Availability(new FloatingParameter()), new Quantity(new FloatingParameter())));
                    parameters.getParameters().add(new CostParameters(new UnitCost(new FloatingParameter())));
                }
            }
        });


        GWT.log("**********************************************************************");


        diagram.getGraph().nodes().forEach(n -> {
            org.kie.workbench.common.stunner.core.graph.impl.NodeImpl elm = (org.kie.workbench.common.stunner.core.graph.impl.NodeImpl) n;
            GWT.log("n 1 " + elm + " " + elm.asNode().getClass().getCanonicalName());
            GWT.log("n 2 " + elm + " " + elm.asNode().getContent().getClass().getCanonicalName());
            BPMNDiagramImpl bpmnDiagram;
            if (elm.asNode().getContent() instanceof org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl) {

                org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl impl = (org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl) elm.asNode().getContent();

                GWT.log("impl " + impl.getDefinition() + " " + impl.getDefinition().getClass().getCanonicalName());


                if (impl.getDefinition() instanceof UserTask) {
                    UserTask userTask = (UserTask) impl.getDefinition();
                    GWT.log("            UserTask " + userTask);

                    userTask.getBpmnProperties().forEach(bpmn -> {
                        GWT.log("BPMN " + bpmn.toString());
                    });

                    GWT.log("getAssignmentsinfo " + userTask.getExecutionSet().getAssignmentsinfo());

                    GWT.log("getIsMultipleInstance " + userTask.getExecutionSet().getAssignmentsinfo());
                    GWT.log("getIsMultipleInstance " + userTask.getExecutionSet().getMultipleInstanceCollectionInput().getValue());
                    GWT.log("getReassignmentsInfo " + userTask.getExecutionSet().getReassignmentsInfo().getValue());

                    definitionList.add(userTask);

                } else if (impl.getDefinition() instanceof BPMNDiagramImpl) {
                    bpmnDiagram = (BPMNDiagramImpl) impl.getDefinition();
                    definitions.setProcess(bpmnDiagram.getDiagramSet());
                    definitions.getProcess().setDefinitionList(definitionList);
                    definitions.getRelationship().setSource(definitionsId);
                    definitions.getRelationship().setTarget(definitionsId);
                    definitions.setBpmnDiagram(bpmnDiagram);
                    bpmnDiagram.setPlane(plane);

                    bpmnDiagram.getDiagramSet().getImports().getValue().getDefaultImports().forEach(defaultImport -> {
                        GWT.log("defaultImport " + defaultImport.getClassName());
                    });

                    bpmnDiagram.getDiagramSet().getImports().getValue().getWSDLImports().forEach(wsdl -> {
                        //definitions.getItemDefinitions().add(new ItemDefinition(wsdl.getLocation(), wsdl.getNamespace()));
                        GWT.log("wsdl " + wsdl.getLocation() + " " + wsdl.getNamespace());
                    });

                    bpmnDiagram.getDimensionsSet();
                    bpmnDiagram.getAdvancedData().getGlobalVariables();
                    bpmnDiagram.getBackgroundSet();


                    GWT.log("getProcessData " + bpmnDiagram.getProcessData().getProcessVariables().getValue());
                    GWT.log("getMetaDataAttributes " + bpmnDiagram.getAdvancedData().getMetaDataAttributes().getValue());
                    GWT.log("getMetaDataAttributes " + bpmnDiagram.getAdvancedData().getGlobalVariables().getValue());


                }

            }

            if (elm.asNode() instanceof org.kie.workbench.common.stunner.core.graph.impl.NodeImpl) {
                org.kie.workbench.common.stunner.core.graph.impl.NodeImpl node = (org.kie.workbench.common.stunner.core.graph.impl.NodeImpl) elm.asNode();


                GWT.log("\n " + node + " " + node.asNode() + " " + node.asEdge());

            }
        });

        try {
            XMLSerializationContext context = DefaultXMLSerializationContext.builder().writeEmptyXMLArrays(false).build();

            return Definitions_MapperImpl.INSTANCE.write(definitions, context);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return "";
        }

    }
}
