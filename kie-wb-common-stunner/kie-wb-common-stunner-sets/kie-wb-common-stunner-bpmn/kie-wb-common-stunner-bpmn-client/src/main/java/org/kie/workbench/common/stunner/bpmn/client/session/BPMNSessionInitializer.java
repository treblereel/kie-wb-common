/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.client.session;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.workbench.common.stunner.bpmn.client.diagram.DiagramTypeService;
import org.kie.workbench.common.stunner.bpmn.client.workitem.WorkItemDefinitionClientRegistry;
import org.kie.workbench.common.stunner.bpmn.qualifiers.BPMN;
import org.kie.workbench.common.stunner.core.client.session.impl.SessionInitializer;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.uberfire.mvp.Command;

@BPMN
@ApplicationScoped
public class BPMNSessionInitializer implements SessionInitializer {

    private final WorkItemDefinitionClientRegistry workItemDefinitionService;
    private final DiagramTypeService diagramTypeService;

    // CDI proxy.
    protected BPMNSessionInitializer() {
        this(null, null);
    }

    @Inject
    public BPMNSessionInitializer(final WorkItemDefinitionClientRegistry workItemDefinitionService, final DiagramTypeService diagramTypeService) {
        this.workItemDefinitionService = workItemDefinitionService;
        this.diagramTypeService = diagramTypeService;
    }

    @Override
    public void init(final Metadata metadata,
                     final Command completeCallback) {
        diagramTypeService.loadDiagramType(metadata);
        workItemDefinitionService.load(metadata, completeCallback);
    }
}
