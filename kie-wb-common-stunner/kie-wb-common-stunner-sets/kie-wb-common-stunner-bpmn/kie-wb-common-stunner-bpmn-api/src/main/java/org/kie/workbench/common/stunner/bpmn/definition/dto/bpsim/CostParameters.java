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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CostParameters", namespace = "http://www.bpsim.org/schemas/1.0")
public class CostParameters implements Parameters {

    @XmlElement(name = "UnitCost")
    private UnitCost unitCost;

    public CostParameters() {

    }

    public CostParameters(UnitCost unitCost) {
        this.unitCost = unitCost;
    }

    public UnitCost getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(UnitCost unitCost) {
        this.unitCost = unitCost;
    }
}
