/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.library.client.screens.organizationalunit.contributors.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.guvnor.common.services.project.client.security.ProjectController;
import org.guvnor.structure.contributors.Contributor;
import org.guvnor.structure.contributors.ContributorType;
import org.guvnor.structure.repositories.Repository;
import org.guvnor.structure.repositories.RepositoryService;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.uberfire.rpc.SessionInfo;

public class ProjectContributorsListServiceImpl implements ContributorsListService {

    private LibraryPlaces libraryPlaces;

    private Caller<RepositoryService> repositoryService;

    private SpaceContributorsListServiceImpl spaceContributorsListService;

    private SessionInfo sessionInfo;

    private ProjectController projectController;

    private ContributorsSecurityUtils contributorsSecurityUtils;

    @Inject
    public ProjectContributorsListServiceImpl(final LibraryPlaces libraryPlaces,
                                              final Caller<RepositoryService> repositoryService,
                                              final SpaceContributorsListServiceImpl spaceContributorsListService,
                                              final SessionInfo sessionInfo,
                                              final ProjectController projectController,
                                              final ContributorsSecurityUtils contributorsSecurityUtils) {
        this.libraryPlaces = libraryPlaces;
        this.repositoryService = repositoryService;
        this.spaceContributorsListService = spaceContributorsListService;
        this.sessionInfo = sessionInfo;
        this.projectController = projectController;
        this.contributorsSecurityUtils = contributorsSecurityUtils;
    }

    @Override
    public void getContributors(Consumer<List<Contributor>> contributorsConsumer) {
        repositoryService.call((Repository repository) -> {
            contributorsConsumer.accept(new ArrayList<>(repository.getContributors()));
        }).getRepositoryFromSpace(libraryPlaces.getActiveSpace().getSpace(), libraryPlaces.getActiveWorkspace().getRepository().getAlias());
    }

    @Override
    public void saveContributors(final List<Contributor> contributors,
                                 final Runnable successCallback,
                                 final ErrorCallback<Message> errorCallback) {
        repositoryService.call((Repository repository) -> {
            repositoryService.call((Void) -> successCallback.run(), errorCallback).updateContributors(repository, contributors);
        }).getRepositoryFromSpace(libraryPlaces.getActiveSpace().getSpace(), libraryPlaces.getActiveWorkspace().getRepository().getAlias());
    }

    @Override
    public boolean canEditContributors(final List<Contributor> contributors,
                                       final ContributorType type) {
        boolean canEdit = false;

        final Optional<Contributor> contributor = contributors.stream().filter(c -> c.getUsername().equals(sessionInfo.getIdentity().getIdentifier())).findFirst();
        if (contributor.isPresent()) {
            final ContributorType userContributorType = contributor.get().getType();
            canEdit = contributorsSecurityUtils.canUserEditContributorOfType(userContributorType, type);
        }

        canEdit = canEdit || projectController.canUpdateProject(libraryPlaces.getActiveWorkspace());

        return canEdit;
    }



    @Override
    public void getValidUsernames(Consumer<List<String>> validUsernamesConsumer) {
        spaceContributorsListService.getContributors(contributors -> {
            validUsernamesConsumer.accept(contributors.stream().map(c -> c.getUsername()).collect(Collectors.toList()));
        });
    }
}
