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

package org.kie.workbench.common.stunner.bpmn.backend.converters.customproperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.kie.workbench.common.stunner.bpmn.backend.converters.fromstunner.associations.AssociationType;

public class ParsedNotificationsInfos {

    Notification notification = new Notification();

    public static String of(String type, String body){
        return new ParsedNotificationsInfos(type, body).toJSON();
    }

    private ParsedNotificationsInfos(String type, String body){
        notification.type = type;

        if (body != null && !body.isEmpty()) {
            String temp;
            if (body.contains("@")) {
                String[] parts = body.split("@");
                parsePeriod(notification, parts[1]);
                temp = parts[0];
            }else {
                temp = body;
            }
            temp = replaceBracket(temp);

            getFrom(notification, temp);
            getUsers(notification, temp);
            getGroups(notification, temp);
            getReplyTo(notification, temp);
            getSubject(notification, temp);
            getBody(notification, temp);
        }
    }

    public static String ofCDATA(String json, AssociationType type){
        return new ParsedNotificationsInfos.CDATA(json, type).get();
    }

    private static class CDATA {
        private List<Notification>  notifications;

        private AssociationType type;

        CDATA(String json, AssociationType type){
            this.type = type;
            notifications = new Gson().
                    fromJson(json, new TypeToken<List<Notification>>(){}.getType());
        }
        String get(){
            return notifications.stream().filter(m -> m.type.equals(type.getName()))
                    .map(m -> m.toCDATAFormat())
                    .collect(Collectors.joining("^"));
        }
    }

    private String toJSON(){
        return new Gson().toJson(notification);
    }

    private static void getFrom(Notification notification, String body) {
        notification.from = parseElement(body, "from", 0);
    }

    private static void getUsers(Notification notification, String body) {
        notification.tousers = parseGroup(body, "tousers", 1);
    }

    private static void getGroups(Notification notification, String body) {
        notification.togroups = parseGroup(body, "togroups", 2);
    }

    private static void getReplyTo(Notification notification, String body) {
        notification.replyTo = parseElement(body, "replyTo", 3);
    }

    private static void getSubject(Notification notification, String body) {
        notification.subject = parseElement(body, "subject", 4);
    }

    private static void getBody(Notification notification, String body) {
        notification.body = parseElement(body, "body", 5);
    }

    private static String parseElement(String group, String type, int position) {
        if (group.contains(type)) {
            return group
                    .split("\\|")[position]
                    .replace(type+":", "");
        }
        return "";
    }

    private static List<String> parseGroup(String group, String type, int position) {
        if (group.contains(type)) {
            String[] arr = group
                    .split("\\|")[position]
                    .replace(type+":", "")
                    .split(",");
            return Arrays.stream(arr).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private static void parsePeriod(Notification notification, String part) {
        notification.expiresAt = replaceBracket(part);

    }

    private static String replaceBracket(String original){
        return original.replaceFirst("\\[", "").replace("]", "");
    }

    public static class Notification {

        private String body;
        private String expiresAt;
        private String from;
        private String replyTo;
        private String subject;
        private String type;
        private List<String> tousers = new ArrayList<>();
        private List<String> togroups = new ArrayList<>();

        @Override
        public String toString() {
            return "Notification{" +
                    ", type='" + type + '\'' +
                    ", from='" + from + '\'' +
                    ", replyTo='" + replyTo + '\'' +
                    ", subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", expiresAt='" + expiresAt + '\'' +
                    ", users='" + tousers.stream().collect(Collectors.joining(", ")) + '\'' +
                    ", groups='" + togroups.stream().collect(Collectors.joining(", ")) + '\'' +
                    '}';
        }

        public String toCDATAFormat() {
            return  "[from:" + from  +
                    "|tousers:"+tousers.stream().collect(Collectors.joining(", "))+
                    "|togroups:"+togroups.stream().collect(Collectors.joining(", ")) +
                    "|replyTo:"+replyTo +
                    "|subject:"+subject +
                    "|body:"+body +
                    "]@["+expiresAt+"]";
        }
    }
}
