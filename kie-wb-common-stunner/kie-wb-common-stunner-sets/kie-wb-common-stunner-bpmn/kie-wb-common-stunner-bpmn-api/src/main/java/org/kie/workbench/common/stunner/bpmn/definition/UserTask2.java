/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import java.util.HashSet;
import java.util.Set;

import org.kie.soup.commons.util.Sets;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Labels;

/*@Portable
@Bindable
@Definition(graphFactory = NodeFactory.class)
@CanDock(roles = {"IntermediateEventOnActivityBoundary"})
@Morph(base = BaseTask.class)
@FormDefinition(
        policy = FieldPolicy.ONLY_MARKED,
        startElement = "general",
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)*/
public class UserTask2 implements DataIOModel, BPMNDefinition {

    public static final Set<String> TASK_LABELS = new Sets.Builder<String>()
            .add("all")
            .add("lane_child")
            .add("sequence_start")
            .add("sequence_end")
            .add("from_task_event")
            .add("to_task_event")
            .add("messageflow_start")
            .add("messageflow_end")
            .add("fromtoall")
            .add("ActivitiesMorph")
            .add("cm_activity")
            .build();

    @Category
    public static final transient String category = BPMNCategories.ACTIVITIES;

    @Labels
    protected final Set<String> labels = new HashSet<>(TASK_LABELS);

    public String getCategory() {
        return category;
    }

    public Set<String> getLabels() {
        return labels;
    }

/*    @PropertySet
    @FormField(
            afterElement = "general"
    )
    @Valid
    protected UserTaskExecutionSet executionSet;*/



/*    public UserTask() {
        this(new TaskGeneralSet(new Name("Task"),
                                new Documentation("")),
             new UserTaskExecutionSet(),
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new TaskType(TaskTypes.USER));
    }

    public UserTask(final @MapsTo("general") TaskGeneralSet general,
                    final @MapsTo("executionSet") UserTaskExecutionSet executionSet,
                    final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                    final @MapsTo("fontSet") FontSet fontSet,
                    final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                    final @MapsTo("simulationSet") SimulationSet simulationSet,
                    final @MapsTo("taskType") TaskType taskType) {
        super(general,
              backgroundSet,
              fontSet,
              dimensionsSet,
              simulationSet,
              taskType);
        this.executionSet = executionSet;
    }*/

/*    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserTask) {
            UserTask other = (UserTask) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }*/

    @Override
    public boolean hasInputVars() {
        return true;
    }

    @Override
    public boolean isSingleInputVar() {
        return false;
    }

    @Override
    public boolean hasOutputVars() {
        return true;
    }

    @Override
    public boolean isSingleOutputVar() {
        return false;
    }

    @Override
    public BPMNBaseInfo getGeneral() {
        return null;
    }
}
