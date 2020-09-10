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

package org.kie.workbench.common.stunner.kogito.client.marshalling.tostunner;

import javax.xml.stream.XMLStreamException;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.stunner.bpmn.client.marshall.service.BPMNClientMarshalling;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagramImpl;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions_MapperImpl;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpmndi.BPMNPlaneElement;
import org.kie.workbench.common.stunner.bpmn.definition.dto.bpmndi.BPMNShape;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.WSDLImport;
import org.kie.workbench.common.stunner.core.api.DefinitionManager;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.DiagramImpl;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.diagram.MetadataImpl;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.Bounds;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl;
import org.kie.workbench.common.stunner.core.graph.impl.NodeImpl;
import org.kie.workbench.common.stunner.core.util.UUID;
import org.treblereel.gwt.jackson.api.DefaultXMLDeserializationContext;
import org.treblereel.gwt.jackson.api.XMLDeserializationContext;
import org.uberfire.backend.vfs.PathFactory;

public class XmlToDiagramFactory {


    private static final String ROOT_PATH = "default://master@system/stunner/diagrams";

    public static Diagram toDiagram(String xml, Diagram diagram) {
        //GWT.log("setXML " + xml);
        try {
            XMLDeserializationContext context = DefaultXMLDeserializationContext.builder().failOnUnknownProperties(false).build();
            Definitions definitions = Definitions_MapperImpl.INSTANCE.read(xml, context);

            GWT.log("rez " + definitions);

            NodeImpl element = (NodeImpl) diagram.getGraph().nodes().iterator().next();
            BPMNDiagramImpl bpmnDiagram = (BPMNDiagramImpl) ((View) element.asNode().getContent()).getDefinition();


            GWT.log("1 " + definitions.getProcess());

            bpmnDiagram.setDiagramSet(definitions.getProcess());


            definitions.getImports().stream().map(imp -> new WSDLImport(imp.getLocation(), imp.getNamespace()))
                    .forEach(imp -> {
                        GWT.log("IMP " + imp);
                    });

            definitions.getProcess().getExtensionElements().forEach(elm -> {
                GWT.log("ELM " + elm.getName());
            });

/*            definitions.getImports().stream().map(imp -> new WSDLImport(imp.getLocation(), imp.getNamespace()))
                    .forEach(imp -> bpmnDiagram.getDiagramSet().getImports().getValue().getWSDLImports().add(imp));*/

/*            definitions.getBpmnDiagram().getPlane()
                    .getElements()
                    .stream().forEach(e -> {
                GWT.log("BPMNShape '" + e.getId() + "'" + e.getClass().getCanonicalName());

            });*/


            definitions.getProcess().getDefinitionList().forEach(definition -> {
                if (definition instanceof UserTask) {
                    String id = ((UserTask) definition).getId();

                    GWT.log("UserTask id ' " + id + "'");


                    BPMNPlaneElement planeElement = definitions.getBpmnDiagram().getPlane()
                            .getElements()
                            .stream()
                            .filter(elm -> elm.getId().equals("shape_" + id))
                            .findFirst()
                            .orElseThrow(() -> new Error("Unable to find a Bound " + id));

                    GWT.log("BPMNShape " + ((BPMNShape) planeElement).getBounds());

                    org.kie.workbench.common.stunner.bpmn.definition.dto.dc.Bounds bounds = ((BPMNShape) planeElement).getBounds();
                    diagram.getGraph().addNode(newNode(definition, Bounds.create(bounds.getX(),
                            bounds.getY(),
                            bounds.getHeight(),
                            bounds.getWidth())));
                }
                GWT.log("DEF " + definition + " " + definition.getClass().getSimpleName());
            });

            //GWT.log("? " + definitions.getBpmnDiagram() + " " + definitions.getBpmnDiagram().getClass().getCanonicalName());
/*            definitions.getBpmnDiagram().getPlane().getElements().forEach(elm -> {
                GWT.log("PLANE " + elm + " " + elm.getClass().getSimpleName());
            });*/

            return diagram;
        } catch (XMLStreamException e) {
            e.printStackTrace();
            GWT.log(e.getMessage());
        }
        return null;
    }

    private static Node<View, Edge> newNode(final Object definition, Bounds bounds) {
        final Node<View, Edge> node = new NodeImpl<>(UUID.uuid());
        final View<Object> content = new ViewImpl<>(definition, bounds);
        node.setContent(content);
        return node;
    }

    private static Metadata createMetadata(DefinitionManager definitionManager) {
        return new MetadataImpl.MetadataImplBuilder(BPMNClientMarshalling.getDefinitionSetId(),
                definitionManager)
                .setRoot(PathFactory.newPath(".", ROOT_PATH))
                .build();
    }

    private DiagramImpl convert(final Diagram diagram) {
        return new DiagramImpl(diagram.getName(),
                diagram.getGraph(),
                diagram.getMetadata());
    }
}
