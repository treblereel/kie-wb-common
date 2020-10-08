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

import javax.xml.stream.XMLStreamException;

import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions_XMLMapperImpl;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.treblereel.gwt.jackson.api.DefaultXMLSerializationContext;
import org.treblereel.gwt.jackson.api.XMLSerializationContext;

public class DiagramToXmlFactory extends DiagramFactory {

    public DiagramToXmlFactory(Diagram diagram) {
        super(diagram);
    }

    public String toXml() {
        Definitions definitions = process();
        try {
            XMLSerializationContext context = DefaultXMLSerializationContext.builder()
                    .serializeNulls(false)
                    .writeEmptyXMLArrays(false).build();

            return Definitions_XMLMapperImpl.INSTANCE.write(definitions, context);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return "";
        }
    }
}
