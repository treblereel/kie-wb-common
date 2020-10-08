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

package org.kie.workbench.common.stunner.bpmn.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataInputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.dto.DataOutputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.dto.MultiInstanceLoopCharacteristics;

/**
 * Marker interface for all BPMN properties.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataInputAssociation.class, name = "dataInputAssociation"),
        @JsonSubTypes.Type(value = DataOutputAssociation.class, name = "dataOutputAssociation"),
        @JsonSubTypes.Type(value = MultiInstanceLoopCharacteristics.class, name = "multiInstanceLoopCharacteristics")
})
public interface BPMNProperty {

}
