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

import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.AssignmentDemarshaller;
import org.kie.workbench.common.stunner.bpmn.definition.dto.handler.AssignmentMarshaller;
import org.treblereel.gwt.jackson.api.annotation.XmlTypeAdapter;

@XmlTypeAdapter(
        serializer = AssignmentMarshaller.class,
        deserializer = AssignmentDemarshaller.class)
public class Assignment {
    private String from;
    private String to;

    public Assignment() {

    }

    public Assignment(String value, String id, String postfix) {
        this.from = value;
        this.to = id + "_" + postfix;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
