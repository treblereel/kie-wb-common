/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.definition.dto.bpsim;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.treblereel.gwt.jackson.api.annotation.XmlUnwrappedCollection;

public class Scenario {

    @XmlAttribute
    private String id = "default";
    @XmlAttribute
    private String name = "Simulationscenario";

    @XmlElement(name ="ScenarioParameters")
    private ScenarioParameters scenarioParameters;

    @XmlElement(name = "ElementParameters")
    @XmlUnwrappedCollection
    private List<ElementParameters> elementParameters;

    public Scenario() {

    }

    public Scenario(List<ElementParameters> elementParameters) {
        this.elementParameters = elementParameters;
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

    public ScenarioParameters getScenarioParameters() {
        return scenarioParameters;
    }

    public void setScenarioParameters(ScenarioParameters scenarioParameters) {
        this.scenarioParameters = scenarioParameters;
    }

    public List<ElementParameters> getElementParameters() {
        return elementParameters;
    }

    public void setElementParameters(List<ElementParameters> elementParameters) {
        this.elementParameters = elementParameters;
    }
}
