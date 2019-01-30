/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.client.forms.fields.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class Reassignment {

    private String type;

    private String duration;

    private List<String> users, groups;

    public Reassignment() {

    }

    public Reassignment(ReassignmentRow row) {
        this.setType(row.getType().getAlias());
        this.setDuration(row.getDuration());
        this.setGroups(row.getGroups().stream().collect(Collectors.toList()));
        this.setUsers(row.getUsers().stream().collect(Collectors.toList()));
    }

    public static Reassignment fromJSONObject(JSONObject obj) {
        Reassignment reassignment = new Reassignment();
        if (obj.containsKey("type")) {
            reassignment.setType(obj.get("type").isString().stringValue());
        }
        if (obj.containsKey("period")) {
            reassignment.setDuration(obj.get("period").isString().stringValue());
        }

        if (obj.containsKey("users")) {
            JSONArray array = obj.get("users").isArray();
            List<String> users = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                users.add(array.get(i).isString().stringValue());
            }
            reassignment.setUsers(users);
        } else {
            reassignment.setUsers(Collections.EMPTY_LIST);
        }

        if (obj.containsKey("groups")) {
            JSONArray array = obj.get("groups").isArray();
            List<String> groups = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                groups.add(array.get(i).isString().stringValue());
            }
            reassignment.setGroups(groups);
        } else {
            reassignment.setGroups(Collections.EMPTY_LIST);
        }

        return reassignment;
    }

    public String toString() {
        return "{\"type\":\"" + getType() + "\"," +
                "\"period\":\"" + getDuration() + "\"," +
                "\"users\":[" + getUsers().stream().map(u ->"\""+u+"\"").collect(Collectors.joining(",")) + "]," +
                "\"groups\":[" + getGroups().stream().map(u ->"\""+u+"\"").collect(Collectors.joining(",")) + "]}";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
