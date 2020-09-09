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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;

import org.treblereel.gwt.jackson.api.DefaultXMLDeserializationContext;
import org.treblereel.gwt.jackson.api.XMLDeserializationContext;

public class DefinitionsTest {

    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void parseTest() throws IOException, XMLStreamException {

        System.out.println(new File("").getAbsolutePath());
        String xml = readFile("src/test/java/org/kie/workbench/common/stunner/bpmn/definition/dto/test.bpmn", Charset.defaultCharset());
        System.out.println("\n\n");
        System.out.println("XML \n" + readFile("src/test/java/org/kie/workbench/common/stunner/bpmn/definition/dto/test.bpmn", Charset.defaultCharset()));

        XMLDeserializationContext context = DefaultXMLDeserializationContext.builder().failOnUnknownProperties(false).build();
        //Definitions definitions = Definitions_MapperImpl.INSTANCE.read(xml, context);
        Definitions definitions = Definitions_MapperImpl.INSTANCE.read(xml);

        //Assert.assertEquals(2, definitions.getProcess().getDefinitionList().size());
        //Assert.assertEquals(0, definitions.getItemDefinitions().size());
        //Assert.assertNull(definitions.getBpmnDiagram());
        Assert.assertNotNull(definitions.getBpmnDiagram());

        System.out.println("\n\n");
        System.out.println("REZ \n" + Definitions_MapperImpl.INSTANCE.write(definitions));

    }
}
