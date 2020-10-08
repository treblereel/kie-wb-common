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

import java.util.HashSet;

import elemental2.dom.DomGlobal;
import org.dominokit.jacksonapt.DefaultJsonSerializationContext;
import org.dominokit.jacksonapt.JsonSerializationContext;
import org.dominokit.jacksonapt.exception.JsonSerializationException;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions_MapperImpl;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Import;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.ExtensionElement;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.Global;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.OnEntryScript;
import org.kie.workbench.common.stunner.bpmn.definition.dto.drools.OnExitScript;
import org.kie.workbench.common.stunner.core.diagram.Diagram;

public class DiagramToJsonFactory extends DiagramFactory {

    public DiagramToJsonFactory(Diagram diagram) {
        super(diagram);
    }

    public String toJson() {
        Definitions definitions = process();

        DomGlobal.console.log("definitions " + definitions);

        definitions.getImports().forEach(i -> {
            DomGlobal.console.log("I " + i.getLocation() + " " + i.getNamespace() + " '" + i.getImportType() + "'");
        });

        try {
            JsonSerializationContext context = DefaultJsonSerializationContext.builder()
                    .serializeNulls(false)
                    .writeEmptyJsonArrays(false)
                    .build();

            HashSet<ExtensionElement> temp = new HashSet();
            temp.add(new MetaData("AAA", "BBB"));
            temp.add(new MetaData("AAA1", "BBB1"));
            temp.add(new MetaData("AAA2", "BBB2"));

            definitions.getProcess().getExtensionElements().forEach(elm -> {
                if (elm instanceof MetaData) {
                    temp.add(elm);
                } else if (elm instanceof org.kie.workbench.common.stunner.bpmn.definition.dto.drools.Import) {
                    temp.add(elm);
                } else if (elm instanceof OnEntryScript) {
                    temp.add(elm);
                } else if (elm instanceof OnExitScript) {
                    temp.add(elm);

                } else if (elm instanceof Global) {
                    temp.add(elm);
                }

                DomGlobal.console.log("ELM " + elm.toString());
            });

            definitions.getProcess().setExtensionElements(temp);

            for (Import i : definitions.getImports()) {
                DomGlobal.console.log("I " + i.getLocation() + " " + i.getNamespace() + " '" + i.getImportType() + "'");
            }

            Definitions definitions1 = new Definitions();
            definitions1.setImports(definitions.getImports());
            definitions1.setId(definitions.getId());
            definitions1.setExporter(definitions.getExporter());
            definitions1.setRelationship(definitions.getRelationship());

            definitions1.setItemDefinitions(definitions.getItemDefinitions());

            definitions1.setProcess(definitions.getProcess());

            definitions1.setBpmnDiagram(definitions.getBpmnDiagram());
            definitions1.setName(definitions.getName());
            definitions1.setExporterVersion(definitions.getExporterVersion());

            DomGlobal.console.log("JSON \n" + Definitions_MapperImpl.INSTANCE.write(definitions1));

            //definitions.setImports(new ArrayList<>());

            //DomGlobal.console.log("JSON 2 \n" + Definitions_MapperImpl.INSTANCE.write(definitions));

            return Definitions_MapperImpl.INSTANCE.write(definitions1);
        } catch (JsonSerializationException e) {
            DomGlobal.console.log("JsonSerializationException " + e.getMessage());

            e.printStackTrace();
            return "";
        }
    }
}
