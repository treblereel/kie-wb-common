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

public class ParsedReassignmentsInfos {
    Reassignment reassignment = new Reassignment();

    public static String of(String id, String type, String body){
        return new ParsedReassignmentsInfos(id, type, body).toJSON();
    }

    public static String ofCDATA(String json, AssociationType type){
        return new CDATA(json, type).get();
    }

    private ParsedReassignmentsInfos(String id, String type, String body){
        reassignment.id = id;
        reassignment.type = type;

        if (body != null && !body.isEmpty()) {
            String usersAndGroups;
            if (body.contains("@")) {
                String[] parts = body.split("@");
                parsePeriod(reassignment, parts[1]);
                usersAndGroups = parts[0];
            }else {
                usersAndGroups = body;
            }
            usersAndGroups = replaceBracket(usersAndGroups);

            getUsers(reassignment, usersAndGroups);
            getGroups(reassignment, usersAndGroups);
        }

    }

    private String toJSON(){
        return new Gson().toJson(reassignment);
    }

    private static void getUsers(Reassignment reassignment, String usersAndGroups) {
        reassignment.users = parseGroup(usersAndGroups, "users", 0);
    }

    private static void getGroups(Reassignment reassignment, String usersAndGroups) {
        reassignment.groups = parseGroup(usersAndGroups, "groups", 1);
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

    private static void parsePeriod(Reassignment reassignment, String part) {
        reassignment.period = replaceBracket(part);

    }

    private static String replaceBracket(String original){
        return original.replaceFirst("\\[", "").replace("]", "");
    }

    private static class CDATA {
        private List<ParsedReassignmentsInfos.Reassignment>  reassignments;

        private AssociationType type;

        CDATA(String json, AssociationType type){
            this.type = type;
            reassignments = new Gson().
                    fromJson(json, new TypeToken<List<Reassignment>>(){}.getType());
        }

        String get(){
            return reassignments.stream().filter(m -> m.type.equals(type.getName()))
                    .map(m -> m.toCDATAFormat())
                    .collect(Collectors.joining("^"));
        }
    }

    public static class Reassignment {
        public String id;
        public String type;
        public String period;
        public List<String> users = new ArrayList<>();
        public List<String> groups = new ArrayList<>();

        @Override
        public String toString() {
            return "Reassignment{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", period='" + period + '\'' +
                    ", users='" + users.stream().collect(Collectors.joining(", ")) + '\'' +
                    ", groups='" + groups.stream().collect(Collectors.joining(", ")) + '\'' +
                    '}';
        }

        public String toCDATAFormat() {
            return "[users:"+users.stream().collect(Collectors.joining(", "))+
                   "|groups:"+groups.stream().collect(Collectors.joining(", ")) +"]"+
                    "@["+period+"]";
        }
    }
}
