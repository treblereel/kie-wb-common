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

public class Notification {

    private String body = "";

    private String expiresAt = "0d";

    private String from = "";

    private List<String> groups = new ArrayList<>();

    private String replyTo = "";

    private String subject = "";

    private List<String> users = new ArrayList<>();

    private String type;

    public Notification() {

    }

    public Notification(NotificationRow notification) {
        this.setType(notification.getType().getAlias());
        this.setExpiresAt(notification.getExpiresAt());
        this.setGroups(notification.getGroups().stream().collect(Collectors.toList()));
        this.setUsers(notification.getUsers().stream().collect(Collectors.toList()));
        this.setBody(notification.getBody());
        this.setSubject(notification.getSubject());
        this.setFrom(notification.getFrom());
        this.setReplyTo(notification.getReplyTo());
    }

    public static Notification fromJSONObject(JSONObject obj) {
        Notification notification = new Notification();
        if (obj.containsKey("type")) {
            notification.setType(obj.get("type").isString().stringValue());
        }
        if (obj.containsKey("expiresAt")) {
            notification.setExpiresAt(obj.get("expiresAt").isString().stringValue());
        }
        if (obj.containsKey("body")) {
            notification.setBody(obj.get("body").isString().stringValue());
        }
        if (obj.containsKey("subject")) {
            notification.setSubject(obj.get("subject").isString().stringValue());
        }

        if (obj.containsKey("from")) {
            notification.setFrom(obj.get("from").isString().stringValue());
        }

        if (obj.containsKey("replyTo")) {
            notification.setReplyTo(obj.get("replyTo").isString().stringValue());
        }

        if (obj.containsKey("tousers")) {
            JSONArray array = obj.get("tousers").isArray();
            List<String> users = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                users.add(array.get(i).isString().stringValue());
            }
            notification.setUsers(users);
        } else {
            notification.setUsers(Collections.EMPTY_LIST);
        }

        if (obj.containsKey("togroups")) {
            JSONArray array = obj.get("togroups").isArray();
            List<String> groups = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                groups.add(array.get(i).isString().stringValue());
            }
            notification.setGroups(groups);
        } else {
            notification.setGroups(Collections.EMPTY_LIST);
        }

        return notification;
    }

    public String toString() {
        return "{\"type\":\"" + getType() + "\"," +
                "\"body\":\"" + getBody() + "\"," +
                "\"subject\":\"" + getSubject() + "\"," +
                "\"replyTo\":\"" + getReplyTo() + "\"," +
                "\"from\":\"" + getFrom() + "\"," +
                "\"expiresAt\":\"" + getExpiresAt() + "\"," +
                "\"tousers\":[" + getUsers().stream().map(u -> "\"" + u + "\"").collect(Collectors.joining(",")) + "]," +
                "\"togroups\":[" + getGroups().stream().map(u -> "\"" + u + "\"").collect(Collectors.joining(",")) + "]}";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
}
