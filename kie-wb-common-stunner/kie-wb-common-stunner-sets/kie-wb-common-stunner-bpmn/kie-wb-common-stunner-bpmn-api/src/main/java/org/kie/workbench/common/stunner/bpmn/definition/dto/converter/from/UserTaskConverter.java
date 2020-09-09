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

package org.kie.workbench.common.stunner.bpmn.definition.dto.converter.from;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.stunner.bpmn.definition.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.dto.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.dto.ItemDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.UserTaskExecutionSet;

public class UserTaskConverter {

    public static void convert(UserTask userTask, Definitions definitions) {
        UserTaskExecutionSet executionSet = userTask.getExecutionSet();

        if (executionSet.getSkippable().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "Skippable"));
        }

        if (executionSet.getPriority().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "Priority"));
        }

        if (executionSet.getSubject().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "Comment"));
        }

        if (executionSet.getDescription().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "Description"));
        }

        if (executionSet.getCreatedBy().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "CreatedBy"));
        }

        if (executionSet.getTaskName().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "TaskName"));
        }

        if (executionSet.getGroupid().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "GroupId"));
        }

        if (executionSet.getContent().getValue() != null) {
            definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), "Content"));
        }

        if (executionSet.getReassignmentsInfo().getValue() != null) {
            executionSet.getReassignmentsInfo().getValue().getValues().forEach(value ->
                    definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), value.getType())));
        }

        if (executionSet.getNotificationsInfo().getValue() != null) {
            executionSet.getNotificationsInfo().getValue().getValues().forEach(value ->
                    definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(), value.getType())));
        }

        if (Boolean.TRUE.equals(executionSet.getIsMultipleInstance().getValue())) {
            GWT.log("1 " + executionSet.getMultipleInstanceCollectionInput().getValue());
            GWT.log("2 " + executionSet.getMultipleInstanceCollectionOutput().getValue());
            GWT.log("3 " + executionSet.getMultipleInstanceDataInput().getValue());
            GWT.log("3 " + executionSet.getMultipleInstanceDataInput().getValue());
            GWT.log("4 " + executionSet.getMultipleInstanceDataOutput().getValue());


            if (executionSet.getMultipleInstanceDataInput().getValue() != null) {
                String[] value = executionSet.getMultipleInstanceDataInput().getValue().split(":");
                definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(),
                        "multiInstanceItemType_" + value[0], value[1]));
            }
            if (executionSet.getMultipleInstanceDataOutput().getValue() != null) {
                String[] value = executionSet.getMultipleInstanceDataOutput().getValue().split(":");
                definitions.getItemDefinitions().add(new ItemDefinition(userTask.getId(),
                        "multiInstanceItemType_" + value[0], value[1]));
            }
        }

/*        this.priority = CustomInput.priority.of(task);
        definitions.getItemDefinitions().add(this.priority.typeDef());

        this.subject = CustomInput.subject.of(task);
        definitions.getItemDefinitions().add(this.subject.typeDef());

        this.description = CustomInput.description.of(task);
        definitions.getItemDefinitions().add(this.description.typeDef());

        this.createdBy = CustomInput.createdBy.of(task);
        definitions.getItemDefinitions().add(this.createdBy.typeDef());

        this.taskName = CustomInput.taskName.of(task);
        definitions.getItemDefinitions().add(this.taskName.typeDef());

        this.groupId = CustomInput.groupId.of(task);
        definitions.getItemDefinitions().add(this.groupId.typeDef());

        this.content = CustomInput.content.of(task);
        definitions.getItemDefinitions().add(this.content.typeDef());

        this.notStartedReassign = CustomInput.notStartedReassign.of(task);
        definitions.getItemDefinitions().add(this.notStartedReassign.typeDef());

        this.notCompletedReassign = CustomInput.notCompletedReassign.of(task);
        definitions.getItemDefinitions().add(this.notCompletedReassign.typeDef());

        this.notStartedNotify = CustomInput.notStartedNotify.of(task);
        definitions.getItemDefinitions().add(this.notStartedNotify.typeDef());

        this.notCompletedNotify = CustomInput.notCompletedNotify.of(task);
        definitions.getItemDefinitions().add(this.notCompletedNotify.typeDef());*/


/*        p.setTaskName(executionSet.getTaskName().getValue());
        p.setActors(executionSet.getActors());
        p.setAssignmentsInfo(executionSet.getAssignmentsinfo());
        p.setReassignments(executionSet.getReassignmentsInfo());
        p.setNotifications(executionSet.getNotificationsInfo());
        p.setSkippable(executionSet.getSkippable().getValue());
        p.setGroupId(executionSet.getGroupid().getValue());
        p.setSubject(executionSet.getSubject().getValue());
        p.setDescription(executionSet.getDescription().getValue());
        p.setPriority(executionSet.getPriority().getValue());
        p.setAsync(executionSet.getIsAsync().getValue());
        p.setCreatedBy(executionSet.getCreatedBy().getValue());
        p.setAdHocAutostart(executionSet.getAdHocAutostart().getValue());
        if (Boolean.TRUE.equals(executionSet.getIsMultipleInstance().getValue())) {
            p.setIsSequential(executionSet.getMultipleInstanceExecutionMode().isSequential());
            p.setCollectionInput(executionSet.getMultipleInstanceCollectionInput().getValue());
            p.setInput(executionSet.getMultipleInstanceDataInput().getValue());
            p.setCollectionOutput(executionSet.getMultipleInstanceCollectionOutput().getValue());
            p.setOutput(executionSet.getMultipleInstanceDataOutput().getValue());
            p.setCompletionCondition(executionSet.getMultipleInstanceCompletionCondition().getValue());
        }
        p.setOnEntryAction(executionSet.getOnEntryAction());
        p.setOnExitAction(executionSet.getOnExitAction());
        p.setContent(executionSet.getContent().getValue());
        p.setSLADueDate(executionSet.getSlaDueDate().getValue());*/
    }
}
