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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.stunner.core.util.HashUtil;

/**
 * Class which is bound to rows in the NotificationEditor
 */
@Bindable
public class NotificationRow {

    private long id;

    // Field which is incremented for each row.
    // Required to implement equals function which needs a unique field
    private static long lastId = 0;

    private String body = "";

    private String expiresAt = "0h";

    private String from = "";

    private List<String> groups = new ArrayList<>();

    private String replyTo = "";

    private String subject = "";

    private List<String> users = new ArrayList<>();

    private NotificationType type = NotificationType.NotCompletedNotify;

    public NotificationRow(){
        this.id = lastId++;
    }

    public NotificationRow(Notification notification){
        this.id = lastId++;
        this.setType(NotificationType.get(notification.getType()));
        this.setExpiresAt(notification.getExpiresAt());
        this.setGroups(notification.getGroups().stream().collect(Collectors.toList()));
        this.setUsers(notification.getUsers().stream().collect(Collectors.toList()));
        this.setBody(notification.getBody());
        this.setSubject(notification.getSubject());
        this.setFrom(notification.getFrom());
        this.setReplyTo(notification.getReplyTo());
    }

    public NotificationRow clone(){
        NotificationRow clone = new NotificationRow();
        clone.setId(getId());
        clone.setExpiresAt(getExpiresAt());
        clone.setType(getType());
        clone.setGroups(getGroups());
        clone.setUsers(getUsers());
        clone.setFrom(getFrom());
        clone.setReplyTo(getReplyTo());
        clone.setSubject(getSubject());
        clone.setBody(getBody());
        return clone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NotificationRow other = (NotificationRow) obj;
        return (id == other.getId());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                Objects.hashCode(id));
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

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NotificationRow{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", expiresAt='" + expiresAt + '\'' +
                ", from='" + from + '\'' +
                ", groups=" + groups.stream().collect(Collectors.joining(",")) +
                ", replyTo='" + replyTo + '\'' +
                ", subject='" + subject + '\'' +
                ", users=" + users.stream().collect(Collectors.joining(",")) +
                ", type=" + type +
                '}';
    }
}
